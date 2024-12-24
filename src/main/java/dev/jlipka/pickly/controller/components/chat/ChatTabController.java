package dev.jlipka.pickly.controller.components.chat;

import dev.jlipka.pickly.Message;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatTabController {
    @Setter
    private String tabName;
    @FXML private VBox chatContainer;

    public ChatTabController() {

    }

    @FXML
    public void initialize() {

    }

    public void addMessage(Message message, MessageDirection direction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dev/jlipka/pickly/view/components/Message.fxml"));
            loader.setRoot(chatContainer);
            loader.load();
            MessageController controller = loader.getController();
            controller.setMessage(message.getMessage());
            controller.setUserName(message.getSenderId());
            controller.setStatus("Sent");
            controller.setMessageType("default");


            chatContainer.setAlignment(direction == MessageDirection.SENDER ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearMessages() {
        chatContainer.getChildren().clear();
    }
}
