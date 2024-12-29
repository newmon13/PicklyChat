package dev.jlipka.pickly.controller.components.chat;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
public class ChatTabPaneController {
    @FXML
    private TabPane tabPane;
    private final Map<String, ChatTabController> chatTabs ;
    @Setter
    private MessageInputAreaController messageInput;


    public ChatTabPaneController() {
        chatTabs = new HashMap<>();
    }

    @FXML
    public void initialize() {
        tabPane.setUserData(this);
        Platform.runLater(() -> {
            messageInput.setChatTabPaneController(this);
        });
    }


    public ChatTabController getSelectedTabController() {
        String chatTabName = getSelectedTab().getText();
        return chatTabs.get(chatTabName);
    }

    private Tab getSelectedTab() {
        return tabPane.getSelectionModel().getSelectedItem();
    }


    public void addTab(String name) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dev/jlipka/pickly/view/components/ChatTab.fxml"));
            Tab tab = new Tab();
            loader.setRoot(tab);
            loader.load();

            tab.setText(name);
            tab.setId(name);

            ChatTabController controller = loader.getController();
            controller.setTabName(name);

            chatTabs.put(name, controller);
            tabPane.getTabs().add(tab);
            tabPane.getTabs().forEach(item -> {
                System.out.println(item.getText());
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ChatTabController getTabController(String name) {
        return chatTabs.get(name);
    }

    public void deleteTab(String name) {
        chatTabs.remove(name);
        log.info("Tab deleted");
    }
}