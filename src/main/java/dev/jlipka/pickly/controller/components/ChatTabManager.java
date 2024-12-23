package dev.jlipka.pickly.controller.components;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.extern.slf4j.Slf4j;
import java.net.URL;
import java.util.*;

@Slf4j
public class ChatTabManager {
    MessageInputAreaController messageInput;
    Map<String, ChatTabController> chatTabs;
    TabPane tabPane;

    public ChatTabManager(TabPane tabPane) {
        chatTabs = new HashMap<>();
        this.tabPane = tabPane;
    }

    public void addTab(String name) {
        ChatTabController controller = new ChatTabController();
        controller.setText(name);
        chatTabs.put(name, controller);
        tabPane.getTabs().add(controller);
        log.info("Tab added");
    }

    public void deleteTab(String name) {
        chatTabs.remove(name);
        log.info("Tab deleted");
    }

}
