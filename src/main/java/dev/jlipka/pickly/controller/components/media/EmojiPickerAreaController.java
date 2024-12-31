package dev.jlipka.pickly.controller.components.media;

import com.gluonhq.emoji.Emoji;
import com.gluonhq.emoji.EmojiData;
import com.gluonhq.emoji.util.EmojiImageUtils;
import com.gluonhq.emoji.util.TextUtils;
import com.gluonhq.richtextarea.RichTextArea;
import dev.jlipka.pickly.controller.components.chat.ChatControllersMediator;
import dev.jlipka.pickly.controller.components.chat.MessageInputAreaController;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class EmojiPickerAreaController {
    public TabPane emojiTabPane;
    public HBox emojiPickerRoot;
    private final ChatControllersMediator mediator;

    public EmojiPickerAreaController() {
        mediator = ChatControllersMediator.getInstance();
        mediator.setEmojiPickerAreaController(this);
    }

    @FXML
    public void initialize() {
        populateCategoryTabs();
    }

    private void populateCategoryTabs() {
        String text = getAllCategoriesAsUnicodeString();
        EmojiCatalog[] values = EmojiCatalog.values();
        Iterator<EmojiCatalog> iterator = Arrays.stream(values).iterator();
        List<Node> categoriesNodes = processEmojis(text);
        Platform.runLater(() -> {
            categoriesNodes.forEach(categoryNode -> {
                MFXButton categoryButton = new MFXButton();
                categoryButton.setText("");
                categoryButton.setGraphic(categoryNode);
                Tab emojiTab = createEmojiTab(iterator.next());
                emojiTab.setClosable(false);
                emojiTab.setGraphic(categoryNode);
                emojiTabPane.getTabs().add(emojiTab);
            });
        });
    }

    public Tab createEmojiTab(EmojiCatalog category) {
        Tab tab = new Tab();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setMinHeight(120);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        FlowPane flowPane = new FlowPane();
        flowPane.maxWidth(Double.MAX_VALUE);
        flowPane.maxHeight(Double.MAX_VALUE);
        List<Node> emojiNodes = processEmojis(getCategoryEmojisAsUnicodeString(category));
        addCategoryEmojis(emojiNodes, flowPane);
        scrollPane.setContent(flowPane);
        tab.setContent(scrollPane);
        return tab;
    }

    private void addCategoryEmojis(List<Node> emojiNodes, Pane parent) {
        RichTextArea richTextAreaHook = (RichTextArea) emojiTabPane.getScene().lookup("#messageField");
            emojiNodes.forEach(node -> {
                MFXButton emojiButton = new MFXButton();
                emojiButton.setOnMouseClicked(mouseEvent -> {
                    Emoji emoji = (Emoji) node.getUserData();
                    richTextAreaHook.getActionFactory().insertEmoji(emoji).execute(new ActionEvent());
                    mediator.toggleEmojiPaneTabPane();
                });
                emojiButton.setText("");
                emojiButton.setGraphic(node);
                parent.getChildren().add(emojiButton);
            });
    }

    private List<Node> processEmojis(String unicodeText) {
        List<Node> emojiNodes = TextUtils.convertToTextAndImageNodes(unicodeText);
        emojiNodes.parallelStream()
                .filter(ImageView.class::isInstance)
                .forEach(node -> {
                    String unified = (String) node.getProperties().get(EmojiImageUtils.IMAGE_VIEW_EMOJI_PROPERTY);
                    EmojiData.emojiFromCodepoints(unified).ifPresent(emoji -> {
                        Tooltip.install(node, new Tooltip("Emoji: " + emoji.getName() + "\n" + unified));
                        node.setUserData(emoji);
                    });
                });
        return emojiNodes;
    }

    private String getCategoryEmojisAsUnicodeString(EmojiCatalog category) {
        return EmojiData.getEmojiCollection()
                .parallelStream()
                .filter(emoji -> emoji.getCategory().equals(category.getCategoryName()))
                .map(Emoji::character)
                .collect(Collectors.joining());
    }

    private String getAllCategoriesAsUnicodeString() {
        return Arrays.stream(EmojiCatalog.values())
                .map(EmojiCatalog::getRepresentative)
                .collect(Collectors.joining());
    }

    public void toggleEmojiPaneTabPane() {
        emojiPickerRoot.setVisible(!emojiPickerRoot.isVisible());
        emojiPickerRoot.setManaged(!emojiPickerRoot.isManaged());
    }
}
