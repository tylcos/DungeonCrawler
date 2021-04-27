package core;

import game.entities.Player;
import javafx.scene.media.AudioClip;

import java.nio.file.Paths;

/**
 * Manages all sounds playing in the game.
 *
 * kinda spotty sometimes, but we can add more as we go.
 */
public final class SoundManager {
    private static double  volume  = .1d;
    private static boolean soundOn = true;

    private SoundManager() { }

    private static void playSound(String audioPath) {
        playSound(audioPath, 1, 1d);
    }

    private static void playSound(String audioPath, double rate, double volumeAdjust) {
        if (!soundOn) {
            return;
        }

        AudioClip clip = new AudioClip(Paths.get(audioPath).toUri().toString());

        clip.setRate(rate);
        clip.setVolume(Player.getPlayer().isDead() ? volumeAdjust * volume / 2d
                                                   : volumeAdjust * volume);
        clip.play();
    }

    public static void playSound(boolean soundOn) {
        SoundManager.soundOn = soundOn;
    }

    public static void playDoorCreak() {
        playSound("src/main/resources/audio/jail_cell_door.mp3");
    }

    public static void playPlayerAttacked() {
        playSound("src/main/resources/audio/lose sound 2 - 2.wav");
    }

    public static void playEnemyKilled() {
        playSound("src/main/resources/audio/positive 1.mp3");
    }

    public static void playCoinOrKeyCollected() {
        playSound("src/main/resources/audio/Coin-collect-sound-effect.mp3");
    }

    public static void playPotionCollected() {
        playSound("src/main/resources/audio/healspell3.aif");
    }

    public static void playKeyActivated() {
        playSound("src/main/resources/audio/key2 pickup.mp3");
    }

    public static void playVictory() {
        playSound("src/main/resources/audio/Old victory sound roblox.mp3");
    }

    public static void playBeam() {
        playSound("src/main/resources/audio/GolemBeamNoise.mp3", 1d, .5d);
    }

    public static void playDeath() {
        playSound("src/main/resources/audio/Win sound.wav", 1, 10d);
    }

    public static void playNuke() {
        playSound("src/main/resources/audio/Nuke.mp3");
    }
}
