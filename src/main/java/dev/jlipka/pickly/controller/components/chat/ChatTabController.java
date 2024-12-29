package dev.jlipka.pickly.controller.components.chat;

import dev.jlipka.pickly.Message;
import dev.jlipka.pickly.TestClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jdk.incubator.vector.VectorOperators;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChatTabController {
    @Setter
    private String tabName;
    @FXML private VBox chatContainer;


    public void addMessage(Message message, MessageDirection direction) {
        try {
            HBox messageRow = new HBox();
            messageRow.setAlignment(
                    direction.equals(MessageDirection.SENDER) ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT
            );
            VBox messageContainer = new VBox();
            messageContainer.maxWidthProperty().bind(messageRow.widthProperty().divide(2));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dev/jlipka/pickly/view/components/Message.fxml"));
            loader.setRoot(messageContainer);
            loader.load();

            MessageController controller = loader.getController();
            controller.setMessage(message.getMessage());
            controller.setUserName(message.getSenderId());
            controller.setStatus("Sent");
            controller.setMessageType("default");

            messageRow.getChildren().add(messageContainer);
            chatContainer.getChildren().add(messageRow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearMessages() {
        chatContainer.getChildren().clear();
    }
}
