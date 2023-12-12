package Server;

import java.io.File;

public class AudioController {

    public static void main(String[] args) {
        String directoryPath = "F:\\Labs\\Repos\\SoundCloud_coursework\\src\\main\\resources\\audio";
        playMusicFromDirectory(directoryPath);
    }

    public static void playMusicFromDirectory(String directoryPath) {
        File directory = new File(directoryPath);

        if (!directory.isDirectory()) {
            System.out.println("Указанный путь не является каталогом.");
            return;
        }

        File[] files = directory.listFiles();

        if (files == null || files.length == 0) {
            System.out.println("Каталог пуст.");
            return;
        }

        AudioPlayer audioPlayer = new AudioPlayer();

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".mp3")) {
                audioPlayer.addToPlaylist(file);
            }
        }

        audioPlayer.playNext();
    }}
