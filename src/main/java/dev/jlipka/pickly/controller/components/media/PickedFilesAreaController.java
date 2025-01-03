package dev.jlipka.pickly.controller.components.media;

import dev.jlipka.pickly.controller.components.chat.ChatControllersMediator;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.mfxresources.fonts.IconsProviders;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.collections.*;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import static dev.jlipka.pickly.controller.utils.IconAccessor.getIcon;
import static dev.jlipka.pickly.controller.utils.IconAccessor.setIcon;

@Slf4j
public class PickedFilesAreaController {
    public HBox pickedFilesRoot;
    public TabPane fileTabPane;
    @Getter
    private final ObservableMap<File, MimeType> pickedFiles;
    private final ChatControllersMediator mediator;

    public PickedFilesAreaController() {
        this.mediator = ChatControllersMediator.getInstance();
        this.mediator.setPickedFilesAreaController(this);
        this.pickedFiles = FXCollections.observableHashMap();
        initializeFileListener();
    }

    private void initializeFileListener() {
        pickedFiles.addListener((MapChangeListener<? super File, ? super MimeType>) change -> {
            if (change.wasAdded()) {
                addFileNode(change.getKey(), change.getValueAdded());
            } else if (change.wasRemoved()) {
                deleteFileNode(change.getKey());
            }
        });
    }

    public void addFile(File file) {
        try {
            MimeType mime = FileTypeResolver.getMIME(file);
            if (mime == null) {
                throw new IllegalArgumentException("Unsupported file type");
            }
            pickedFiles.put(file, mime);
        } catch (Exception e) {
            log.error("Failed to add file: {}", file.getName(), e);
            throw new RuntimeException("Failed to process file", e);
        }
    }

    private void addFileNode(File file, MimeType mime) {
        MFXButton button = createFileButton();
        MFXFontIcon icon = createFileIcon(mime);
        setIcon(button, icon);

        Tab tab = createFileTab(file, button);
        setupTabListeners(tab, file, button);
        fileTabPane.getTabs().add(tab);
    }

    private MFXButton createFileButton() {
        MFXButton button = new MFXButton();
        button.setStyle("-fx-background-radius: 16; -fx-background-color: linear-gradient(to bottom, #99d19c, #73ab84);");
        return button;
    }

    private MFXFontIcon createFileIcon(MimeType mime) {
        MFXFontIcon icon = getIcon(mime.getDescriptor(), IconsProviders.FONTAWESOME_REGULAR);
        icon.setStyle("-fx-background-color: linear-gradient(to bottom, #99d19c, #73ab84);");
        return icon;
    }

    private Tab createFileTab(File file, MFXButton button) {
        Tab tab = new Tab();
        tab.setStyle("-fx-background-color: linear-gradient(to bottom, #99d19c, #73ab84);");
        tab.setGraphic(button);
        tab.setText(file.getName());
        return tab;
    }

    private void setupTabListeners(Tab tab, File file, MFXButton button) {
        button.setOnMouseClicked(event -> fileTabPane.getSelectionModel().select(tab));
        tab.setOnClosed(event -> pickedFiles.remove(file));
    }

    private void deleteFileNode(File file) {
        fileTabPane.getTabs().removeIf(tab -> tab.getText().equals(file.getName()));
    }

    public void deleteFile(File file) {
        pickedFiles.remove(file);
    }

    public void clearFiles() {
        pickedFiles.entrySet().removeIf(entry -> true);
    }

    public void togglePickedFilesTabPane() {
        pickedFilesRoot.setVisible(!pickedFilesRoot.isVisible());
        pickedFilesRoot.setManaged(!pickedFilesRoot.isManaged());
    }
}