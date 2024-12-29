package dev.jlipka.pickly.controller.components.chat;

import com.gluonhq.richtextarea.RichTextArea;
import dev.jlipka.pickly.Message;
import dev.jlipka.pickly.SerializableFile;
import dev.jlipka.pickly.controller.components.media.EmojiPickerAreaController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.mfxresources.fonts.IconsProviders;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Setter;
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
    private static HBox emojiPickerRoot;

    @Setter
    private ChatTabPaneController chatTabPaneController;


    @FXML
    public void initialize() {
        Iterator<MessageInputIconsDescriptors> iterator = stream(MessageInputIconsDescriptors.values()).iterator();
        Stream.of(mediaButton, emojiButton, sendButton).forEach( mfxButton -> {
            MFXFontIcon icon = getIcon(iterator.next().getDescriptor(), IconsProviders.FONTAWESOME_SOLID);
            setIcon(mfxButton, icon);
        });
        createEmojiPickerNode();
    }

    public void createEmojiPickerNode() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/dev/jlipka/pickly/view/components/EmojiPickerArea.fxml"));
            emojiPickerRoot = loader.load();
            emojiPickerRoot.setVisible(false);
            emojiPickerRoot.setManaged(false);
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
        toggleEmojiTabPane();
    }

    @FXML
    public void sendMessage() {
        messageField.getActionFactory().save().execute(new ActionEvent());
        Platform.runLater(() ->{
            Optional<Message> message = createMessage();
            ChatTabController controller = chatTabPaneController.getSelectedTabController();
            message.ifPresent(value -> controller.addMessage(value, MessageDirection.SENDER));
        });
    }

    private Optional<Message> createMessage() {
        if (!isMessageEmpty()) {
            System.out.println("Message not empty");
            Message message = new Message.MessageBuilder("placeholder-sender-id", "placeholder_receiver-id")
                    .addMessageContent(messageField.getDocument().getText())
                    .addSerializableFiles(Objects.nonNull(selectedFile) ? List.of(new SerializableFile(selectedFile)) : Collections.emptyList())
                    .build();
            return Optional.ofNullable(message);
        }
        return Optional.empty();
    }

    private boolean isMessageEmpty() {
        return messageField.getDocument().getText().isEmpty() && Objects.isNull(selectedFile);
    }

    public static void toggleEmojiTabPane() {
        emojiPickerRoot.setManaged(!emojiPickerRoot.isManaged());
        emojiPickerRoot.setVisible(!emojiPickerRoot.isVisible());
    }
}
