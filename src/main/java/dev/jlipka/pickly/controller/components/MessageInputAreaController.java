package dev.jlipka.pickly.controller.components;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxresources.fonts.IconDescriptor;
import io.github.palexdev.mfxresources.fonts.IconsProviders;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

import static dev.jlipka.pickly.controller.utils.IconAccessor.getIcon;
import static dev.jlipka.pickly.controller.utils.IconAccessor.setIcon;

@Slf4j
public class MessageInputAreaController {
    public MFXButton mediaButton;
    public MFXButton emojiButton;
    public MFXButton sendButton;
    public TextArea messageField;

    private final String[] iconDescriptors = new String[]{
            "fas-paperclip",
            "fas-face-laugh-beam",
            "fas-right-long"
    };

    @FXML
    public void initialize() {
        Iterator<String> iterator = Arrays.stream(iconDescriptors).iterator();
        Stream.of(mediaButton, emojiButton, sendButton).forEach( mfxButton -> {
            MFXFontIcon icon = getIcon(iterator.next(), IconsProviders.FONTAWESOME_SOLID);
            setIcon(mfxButton, icon);
        });
    }
}
