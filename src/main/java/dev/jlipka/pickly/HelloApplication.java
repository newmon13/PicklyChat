package dev.jlipka.pickly;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("view/scenes/chat-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Pickly");
        stage.setScene(scene);
        InputStream iconStream = getClass().getResourceAsStream("static/favicon.png");
        stage.getIcons().add(new Image(Objects.requireNonNull(iconStream)));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}