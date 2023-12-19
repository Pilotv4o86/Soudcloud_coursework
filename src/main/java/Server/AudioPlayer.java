package Server;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioPlayer {

    private AudioPlayerThread playerThread;
    private List<File> playlist = new ArrayList<>();
    private File currentFile;
    private ExecutorService executorService;
    private int currentIndex = -1;
    private int i = 0;
    private DefaultListModel<File> playlistModel; // Новое поле для модели плейлиста


    public AudioPlayer(DefaultListModel<File> playlistModel) {
        this.playlistModel = playlistModel;
        executorService = Executors.newSingleThreadExecutor();
    }

    public void addToPlaylist(File file) {
        playlist.add(file);
        if (playerThread == null) {
            currentFile = playlist.get(i++);
        }
        playlistModel.addElement(file); // Добавление трека в модель списка
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
