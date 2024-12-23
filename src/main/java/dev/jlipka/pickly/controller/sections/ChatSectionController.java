package dev.jlipka.pickly.controller.sections;

import dev.jlipka.pickly.controller.components.ChatTabManager;
import dev.jlipka.pickly.controller.components.MessageInputAreaController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;


@Slf4j
public class ChatSectionController {
    public VBox chatSection;
    public HBox messageInput;
    @FXML
    private TabPane chatTabPane;
    private ChatTabManager chatTabManager;

    @FXML
    public void initialize() {
        loadMessageInputArea();
    }

    private void loadMessageInputArea() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/dev/jlipka/pickly/view/components/MessageInputArea.fxml"));
            loader.setRoot(messageInput);
//            MessageInputAreaController controller = loader.getController();
            loader.load();

        } catch (IOException e) {
            throw new RuntimeException("Failed to load message input area", e);
        }
    }


}
