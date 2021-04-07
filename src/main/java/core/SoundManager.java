package core;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public final class SoundManager { //kinda spotty sometimes, but we can add more as we go


    public static void playDoorCreak() {

        MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File("src/main/resources/audio/jail_cell_door.mp3").toURI().toString()));
        mediaPlayer.setRate(1.5);
        mediaPlayer.play();
        mediaPlayer.setStopTime(new Duration(1500));
    }

    public static void playPlayerAttacked() {
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File("src/main/resources/audio/lose sound 2 - 2.wav").toURI().toString()));
        mediaPlayer.setRate(1.5);
        mediaPlayer.setStopTime(new Duration(1000));
        mediaPlayer.play();
    }
    public static void playEnemyKilled() {
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File("src/main/resources/audio/positive 1.mp3").toURI().toString()));
        mediaPlayer.setRate(1.5);
        mediaPlayer.setStopTime(new Duration(1000));
        mediaPlayer.play();
    }
    public static void playCoinOrKeyCollected() {
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File("src/main/resources/audio/Coin-collect-sound-effect.mp3").toURI().toString()));
        mediaPlayer.setRate(1.5);
        mediaPlayer.setStopTime(new Duration(1000));
        mediaPlayer.play();
    }
    public static void playPotionCollected() {
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File("src/main/resources/audio/healspell3.aif").toURI().toString()));
        mediaPlayer.setRate(1.5);
        mediaPlayer.setStopTime(new Duration(1000));
        mediaPlayer.play();
    }
    public static void playKeyActivated() {
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File("src/main/resources/audio/key2 pickup.mp3").toURI().toString()));
        mediaPlayer.setRate(1.5);
        mediaPlayer.setStopTime(new Duration(1000));
        mediaPlayer.play();
    }
    public static void playVictory() {
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(new File("src/main/resources/audio/Old victory sound roblox.mp3").toURI().toString()));
        mediaPlayer.setRate(1.5);
        mediaPlayer.setStopTime(new Duration(3000));
        mediaPlayer.play();
    }


}
