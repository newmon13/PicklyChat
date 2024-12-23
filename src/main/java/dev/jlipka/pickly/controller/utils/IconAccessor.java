package dev.jlipka.pickly.controller.utils;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.mfxresources.fonts.IconsProviders;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;

public class IconAccessor {
    public static void setIcon(MFXButton button, MFXFontIcon icon) {
        button.setGraphic(icon);
        button.setText("");
    }

    public static MFXFontIcon getIcon(String iconDescriptor, IconsProviders provider) {
        MFXFontIcon icon = new MFXFontIcon();
        icon.setIconsProvider(provider);
        icon.setDescription(iconDescriptor);
        return icon;
    }

}
