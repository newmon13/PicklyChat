package dev.jlipka.pickly.controller.sections;

import io.github.palexdev.materialfx.controls.MFXPasswordField;

public class HeaderSectionController {

    public MFXPasswordField addressField;
    public MFXPasswordField portField;

    public HeaderSectionController() {
        System.out.println("Header created");
    }
}
