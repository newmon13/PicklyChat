package dev.jlipka.pickly.controller.components.chat;

import com.gluonhq.richtextarea.RichTextArea;
import dev.jlipka.pickly.Message;
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
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;
import static dev.jlipka.pickly.controller.utils.IconAccessor.getIcon;
import static dev.jlipka.pickly.controller.utils.IconAccessor.setIcon;
import static java.util.Arrays.*;

@Slf4j
public class MessageInputAreaController {
    private static final String MESSAGE_INPUT_FXML = "/dev/jlipka/pickly/view/components/EmojiPickerArea.fxml";
    private static final String PICKED_FILES_FXML = "/dev/jlipka/pickly/view/media/PickedFilesArea.fxml";
    public MFXButton mediaButton;
    public MFXButton emojiButton;
    public MFXButton sendButton;
    public VBox messageInputVbox;
    public RichTextArea messageField;
    public HBox messageInputHBox;
    private final ChatControllersMediator mediator;

    public MessageInputAreaController() {
        mediator = ChatControllersMediator.getInstance();
        mediator.setMessageInputAreaController(this);
    }

    @FXML
    public void initialize() {
        Iterator<MessageInputIconsDescriptors> iterator = stream(MessageInputIconsDescriptors.values()).iterator();
        Stream.of(mediaButton, emojiButton, sendButton).forEach( mfxButton -> {
            MFXFontIcon icon = getIcon(iterator.next().getDescriptor(), IconsProviders.FONTAWESOME_SOLID);
            setIcon(mfxButton, icon);
        });
        createEmojiPickerNode();
        createPickedFilesNode();
    }

    public void createEmojiPickerNode() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource(MESSAGE_INPUT_FXML));
            messageInputVbox.getChildren().addFirst(loader.load());
        } catch (IOException e) {
            throw  new RuntimeException("Failed to load message input area", e);
        }
    }

    public void createPickedFilesNode() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource(PICKED_FILES_FXML));
            messageInputVbox.getChildren().add(1, loader.load());
        } catch (IOException e) {
            throw  new RuntimeException("Failed to load message input area", e);
        }
    }

    @FXML
    public void openFileChooser(MouseEvent event){
        FileChooser fileChooser = new FileChooser();
        Stage stage = (Stage) mediaButton.getScene().getWindow();
        mediator.addFile(fileChooser.showOpenDialog(stage));
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
            message.ifPresent(value -> mediator.send(message.get()));
        });
    }

    private Optional<Message> createMessage() {
        if (!isMessageEmpty()) {
            System.out.println("Message not empty");
            Message message = new Message.MessageBuilder("placeholder-sender-id", "placeholder_receiver-id")
                    .addMessageContent(messageField.getDocument().getText())
//                    .addSerializableFiles(Objects.nonNull(selectedFile) ? List.of(new SerializableFile(selectedFile)) : Collections.emptyList())
                    .build();
            return Optional.ofNullable(message);
        }
        return Optional.empty();
    }

    private boolean isMessageEmpty() {
        return messageField.getDocument().getText().isEmpty() && mediator.getSelectedFiles().isEmpty();
    }

    public void toggleEmojiTabPane() {
        mediator.toggleEmojiPaneTabPane();
    }
}
