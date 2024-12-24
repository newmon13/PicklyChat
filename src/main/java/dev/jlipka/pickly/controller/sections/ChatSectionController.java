package dev.jlipka.pickly.controller.sections;

import dev.jlipka.pickly.controller.components.chat.ChatTabPaneController;
import dev.jlipka.pickly.controller.components.chat.MessageInputAreaController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


@Slf4j
public class ChatSectionController {
    public VBox chatSection;
    public HBox messageInput;

    private ChatTabPaneController chatTabPaneController;
    private MessageInputAreaController messageInputAreaController;

    @FXML
    public void initialize() {
        loadMessageInputArea();
        loadChatTabPane();
        chatTabPaneController.setMessageInput(messageInputAreaController);
    }

    private void loadMessageInputArea() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/dev/jlipka/pickly/view/components/MessageInputArea.fxml"));
            loader.setRoot(messageInput);
            loader.load();
            messageInputAreaController = loader.getController();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load message input area", e);
        }
    }

    private void loadChatTabPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/dev/jlipka/pickly/view/components/ChatTabPane.fxml"));
            TabPane tabPane = loader.load();
            int offset = chatSection.getChildren().indexOf(messageInput);
            chatSection.getChildren().add(offset, tabPane);
            chatTabPaneController = loader.getController();
            System.out.println("ChatTabPaneController loaded: " + chatTabPaneController);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load chat tab pane", e);
        }
    }
}

