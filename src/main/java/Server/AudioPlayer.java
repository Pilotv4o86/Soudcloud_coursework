package Server;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioPlayer {

    private AudioPlayerThread playerThread;
    private List<File> playlist = new ArrayList<>();
    private File currentFile;
    private ExecutorService executorService;
    private int currentIndex = -1;


    public AudioPlayer() {
        executorService = Executors.newSingleThreadExecutor();
    }

    public void addToPlaylist(File file) {
        playlist.add(file);
    }


    // play
    // Обновленный метод play
    public void play() {
        if (playerThread == null) {
            if (!playlist.isEmpty()) {
                if (currentIndex == -1) {
                    currentIndex = 0;
                    currentFile = playlist.get(currentIndex);
                }
                playerThread = new AudioPlayerThread(currentFile);
                executorService.submit(playerThread);
            } else {
                System.out.println("Очередь воспроизведения пуста.");
            }
        } else {
            System.out.println("Воспроизведение уже запущено.");
        }
    }


    // playNext
    public void playNext() {
        if (!playlist.isEmpty()) {
            stopPlayback();
            currentIndex = (currentIndex + 1) % playlist.size();
            currentFile = playlist.get(currentIndex);
            playFile(currentFile);
        } else {
            System.out.println("Очередь воспроизведения пуста.");
        }
    }

    // playPrevious
    public void playPrevious() {
        if (!playlist.isEmpty()) {
            stopPlayback();
            currentIndex = (currentIndex - 1 + playlist.size()) % playlist.size();
            currentFile = playlist.get(currentIndex);
            playFile(currentFile);
        } else {
            System.out.println("Очередь воспроизведения пуста.");
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

    public List<File> getPlaylist()
    {
        return playlist;
    }
}
