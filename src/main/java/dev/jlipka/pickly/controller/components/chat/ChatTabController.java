package dev.jlipka.pickly.controller.components.chat;

import dev.jlipka.pickly.Message;
import dev.jlipka.pickly.SerializableFile;
import dev.jlipka.pickly.controller.components.media.FileTypeResolver;
import dev.jlipka.pickly.controller.components.media.MimeType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
public class ChatTabController {
    @FXML
    private VBox chatContainer;
    private static final String MESSAGE_FXML_PATH = "/dev/jlipka/pickly/view/components/Message.fxml";
    private static final double MESSAGE_WIDTH_RATIO = 2.0;
    private ChatControllersMediator mediator;

    public ChatTabController() {
        mediator = ChatControllersMediator.getInstance();
    }

    public void addMessage(Message message, MessageDirection direction) {
        try {
            HBox messageRow = createMessageRow(direction);
            VBox messageContainer = createMessageContainer(messageRow);
            MessageController controller = loadMessageComponent(messageContainer);

            configureMessageController(controller, message);
            addMessageToDisplay(messageRow, messageContainer);
            attachFiles(message, controller);
            mediator.clearSelectedFiles();

        } catch (IOException e) {
            log.error("Failed to load message component", e);
            throw new RuntimeException("Message component loading failed", e);
        }
    }

    private void attachFiles(Message message, MessageController controller) {
        if (mediator.getSelectedFiles().isEmpty()) return;

        List<SerializableFile> files = mediator.getSelectedFiles().stream()
                .map(file -> {
                    try {
                        controller.attachFileNode(FileTypeResolver.getFileTypeDisplayNode(file));
                        return new SerializableFile(file);
                    } catch (IOException e) {
                        log.error("Failed to attach file: {}", file.getName(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        message.setFiles(files);
    }

    private HBox createMessageRow(MessageDirection direction) {
        HBox messageRow = new HBox();
        messageRow.setAlignment(getMessageAlignment(direction));
        return messageRow;
    }

    private Pos getMessageAlignment(MessageDirection direction) {
        return direction.equals(MessageDirection.SENDER) ?
                Pos.CENTER_RIGHT : Pos.CENTER_LEFT;
    }

    private VBox createMessageContainer(HBox messageRow) {
        VBox messageContainer = new VBox();
        messageContainer.maxWidthProperty().bind(
                messageRow.widthProperty().divide(MESSAGE_WIDTH_RATIO)
        );
        return messageContainer;
    }

    private MessageController loadMessageComponent(VBox messageContainer) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MESSAGE_FXML_PATH));
        loader.setRoot(messageContainer);
        loader.load();
        return loader.getController();
    }

    private void configureMessageController(MessageController controller, Message message) {
        controller.setMessage(message.getMessage());
        controller.setUserName(message.getSenderId());
        controller.setStatus("Sent");
        controller.setMessageType("default");
    }

    private void addMessageToDisplay(HBox messageRow, VBox messageContainer) {
        messageRow.getChildren().add(messageContainer);
        chatContainer.getChildren().add(messageRow);
    }


    public void clearMessages() {
        chatContainer.getChildren().clear();
    }
}

