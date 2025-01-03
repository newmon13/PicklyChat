package dev.jlipka.pickly.controller.components.chat;

import dev.jlipka.pickly.Message;
import dev.jlipka.pickly.controller.components.media.EmojiPickerAreaController;
import dev.jlipka.pickly.controller.components.media.PickedFilesAreaController;
import dev.jlipka.pickly.model.User;
import lombok.Setter;

import java.io.File;
import java.util.Objects;
import java.util.Set;

public class ChatControllersMediator {
    private static ChatControllersMediator instance;
    @Setter
    private ChatTabPaneController chatTabPaneController;
    @Setter
    private MessageInputAreaController messageInputAreaController;
    @Setter
    private EmojiPickerAreaController emojiPickerAreaController;
    @Setter
    private PickedFilesAreaController pickedFilesAreaController;

    private ChatControllersMediator(){}

    public static ChatControllersMediator getInstance() {
        if (Objects.isNull(instance)) instance = new ChatControllersMediator();
        return instance;
    }

    public void send(Message message) {
        ChatTabController selectedTabController = chatTabPaneController.getSelectedTabController();
        selectedTabController.addMessage(message, MessageDirection.SENDER);
    }

    public void receive(Message message) {
        ChatTabController selectedTabController = chatTabPaneController.getSelectedTabController();
        selectedTabController.addMessage(message, MessageDirection.RECEIVER);
    }

    public void addFile(File file) {
        pickedFilesAreaController.addFile(file);
    }

    public void deleteFile(File file) {
        pickedFilesAreaController.deleteFile(file);
    }

    public Set<File> getSelectedFiles() {
        return pickedFilesAreaController.getPickedFiles().keySet();
    }

    public void clearSelectedFiles() {
        pickedFilesAreaController.clearFiles();
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

    public void togglePickedFilesTabPane() {
        pickedFilesAreaController.togglePickedFilesTabPane();
    }
}
