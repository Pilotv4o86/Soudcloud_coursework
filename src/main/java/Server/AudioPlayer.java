package Server;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class AudioPlayer {

    private AdvancedPlayer player;
    private Queue<File> playlist = new LinkedList<>();

    public void addToPlaylist(File file) {
        playlist.add(file);
    }

    public void playNext() {
        if (!playlist.isEmpty()) {
            File nextFile = playlist.poll();
            play(nextFile);
        } else {
            System.out.println("Очередь воспроизведения пуста.");
        }
    }

    private void play(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            Bitstream bitstream = new Bitstream(fileInputStream);
            int duration = bitstream.readFrame().calculate_framesize();

            this.player = new AdvancedPlayer(fileInputStream);

            this.player.setPlayBackListener(new PlaybackListener() {
                @Override
                public void playbackFinished(PlaybackEvent evt) {
                    System.out.println("Воспроизведение завершено: " + file.getName());
                    playNext();
                }
            });

            System.out.println("Воспроизведение: " + file.getName());
            player.play();

        } catch (JavaLayerException | IOException e) {
            e.printStackTrace();
        }
    }
}
