package dev.jlipka.pickly.controller.sections;

import dev.jlipka.pickly.model.Person;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import io.github.palexdev.mfxresources.fonts.IconsProviders;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.fxml.FXML;
import javafx.util.StringConverter;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

import static dev.jlipka.pickly.controller.utils.IconAccessor.getIcon;
import static dev.jlipka.pickly.controller.utils.IconAccessor.setIcon;
import static dev.jlipka.pickly.model.Model.people;

public class NavSectionController {
    public MFXButton favButton;
    public MFXButton searchButton;
    public MFXButton notificationButton;
    public MFXButton settingsButton;
    public MFXListView<Person> customList;
    private final String[] iconDescriptions = new String[]{
            "fas-heart",
            "fas-magnifying-glass",
            "fas-bell",
            "fas-gear",
    };

    StringConverter<Person> converter = FunctionalStringConverter.to(
            person -> (person == null) ? "" : person.getName() + " " + person.getSurname());


    public NavSectionController() {
        System.out.println("Nav section created");
    }


    @FXML
    public void initialize() {
        Iterator<String> iterator = Arrays.stream(iconDescriptions).iterator();
        Stream.of(favButton, searchButton, notificationButton, searchButton)
                .forEach( mfxButton -> {
                    MFXFontIcon icon = getIcon(iterator.next(), IconsProviders.FONTAWESOME_SOLID);
                    setIcon(mfxButton, icon);
                });
        customList.setItems(people);
        customList.setCellFactory(person -> new PersonCellFactory(customList, person));
        customList.features().enableBounceEffect();
        customList.features().enableSmoothScrolling(0.5);
        customList.setConverter(converter);
    }

    private static class PersonCellFactory extends MFXListCell<Person> {
        private final MFXFontIcon userIcon;

        public PersonCellFactory(MFXListView<Person> listView, Person data) {
            super(listView, data);

            userIcon = new MFXFontIcon("fas-user", 18);
            userIcon.getStyleClass().add("user-icon");
            render(data);
        }

        @Override
        protected void render(Person data) {
            super.render(data);
            if (userIcon != null) getChildren().add(0, userIcon);
        }
    }
}
