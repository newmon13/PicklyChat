package dev.jlipka.pickly.controller.components.chat;

import lombok.Getter;

@Getter
public enum MessageInputIconsDescriptors {
    MEDIA("fas-paperclip"),
    EMOJI("fas-face-laugh-beam"),
    SEND("fas-right-long");

    private final String descriptor;
    MessageInputIconsDescriptors(String descriptor) {
        this.descriptor = descriptor;
    }
}
