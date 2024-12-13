package dev.jlipka.pickly;

import lombok.Data;

import java.io.Serializable;

@Data
public class SerializableFile implements Serializable {
    private String fileName;
    private byte[] fileContent;

    public SerializableFile(String fileName, byte[] fileContent) {
        this.fileName = fileName;
        this.fileContent = fileContent;
    }
}
