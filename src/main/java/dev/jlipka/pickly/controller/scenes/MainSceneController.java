package dev.jlipka.pickly.controller.scenes;

import dev.jlipka.pickly.controller.sections.ChatSectionController;
import dev.jlipka.pickly.controller.sections.FooterSectionController;
import dev.jlipka.pickly.controller.sections.HeaderSectionController;
import dev.jlipka.pickly.controller.sections.NavSectionController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainSceneController {
    public HBox headerSection;
    public VBox mainSection;
    public VBox navSection;
    public HBox footerSection;

    @FXML
    public void initialize() {
        initializeHeader();
        initializeChatSection();
        initializeNavSection();
        initializeFooterSection();
    }

    public void initializeHeader() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dev/jlipka/pickly/view/sections/HeaderSection.fxml"));
        loader.setRoot(headerSection);
        HeaderSectionController controller = loader.getController();
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private  void initializeChatSection() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/dev/jlipka/pickly/view/sections/ChatSection.fxml"));
            loader.setRoot(mainSection);
            ChatSectionController controller = loader.getController();
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load chat section", e);
        }
    }

    public void initializeNavSection() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dev/jlipka/pickly/view/sections/NavSection.fxml"));
        loader.setRoot(navSection);
        NavSectionController controller = loader.getController();
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initializeFooterSection() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dev/jlipka/pickly/view/sections/FooterSection.fxml"));
        loader.setRoot(footerSection);
        FooterSectionController controller = loader.getController();
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
