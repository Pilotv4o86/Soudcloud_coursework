package Server;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioPlayer {

    private AudioPlayerThread playerThread;
    private Queue<File> playlist = new LinkedList<>();
    private File currentFile;
    private ExecutorService executorService;

    public AudioPlayer() {
        executorService = Executors.newSingleThreadExecutor();
    }

    public void addToPlaylist(File file) {
        playlist.add(file);
    }

    public void play() {
        if (!playlist.isEmpty()) {
            currentFile = playlist.poll();
            playFile(currentFile);
        } else {
            System.out.println("Очередь воспроизведения пуста.");
        }
    }

    public void playNext() {
        if (!playlist.isEmpty()) {
            stopPlayback();
            currentFile = playlist.poll();
            playFile(currentFile);
        } else {
            System.out.println("Очередь воспроизведения пуста.");
        }
    }

    public void playPrevious() {
        if (currentFile != null) {
            playlist.add(currentFile);
            stopPlayback();
            if (!playlist.isEmpty()) {
                currentFile = playlist.poll();
                playFile(currentFile);
            } else {
                System.out.println("Очередь воспроизведения пуста.");
            }
        }
    }

    public void start() {
        if (playerThread == null)
        {
            currentFile = playlist.poll();
            if (currentFile != null) {
                playerThread = new AudioPlayerThread(currentFile);
                executorService.submit(playerThread);
            } else {
                System.out.println("Очередь воспроизведения пуста.");
            }
        } else {
            System.out.println("Воспроизведение уже запущено.");
        }
    }

    public void stop() {
        stopPlayback();
        System.out.println("Воспроизведение остановлено.");
    }

    public boolean isPlaying() {
        return playerThread != null && playerThread.isPlaying();
    }

    private void playFile(File file) {
        if (file != null) {
            playerThread = new AudioPlayerThread(file);
            executorService.submit(playerThread);
        } else {
            System.out.println("Не удалось воспроизвести трек.");
        }
    }

    private void stopPlayback() {
        if (playerThread != null) {
            playerThread.stopPlayback();
            playerThread = null;
        }
    }
}
