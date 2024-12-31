package dev.jlipka.pickly.controller.components.chat;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Getter
public class ChatTabPaneController {
    private static final String CHAT_TAB_FXML = "/dev/jlipka/pickly/view/components/ChatTab.fxml";

    @FXML
    private TabPane tabPane;

    private final Map<String, ChatTabController> chatTabs;

    @Setter
    private ChatControllersMediator mediator;

    public ChatTabPaneController() {
        mediator = ChatControllersMediator.getInstance();
        mediator.setChatTabPaneController(this);
        this.chatTabs = new ConcurrentHashMap<>();
    }

    @FXML
    public void initialize() {
        if (mediator == null) {
            throw new IllegalStateException("Mediator must be set before initialization");
        }
        mediator.setChatTabPaneController(this);
//        initializeTabChangeListener();
    }

    public ChatTabController getSelectedTabController() {
        Tab selectedTab = getSelectedTab();
        if (selectedTab == null) {
            log.error("No tab is currently selected");
        }

        String chatTabName = selectedTab.getText();
        ChatTabController controller = chatTabs.get(chatTabName);
        if (controller == null) {
            throw new IllegalStateException("No controller found for tab: " + chatTabName);
        }

        return controller;
    }

    private Tab getSelectedTab() {
        return tabPane.getSelectionModel().getSelectedItem();
    }

    public void createTab(String name) {
        validateTabName(name);

        if (existsAlready(name)) {
            log.warn("Attempted to create duplicate tab: {}", name);
        }

        try {
            Tab tab = createTabComponent(name);
            ChatTabController controller = loadTabController(tab);

            chatTabs.put(name, controller);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);

        } catch (IOException e) {
            log.error("Failed to load ChatTab.fxml for tab: {}", name, e);
        }
    }

    public void deleteTab(String name) {
        validateTabName(name);

        if (!existsAlready(name)) {
            log.warn("Attempted to delete non-existent tab: {}", name);
        }

        Tab tabToRemove = findTabByName(name);
        if (tabToRemove != null) {
            tabPane.getTabs().remove(tabToRemove);
            chatTabs.remove(name);
            log.debug("Successfully deleted tab: {}", name);
        }
    }

    private void validateTabName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tab name cannot be null or empty");
        }
    }

    private boolean existsAlready(String name) {
        return chatTabs.containsKey(name);
    }

    private Tab createTabComponent(String name) {
        Tab tab = new Tab();
        tab.setText(name);
        tab.setClosable(true);

        tab.setOnCloseRequest(event -> {
            Platform.runLater(() -> {
                deleteTab(name);
            });
        });

        return tab;
    }

    private ChatTabController loadTabController(Tab tab) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(CHAT_TAB_FXML));
        loader.setRoot(tab);
        loader.load();
        return loader.getController();
    }

    private Tab findTabByName(String name) {
        return tabPane.getTabs().stream()
                .filter(tab -> tab.getText().equals(name))
                .findFirst()
                .orElse(null);
    }

//    private void initializeTabChangeListener() {
//        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
//            if (newTab != null) {
//                log.debug("Tab selected: {}", newTab.getText());
//            }
//        });
//    }
}
