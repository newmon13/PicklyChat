package dev.jlipka.pickly.controller.components.media;

import lombok.Getter;

@Getter
public enum AudioPlayerIconDescriptor {
    PLAY("fas-circle-play"),
    PAUSE("fas-circle-pause");
    private final String descriptor;

    AudioPlayerIconDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }
}
