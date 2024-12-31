package dev.jlipka.pickly.controller.components.chat;

import dev.jlipka.pickly.Message;
import dev.jlipka.pickly.controller.components.media.EmojiPickerAreaController;
import dev.jlipka.pickly.model.User;
import lombok.Setter;
import java.util.Objects;


public class ChatControllersMediator {
    private static ChatControllersMediator instance;

    @Setter
    private ChatTabPaneController chatTabPaneController;
    @Setter
    private MessageInputAreaController messageInputAreaController;
    @Setter
    private EmojiPickerAreaController emojiPickerAreaController;

    private ChatControllersMediator(){}

    public void send(Message message) {
        ChatTabController selectedTabController = chatTabPaneController.getSelectedTabController();
        selectedTabController.addMessage(message, MessageDirection.SENDER);
    }

    public void receive(Message message) {
        ChatTabController selectedTabController = chatTabPaneController.getSelectedTabController();
        selectedTabController.addMessage(message, MessageDirection.RECEIVER);
    }

    public void createChat(User user) {
        chatTabPaneController.createTab(user.getName());
    }

    public void deleteChat(User user) {
        chatTabPaneController.deleteTab(user.getName());
    }

    public void toggleEmojiPaneTabPane() {
        emojiPickerAreaController.toggleEmojiPaneTabPane();
    }

    public static ChatControllersMediator getInstance() {
        if (Objects.isNull(instance)) instance = new ChatControllersMediator();
        return instance;
    }
}
