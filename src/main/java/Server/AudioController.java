package Server;

import java.io.File;
import java.util.Scanner;

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

        audioPlayer.start();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Выберите действие:\n1 - Следующий трек\n2 - Предыдущий трек\n3 - Старт\n4 - Стоп\n5 - Выход");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    audioPlayer.playNext();
                    break;
                case 2:
                    audioPlayer.playPrevious();
                    break;
                case 3:
                    audioPlayer.start();
                    break;
                case 4:
                    audioPlayer.stop();
                    break;
                case 5:
                    System.out.println("Выход из программы.");
                    System.exit(0);
                default:
                    System.out.println("Неверный выбор. Повторите попытку.");
            }
        }
    }
}
