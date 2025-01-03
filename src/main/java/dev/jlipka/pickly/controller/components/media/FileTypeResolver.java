package dev.jlipka.pickly.controller.components.media;

import dev.jlipka.pickly.SerializableFile;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.mfxresources.fonts.IconsProviders;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import javafx.scene.control.OverrunStyle;


import static dev.jlipka.pickly.controller.utils.IconAccessor.getIcon;
import static dev.jlipka.pickly.controller.utils.IconAccessor.setIcon;

@Slf4j
public class FileTypeResolver {
    public static MimeType getMIME(File file) {
        try {
            String detectedMimeType = Files.probeContentType(Path.of(file.getPath()));
            for (MimeType mimeType: MimeType.values()) {
                if (mimeType.getAllowedMimeTypes().contains(detectedMimeType)) {
                    return mimeType;
                }
            }
            return MimeType.UNKNOWN;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Node getFileTypeDisplayNode(File file) throws IOException {
        MimeType mimeType = getMIME(file);
        HBox fileRow = new HBox();
        fileRow.setStyle("-fx-background-color: #A6D974; -fx-border-color: grey;");
        fileRow.setPadding(new Insets(4));
        fileRow.setSpacing(4);
        fileRow.setAlignment(Pos.CENTER_LEFT);

        MFXFontIcon fileIcon = getIcon(mimeType.getDescriptor(), IconsProviders.FONTAWESOME_REGULAR);

        Label fileName = new Label(file.getName());
        fileName.setMaxWidth(200);
        fileName.setTextOverrun(OverrunStyle.ELLIPSIS);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        MFXButton button = new MFXButton();
        button.setStyle("-fx-background-color: transparent");

        button.setOnMouseClicked(mouseEvent -> {
            SerializableFile serializableFile = new SerializableFile(file);
            downloadFile(serializableFile);
        });

        MFXFontIcon icon = getIcon(UtilityIconDescriptor.DOWNLOAD.getDescriptor(), IconsProviders.FONTAWESOME_SOLID);
        setIcon(button, icon);

        fileRow.getChildren().addAll(
                fileIcon,
                fileName,
                spacer,
                button
        );

        return fileRow;
    }

    private static void downloadFile(SerializableFile sourceFile) {
        Path downloadPath = Path.of(System.getProperty("user.home"), "Downloads", sourceFile.getName());
        try {
            log.info("File size: {} bytes", sourceFile.length());
            Files.createDirectories(downloadPath);
            Path finalPath = downloadPath.resolve(sourceFile.getFileName());
            Files.write(finalPath, sourceFile.getFileContent(), StandardOpenOption.CREATE_NEW);
            log.info("File successfully downloaded to: {}", finalPath);
        }catch (FileAlreadyExistsException e) {
            log.error("File already exists");
        } catch (IOException e) {
            log.error("Failed to download a file: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while downloading a file: {}", e.getMessage(), e);
        }
    }


    public static void main(String[] args) throws IOException {
        Files.createFile(Path.of(System.getProperty("user.home") + "/Downloads" + "/test.txt"));
    }
}
