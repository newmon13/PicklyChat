package dev.jlipka.pickly.controller.components.chat;

import com.gluonhq.richtextarea.RichTextArea;
import dev.jlipka.pickly.controller.components.media.EmojiPickerAreaController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.mfxresources.fonts.IconsProviders;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;
import static dev.jlipka.pickly.controller.utils.IconAccessor.getIcon;
import static dev.jlipka.pickly.controller.utils.IconAccessor.setIcon;
import static java.util.Arrays.*;

@Slf4j
public class MessageInputAreaController {
    public MFXButton mediaButton;
    public MFXButton emojiButton;
    public MFXButton sendButton;
    public VBox messageInputVbox;
    public RichTextArea messageField;
    private File selectedFile;

    private EmojiPickerAreaController emojiController;
    private HBox emojiPickerRoot;

    private final String[] iconDescriptors = new String[] {
            "fas-paperclip",
            "fas-face-laugh-beam",
            "fas-right-long"
    };

    @FXML
    public void initialize() {
        Iterator<String> iterator = stream(iconDescriptors).iterator();
        Stream.of(mediaButton, emojiButton, sendButton).forEach( mfxButton -> {
            MFXFontIcon icon = getIcon(iterator.next(), IconsProviders.FONTAWESOME_SOLID);
            setIcon(mfxButton, icon);
        });
        createEmojiPickerNode();
    }

    public void createEmojiPickerNode() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/dev/jlipka/pickly/view/components/EmojiPickerArea.fxml"));
            emojiPickerRoot = loader.load();
            emojiController = loader.getController();
            emojiController.emojiTabPane.setVisible(false);
            messageInputVbox.getChildren().addFirst(emojiPickerRoot);
        } catch (IOException e) {
            throw   new RuntimeException("Failed to load message input area", e);
        }
    }

    @FXML
    public void openFileChooser(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        Stage stage = (Stage) mediaButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (Objects.nonNull(selectedFile)) {
            log.info("Selected file: {}", selectedFile );
        }
    }

    @FXML
    public void openEmojiPickerNode() {
        emojiController.emojiTabPane.setVisible(true);
    }


    @FXML
    public void sendMessage() {
        TabPane tabPane = (TabPane) sendButton.getScene().lookup("tabPane");
        tabPane.getSelectionModel().getSelectedItem();
    }

//    private void createMessage() {
//        if (!isMessageEmpty()) {
//            Message message = new Message.MessageBuilder("placeholder-sender-id", "placeholder_receiver-id")
//                    .addMessageContent(messageField.getText())
//                    .addSerializableFiles(Objects.nonNull(selectedFile) ? List.of(new SerializableFile(selectedFile)) : Collections.emptyList())
//                    .build();
//        }
//    }

//    private boolean isMessageEmpty() {
//        if (Objects.isNull(messageField.getText()) && Objects.isNull(selectedFile)) {
//            return true;
//        }
//        return false;
//    }
}
