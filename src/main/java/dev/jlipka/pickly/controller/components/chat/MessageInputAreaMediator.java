package dev.jlipka.pickly.controller.components.chat;

import dev.jlipka.pickly.controller.components.media.EmojiPickerAreaController;
import javafx.scene.control.Tab;
import lombok.Setter;

import java.util.Objects;


public class MessageInputAreaMediator {
    private MessageInputAreaMediator instance;
    @Setter
    private ChatTabPaneController chatTabPaneController;
    private MessageInputAreaController messageInputAreaController;
    private EmojiPickerAreaController emojiPickerAreaController;


    private MessageInputAreaMediator(){}

    public MessageInputAreaMediator getInstance() {
        if (Objects.isNull(instance)) instance = new MessageInputAreaMediator();
        return instance;
    }



    public ChatTabController getSelectedTabController() {
        String chatTabName = getSelectedTab().getText();
        return chatTabPaneController.getChatTabs().get(chatTabName);
    }

    private Tab getSelectedTab() {
        return chatTabPaneController.getTabPane().getSelectionModel().getSelectedItem();
    }
}
