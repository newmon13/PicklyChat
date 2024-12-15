package dev.jlipka.pickly.controller.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ChatTabController extends Tab {  // Changed to extend Tab
    @FXML private VBox messageContainer;

    public ChatTabController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dev/jlipka/pickly/view/components/ChatTab.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            log.error("Failed to load ChatTab.fxml", e);
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {
        // initialization code here
    }

    public void addMessage(MessageController messageController, MessageDirection direction) {
        HBox messageRow = new HBox();
        messageRow.getStyleClass().add("message-row");
        messageRow.setAlignment(setMessagePosition(direction));
        messageRow.getChildren().add(messageController);
        messageContainer.getChildren().add(messageRow);
    }

    private Pos setMessagePosition(MessageDirection direction) {
        return switch (direction) {
            case SENDER -> Pos.CENTER_RIGHT;
            case RECEIVER -> Pos.CENTER_LEFT;
        };
    }

    public void clearMessages() {
        messageContainer.getChildren().clear();
    }
}
