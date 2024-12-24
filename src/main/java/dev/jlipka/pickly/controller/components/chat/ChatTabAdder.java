package dev.jlipka.pickly.controller.components.chat;

import dev.jlipka.pickly.model.User;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class ChatTabAdder {
    private final Scene scene;

    public ChatTabAdder(Scene scene) {
        this.scene = scene;
    }

    public void createChat(User data) {
        if (!existsAlready(data.getName())) {
            TabPane tabPane = (TabPane) scene.lookup("#tabPane");
            ChatTabPaneController chatTabController = (ChatTabPaneController) tabPane.getUserData();
            chatTabController.addTab(data.getName());
        } else {
            log.error("Chat already exists");
        }
    }

    private boolean existsAlready(String name) {
        Node lookup = scene.lookup("#" + name);
        return !Objects.isNull(lookup);
    }
}
