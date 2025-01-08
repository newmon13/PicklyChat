package dev.jlipka.pickly;

import dev.jlipka.pickly.controller.scenes.MainSceneController;
import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        UserAgentBuilder.builder()
                .themes(JavaFXThemes.MODENA)
                .themes(MaterialFXStylesheets.forAssemble(true))
                .setDeploy(true)
                .setResolveAssets(true)
                .build()
                .setGlobal();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/scenes/MainScene.fxml"));
//        MainSceneController controller = loader.getController();
//        controller.setClient(new TestClient());

        Scene scene = new Scene(loader.load(), 800, 600);
        stage.setTitle("Pickly");
        InputStream iconStream = getClass().getResourceAsStream("static/favicon.png");
        stage.getIcons().add(new Image(Objects.requireNonNull(iconStream)));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}