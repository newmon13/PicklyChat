package dev.jlipka.pickly.filter;

import morfologik.stemming.WordData;
import morfologik.stemming.polish.PolishStemmer;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class ProfanityFilter {
    private CompletableFuture<PolishStemmer> polishStemmerFuture;
    private CompletableFuture<Set<String>> badWordsDictionaryFuture;

    public ProfanityFilter() {
        initPolishStemmer();
        initBadWordsDictionary();
    }

    private void initPolishStemmer() {
        polishStemmerFuture = CompletableFuture.supplyAsync(PolishStemmer::new);
    }

    private void initBadWordsDictionary() {
        badWordsDictionaryFuture = CompletableFuture.supplyAsync(() -> {
            Set<String> dictionary = new HashSet<>();
            try (InputStream inputStream = getClass().getResourceAsStream("/bad_words.txt")) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                Objects.requireNonNull(inputStream),
                                StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        dictionary.add(line.trim().toLowerCase());
                    }
                }
                return dictionary;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public boolean isBadWord(String word) {
        try {
            PolishStemmer stemmer = polishStemmerFuture.get();
            Set<String> dictionary = badWordsDictionaryFuture.get();

            word = word.toLowerCase();

            if (dictionary.contains(word)) {
                return true;
            } else {
                List<WordData> lookup = stemmer.lookup(word);
                return lookup.stream().anyMatch(e -> dictionary.contains(e.getStem().toString().toLowerCase()));
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Błąd podczas sprawdzania słowa", e);
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ProfanityFilter filter = new ProfanityFilter();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        PolishStemmer stemmer = filter.polishStemmerFuture.get();

        List<WordData> lookup = stemmer.lookup("huje");

        for (WordData data: lookup) {
            System.out.println("Stem: " + data.getStem() + " Word: " + data.getWord());
        }
    }
}
