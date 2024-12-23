package dev.jlipka.pickly.controller.components;

import dev.jlipka.pickly.Message;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatTabController extends Tab{
    @FXML private VBox chatContainer;

    @FXML
    public void initialize() {
        Message message = new Message.MessageBuilder("adasdasd", "asdasdasdas")
                .addMessageContent("siema")
                .build();
        addMessage(new MessageController(message), MessageDirection.SENDER);
    }

    public void addMessage(MessageController messageController, MessageDirection direction) {
        HBox messageRow = new HBox();
        messageRow.getStyleClass().add("message-row");
        messageRow.setAlignment(setMessagePosition(direction));
        messageRow.getChildren().add(messageController);
        chatContainer.getChildren().add(messageRow);
    }

    private Pos setMessagePosition(MessageDirection direction) {
        return switch (direction) {
            case SENDER -> Pos.CENTER_RIGHT;
            case RECEIVER -> Pos.CENTER_LEFT;
        };
    }

    public void clearMessages() {
        chatContainer.getChildren().clear();
    }
}