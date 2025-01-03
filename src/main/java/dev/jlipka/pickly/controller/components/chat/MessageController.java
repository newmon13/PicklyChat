package dev.jlipka.pickly.controller.components.chat;

import com.gluonhq.richtextarea.RichTextArea;
import com.gluonhq.richtextarea.model.Document;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
@Slf4j
public class MessageController {
    public static int PADDING = 24;
    @FXML private VBox messageContentContainer;
    @FXML private ImageView profilePicture;
    @FXML private Text userName;
    @FXML private Text statusText;
    private ScheduledExecutorService executor;
    private CompletableFuture<Double> heightCalculation;

    public MessageController() {
        executor = Executors.newScheduledThreadPool(1);
    }


    public void setUserName(String name) {
        userName.setText(name);
    }

    @FXML
    public void setMessage(String message) {
        heightCalculation = new CompletableFuture<>();
        TextArea textArea = new TextArea();
        textArea.getStyleClass().add("message-text");
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setVisible(false);
        messageContentContainer.getChildren().add(textArea);
        setupTextAreaDimensionsChangeEventListener(textArea);
        textArea.setText(message);
        heightCalculation.thenAccept(calculatedHeight -> {
            messageContentContainer.getChildren().remove(textArea);
            Document document = new Document(message);
            RichTextArea richTextArea = new RichTextArea();
            richTextArea.setEditable(false);
            richTextArea.setMinHeight(calculatedHeight);
            richTextArea.setPrefHeight(calculatedHeight);
            richTextArea.setMaxHeight(2 * calculatedHeight);
            richTextArea.getActionFactory().open(document).execute(new ActionEvent());
            richTextArea.getActionFactory().save().execute(new ActionEvent());
            messageContentContainer.getChildren().add(richTextArea);
        });
    }

    private void setupTextAreaDimensionsChangeEventListener(TextArea textArea) {
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            executor.schedule(() -> updateTextAreaHeight(textArea), 10, TimeUnit.MILLISECONDS);
        });

    }

    private void updateTextAreaHeight(TextArea textArea) {
        Platform.runLater(() -> {
            try {
                Text text = (Text) textArea.lookup(".text");
                if (text != null) {
                    double height = calculateRequiredHeight(text);
                    updateTextAreaDimensions(textArea, height);
                    heightCalculation.complete(height);
                } else {
                    heightCalculation.completeExceptionally(
                            new Exception("Failed to locate Text Node inside of TextArea")
                    );
                }
            } catch (Exception e) {
                heightCalculation.completeExceptionally(e);
                log.error("Failed to adjust height of message bubble", e);
            }
        });
    }

    private double calculateRequiredHeight(Text text) {
        return text.boundsInParentProperty().get().getMaxY() + PADDING;
    }

    private void updateTextAreaDimensions(TextArea textArea, double height) {
        textArea.setMinHeight(height);
        textArea.setPrefHeight(height);
        textArea.setMaxHeight(height);
    }


    public void attachFileNode(Node fileNode) {
        messageContentContainer.getChildren().addLast(fileNode);
    }

    public void setProfileImage(Image image) {
        profilePicture.setImage(image);
    }

    public void setStatus(String status) {
        statusText.setText(status);
    }

    public void setMessageType(String type) {
//        messageContent.getStyleClass().addAll("message", type);
    }
}
