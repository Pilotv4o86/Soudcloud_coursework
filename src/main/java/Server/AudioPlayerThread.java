package Server;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AudioPlayerThread extends Thread {

    private File file;
    private boolean playing;
    private boolean stopRequested; // новый флаг
    private int currentPosition;
    private AdvancedPlayer player;

    public AudioPlayerThread(File file) {
        this.file = file;
    }

    @Override
    public void run() {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            Bitstream bitstream = new Bitstream(fileInputStream);
            long duration = bitstream.readFrame().calculate_framesize();

            player = new AdvancedPlayer(fileInputStream);

            player.setPlayBackListener(new PlaybackListener() {
                @Override
                public void playbackFinished(PlaybackEvent evt) {
                    System.out.println("Воспроизведение завершено: " + file.getName());
                    playing = false;
                }
            });

            System.out.println("Воспроизведение: " + file.getName());
            playing = true;
            stopRequested = false;

            while (!stopRequested && playing) {
                player.play(0, Integer.MAX_VALUE); // начинаем воспроизведение с начала трека
                // Добавим паузу между повторными воспроизведениями, чтобы не блокировать поток
                Thread.sleep(100);
            }

        } catch (JavaLayerException | IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
                player.close();
        }
    }

    public void stopPlayback() {
        stopRequested = true;
        playing = false;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setCurrentPosition(int position) {
        this.currentPosition = position;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }
}
