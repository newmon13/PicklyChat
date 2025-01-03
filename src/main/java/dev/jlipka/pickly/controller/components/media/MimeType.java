package dev.jlipka.pickly.controller.components.media;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public enum MimeType {
    ARCHIVE("far-file-zipper", List.of("application/zip", "application/x-7z-compressed")),
    PDF("far-file-pdf",List.of("application/pdf")),
    TEXT("far-file-lines", List.of("text/plain", "text/csv")),
    IMAGE("far-file-image", List.of("image/png", "image/jpeg", "image/gif", "image/bmp")),
    AUDIO("far-file-audio", List.of("audio/mpeg", "audio/wav")),
    VIDEO("far-file-video", List.of("video/mp4")),
    UNKNOWN("far-file", Collections.emptyList());
    private final String descriptor;
    private final List<String> allowedMimeTypes;
    MimeType(String descriptor, List<String> mimeTypes) {
        this.descriptor = descriptor;
        this.allowedMimeTypes = mimeTypes;
    }
}
