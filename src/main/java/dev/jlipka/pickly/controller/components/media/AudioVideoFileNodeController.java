package dev.jlipka.pickly.controller.components.media;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class AudioVideoFileNodeController {
    public MediaView mediaControl;

    public void setMedia(Media media) {
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(false);
        mediaControl.setMediaPlayer(mediaPlayer);
    }

    public void downloadFile() {
        //TODO downloading file and spawning new node in some kind of user's files
    }
}
