package dev.jlipka.pickly;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
@Getter
public class SerializableFile extends File implements Serializable {
    private final String fileName;
    private final byte[] fileContent;

    public SerializableFile(File file) {
        super(file.getAbsolutePath());
        this.fileName = file.getName();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            this.fileContent = fileInputStream.readAllBytes();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            log.error("File not found: {}", file.getAbsoluteFile());
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("Error occurred while reading a file: {}", file.getAbsoluteFile());
            throw new RuntimeException(e);
        }
    }

    public SerializableFile(String fileName, byte[] fileContent) {
        super(fileName);
        this.fileName = fileName;
        this.fileContent = fileContent;
    }
}