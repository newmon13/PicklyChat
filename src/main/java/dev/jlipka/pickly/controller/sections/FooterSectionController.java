package dev.jlipka.pickly.controller.sections;

import io.github.palexdev.mfxresources.fonts.IconsProviders;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import static dev.jlipka.pickly.controller.utils.IconAccessor.getIcon;

public class FooterSectionController {
    @FXML
    private HBox footerSocials;

    @FXML
    public void initialize() {
        System.out.println("Footer section created");
        MFXFontIcon githubIcon = getIcon("fab-github", IconsProviders.FONTAWESOME_BRANDS);
        githubIcon.setSize(24);
        MFXFontIcon linkedinIcon = getIcon("fab-linkedin", IconsProviders.FONTAWESOME_BRANDS);
        linkedinIcon.setSize(24);
        footerSocials.getChildren().addAll(githubIcon, linkedinIcon);
    }

}
