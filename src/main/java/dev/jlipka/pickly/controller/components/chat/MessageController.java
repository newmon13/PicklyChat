package dev.jlipka.pickly.controller.components.chat;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class MessageController {
    @FXML private ImageView profilePicture;
    @FXML private Text userName;
    @FXML private TextArea messageContent;
    @FXML private Text statusText;

    public MessageController() {
    }

    public void setUserName(String name) {
        userName.setText(name);
    }

    public void setMessage(String message) {
        messageContent.setText(message);
    }

    public void setProfileImage(Image image) {
        profilePicture.setImage(image);
    }

    public void setStatus(String status) {
        statusText.setText(status);
    }

    public void setMessageType(String type) {
        messageContent.getStyleClass().addAll("message", type);
    }

    @FXML
    private void initialize() {
        messageContent.setEditable(false);
        messageContent.setFocusTraversable(false);
        messageContent.setWrapText(true);
    }
}
