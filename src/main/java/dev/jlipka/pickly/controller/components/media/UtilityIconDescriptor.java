package dev.jlipka.pickly.controller.components.media;

import lombok.Getter;

@Getter
public enum UtilityIconDescriptor {
    DOWNLOAD("fas-file-arrow-down");
    private final String descriptor;
    UtilityIconDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }
}
