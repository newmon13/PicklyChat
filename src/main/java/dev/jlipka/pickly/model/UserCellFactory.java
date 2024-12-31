package dev.jlipka.pickly.model;

import dev.jlipka.pickly.controller.components.chat.ChatControllersMediator;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.cell.MFXListCell;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.util.Objects;

@Slf4j
public class UserCellFactory extends MFXListCell<User> {
    private final ChatControllersMediator mediator;

        public UserCellFactory(MFXListView<User> listView, User data) {
            super(listView, data);
            setAvatar(data);
            mediator = ChatControllersMediator.getInstance();
            this.setOnMouseClicked( mouseEvent -> {
                mediator.createChat(data);
            });
        }
        @Override
        protected void render(User data) {
            super.render(data);
        }

        private void setAvatar(User data) {
            if (Objects.isNull(data.getAvatar())) {
                MFXFontIcon userIcon = new MFXFontIcon("fas-user", 18);
                userIcon.getStyleClass().add("user-icon");
                getChildren().addFirst(userIcon);
            } else {
                byte[] imageData = data.getAvatar();
                ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
                ImageView avatarView = new ImageView(new Image(bis));
                avatarView.setFitHeight(18);
                avatarView.setFitWidth(18);
                getChildren().addFirst(avatarView);
            }
        }
    }