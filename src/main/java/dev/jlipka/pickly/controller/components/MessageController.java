package dev.jlipka.pickly.controller.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

@Getter
@Slf4j
public class MessageController extends VBox {
    @FXML private ImageView profilePicture;
    @FXML private Text userName;
    @FXML private TextArea messageContent;
    @FXML private Text statusText;

    public MessageController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dev/jlipka/pickly/view/components/Message.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
            String cssPath = "/dev/jlipka/pickly/styles/Message.css";
            URL cssResource = getClass().getResource(cssPath);
            getStylesheets().add(Objects.requireNonNull(cssResource).toExternalForm());
        } catch (IOException e) {
            log.error("Failed to load Message.fxml", e);
            throw new RuntimeException(e);
        }
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
        getStyleClass().add(type);
        messageContent.getStyleClass().addAll("message", type);
    }

    @FXML
    private void initialize() {
        messageContent.setEditable(false);
        messageContent.setFocusTraversable(false);
        messageContent.setWrapText(true);
    }
}