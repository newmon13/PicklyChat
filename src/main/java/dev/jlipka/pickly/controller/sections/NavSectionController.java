package dev.jlipka.pickly.controller.sections;

import dev.jlipka.pickly.model.User;
import dev.jlipka.pickly.model.UserCellFactory;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxresources.fonts.IconsProviders;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;
import static dev.jlipka.pickly.controller.utils.IconAccessor.getIcon;
import static dev.jlipka.pickly.controller.utils.IconAccessor.setIcon;
import static dev.jlipka.pickly.model.UserRepository.users;

@Slf4j
public class NavSectionController {
    public MFXButton favButton;
    public MFXButton searchButton;
    public MFXButton notificationButton;
    public MFXButton settingsButton;
    public MFXListView<User> customList;
    private final String[] iconDescriptions = new String[]{
            "fas-heart",
            "fas-magnifying-glass",
            "fas-bell",
            "fas-gear",
    };

    StringConverter<User> converter = FunctionalStringConverter.to(
            user -> (user == null) ? "" : user.getName() + " " + user.getSurname());


    public NavSectionController() {
        System.out.println("Nav section created");
    }


    @FXML
    public void initialize() {
        loadIcons();
        loadUsers();
    }

    private void loadIcons() {
        Iterator<String> iterator = Arrays.stream(iconDescriptions).iterator();
        Stream.of(favButton, searchButton, notificationButton, searchButton)
                .forEach( mfxButton -> {
                    MFXFontIcon icon = getIcon(iterator.next(), IconsProviders.FONTAWESOME_SOLID);
                    setIcon(mfxButton, icon);
                });
    }

    private void loadUsers() {
        customList.setItems(users);
        customList.setCellFactory(user -> new UserCellFactory(customList, user));
        customList.features().enableBounceEffect();
        customList.features().enableSmoothScrolling(0.5);
        customList.setConverter(converter);
    }
}
