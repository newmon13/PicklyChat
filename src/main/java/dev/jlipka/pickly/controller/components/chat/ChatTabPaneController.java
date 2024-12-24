package dev.jlipka.pickly.controller.components.chat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ChatTabPaneController {
    @FXML
    private TabPane tabPane;
    private final Map<String, ChatTabController> chatTabs ;
    @Setter
    private MessageInputAreaController messageInput;


    public ChatTabPaneController() {
        chatTabs = new HashMap<>();
    }

    public void addTab(String name) {
        try {
            System.out.println("DODAWANIE TABA");
            Tab tab = new Tab();
            tab.setText("eeee");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dev/jlipka/pickly/view/components/ChatTab.fxml"));
            loader.setRoot(tab);
            loader.load();

            ChatTabController controller = loader.getController();
            controller.setTabName(name);

//            Message message = new Message.MessageBuilder("321", "123")
//                    .addMessageContent("Test message content")
//                    .build();
//
//            controller.addMessage(message, MessageDirection.SENDER);
            chatTabs.put(name, controller);

            tabPane.getTabs().add(tab);
            tabPane.applyCss();
            tabPane.layout();


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