package dev.jlipka.pickly.controller;

import dev.jlipka.pickly.controller.components.ChatTabController;
import dev.jlipka.pickly.controller.components.MessageController;
import dev.jlipka.pickly.controller.components.MessageDirection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ChatController {
    @FXML private TabPane hereWillBeChatContainer;  // Fixed variable name to match FXML

    @FXML
    public void initialize() {
        try {
            ChatTabController chatTab = new ChatTabController();
            chatTab.setText("Chat 1");

            MessageController message = new MessageController();
            message.setMessage("siema");
            message.setUserName("User1");

            chatTab.addMessage(message, MessageDirection.SENDER);
            hereWillBeChatContainer.getTabs().add(chatTab);
        } catch (Exception e) {
            log.error("Failed to load chat components", e);
            throw new RuntimeException(e);
        }
    }
}
