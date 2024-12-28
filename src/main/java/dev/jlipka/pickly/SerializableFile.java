package dev.jlipka.pickly;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Data
@Slf4j
public class SerializableFile implements Serializable {
    private String fileName;
    private byte[] fileContent;

    public SerializableFile(File file) {
        this.fileName = file.getName();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            this.fileContent = fileInputStream.readAllBytes();
        } catch (FileNotFoundException e) {
            log.error("File not found: {}", file.getAbsoluteFile());
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("Error occured while reading a file: {}", file.getAbsoluteFile());
            throw new RuntimeException(e);
        }
    }

    public SerializableFile(String fileName, byte[] fileContent) {
        this.fileName = fileName;
        this.fileContent = fileContent;
    }
}
