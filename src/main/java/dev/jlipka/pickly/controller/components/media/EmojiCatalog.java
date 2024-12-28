package dev.jlipka.pickly.controller.components.media;

import lombok.Getter;

@Getter
public enum EmojiCatalog {
    ACTIVITIES("Activities", "⚽"),
    EMOTIONS("Smileys & Emotion", "😊"),
    SYMBOLS("Symbols", "⭐"),
    PEOPLE("People & Body", "👥"),
    OBJECTS("Objects", "🎒"),
    TRAVELLING("Travel & Places", "✈️"),
    FLAGS("Flags", "🎌"),
    COMPONENT("Component", "🔧"),
    FOOD("Food & Drink", "🍽️"),
    NATURE("Animals & Nature", "🦋");

    private final String categoryName;
    private final String representative;

    EmojiCatalog(String categoryName, String representative) {
        this.categoryName = categoryName;
        this.representative = representative;
    }

}