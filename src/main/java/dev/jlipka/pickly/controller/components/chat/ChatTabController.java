package dev.jlipka.pickly.controller.components.chat;

import com.gluonhq.richtextarea.RichTextArea;
import com.gluonhq.richtextarea.model.DecorationModel;
import com.gluonhq.richtextarea.model.Document;
import com.gluonhq.richtextarea.model.ParagraphDecoration;
import com.gluonhq.richtextarea.model.TextDecoration;
import dev.jlipka.pickly.Message;
import dev.jlipka.pickly.TestClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jdk.incubator.vector.VectorOperators;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
public class ChatTabController {
    @FXML
    private VBox chatContainer;

    private static final String MESSAGE_FXML_PATH = "/dev/jlipka/pickly/view/components/Message.fxml";
    private static final double MESSAGE_WIDTH_RATIO = 2.0;

    public void addMessage(Message message, MessageDirection direction) {

        try {
            HBox messageRow = createMessageRow(direction);
            VBox messageContainer = createMessageContainer(messageRow);
            MessageController controller = loadMessageComponent(messageContainer);

            configureMessageController(controller, message);
            addMessageToDisplay(messageRow, messageContainer);

        } catch (IOException e) {
            log.error("Failed to load message component", e);
        } catch (Exception e) {
            log.error("Unexpected error while displaying message", e);
        }
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

