package core;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.nio.file.Paths;

/**
 * Manages all sounds playing in the game.
 *
 * kinda spotty sometimes, but we can add more as we go.
 */
public final class SoundManager {
    private static double volume = 1d;

    private SoundManager() { }

    private static void playSound(String audioPath, int duration) {
        MediaPlayer mediaPlayer =
                new MediaPlayer(new Media(Paths.get(audioPath).toUri().toString()));

        mediaPlayer.setRate(1.5d);
        mediaPlayer.setVolume(volume);
        mediaPlayer.play();
        mediaPlayer.setStopTime(new Duration(duration));
    }

    public static void playDoorCreak() {
        playSound("src/main/resources/audio/jail_cell_door.mp3", 1000);
    }

    public static void playPlayerAttacked() {
        playSound("src/main/resources/audio/lose sound 2 - 2.wav", 1000);
    }

    public static void playEnemyKilled() {
        playSound("src/main/resources/audio/positive 1.mp3", 1000);
    }

    public static void playCoinOrKeyCollected() {
        playSound("src/main/resources/audio/Coin-collect-sound-effect.mp3", 1000);
    }

    public static void playPotionCollected() {
        playSound("src/main/resources/audio/healspell3.aif", 1000);
    }

    public static void playKeyActivated() {
        playSound("src/main/resources/audio/key2 pickup.mp3", 1000);
    }

    public static void playVictory() {
        playSound("src/main/resources/audio/Old victory sound roblox.mp3", 3000);
    }
}
