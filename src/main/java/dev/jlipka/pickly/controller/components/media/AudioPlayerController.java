package dev.jlipka.pickly.controller.components.media;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXSlider;
import io.github.palexdev.mfxresources.fonts.IconsProviders;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static dev.jlipka.pickly.controller.utils.IconAccessor.getIcon;
import static dev.jlipka.pickly.controller.utils.IconAccessor.setIcon;

public class AudioPlayerController {
    public HBox audioPlayerHBox;
    public HBox audioPlayerControls;
    public MFXSlider audioSlider;
    private final List<MFXButton> mfxButtons;
    private int mfxButtonsCurrentIndex;

    private MediaPlayer mediaPlayer;
    private boolean isDragging = false;

    public AudioPlayerController() {
        mfxButtons = new ArrayList<>();
        mfxButtonsCurrentIndex = 0;
    }

    public void loadMedia(File file) {
        Media media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        setupMediaPlayer();
    }

    @FXML
    public void initialize() {
        loadControls();
        setupSlider();
    }

    public void loadControls() {
        mfxButtons.clear();

        for (AudioPlayerIconDescriptor descriptor : AudioPlayerIconDescriptor.values()) {
            MFXFontIcon icon = getIcon(descriptor.getDescriptor(), IconsProviders.FONTAWESOME_SOLID);
            MFXButton button = new MFXButton();
            setIcon(button, icon);
            mfxButtons.add(button);
        }

        updateSliderThumb();
    }

    private void setupSlider() {
        audioSlider.setOnMousePressed(event -> {
            isDragging = true;
        });

        audioSlider.setOnMouseReleased(event -> {
            isDragging = false;
            if (mediaPlayer != null) {
                double duration = mediaPlayer.getTotalDuration().toSeconds();
                double seekTime = duration * (audioSlider.getValue() / 100);
                mediaPlayer.seek(Duration.seconds(seekTime));
            }
        });

        if (mediaPlayer != null) {
            mediaPlayer.currentTimeProperty().addListener((obs, oldVal, newVal) -> {
                if (!isDragging) {
                    double duration = mediaPlayer.getTotalDuration().toSeconds();
                    double current = newVal.toSeconds();
                    audioSlider.setValue((current / duration) * 100);
                }
            });
        }
    }

    private void setupMediaPlayer() {
        mediaPlayer.setOnReady(() -> {
            audioSlider.setValue(0);
        });

        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.stop();
            audioSlider.setValue(0);
            mfxButtonsCurrentIndex = 0;
            updateSliderThumb();
        });
    }

    private void toggleAudioButton() {
        mfxButtonsCurrentIndex = (mfxButtonsCurrentIndex + 1) % mfxButtons.size();
        updateSliderThumb();

        if (mediaPlayer != null) {
            if (mfxButtonsCurrentIndex == 0) {
                mediaPlayer.play();
            } else {
                mediaPlayer.pause();
            }
        }
    }

    private void updateSliderThumb() {
        audioSlider.setThumbSupplier(() -> mfxButtons.get(mfxButtonsCurrentIndex));
    }

    public void dispose() {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }
    }
}
