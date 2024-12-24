package dev.jlipka.pickly.model;

import dev.jlipka.pickly.controller.components.chat.ChatTabAdder;
import dev.jlipka.pickly.controller.components.chat.ChatTabPaneController;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.util.Objects;

@Slf4j
public class UserCellFactory extends MFXListCell<User> {
        private MFXFontIcon userIcon;
        private ImageView avatarView;

        public UserCellFactory(MFXListView<User> listView, User data) {
            super(listView, data);

            this.setOnMouseClicked( mouseEvent -> {
                log.info("clicked on: {}", data.getName());
                ChatTabAdder adder = new ChatTabAdder(getScene());
                adder.createChat(data);
            });

            if (Objects.isNull(data.getAvatar())) {
                userIcon = new MFXFontIcon("fas-user", 18);
                userIcon.getStyleClass().add("user-icon");
                getChildren().addFirst(userIcon);
            } else {
                byte[] imageData = data.getAvatar();
                ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
                avatarView = new ImageView(new Image(bis));
                avatarView.setFitHeight(18);
                avatarView.setFitWidth(18);
                getChildren().addFirst(avatarView);
            }
        }
        @Override
        protected void render(User data) {
            super.render(data);
        }
    }