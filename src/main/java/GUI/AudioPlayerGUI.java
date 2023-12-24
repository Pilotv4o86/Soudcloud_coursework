package GUI;

import DB.DataBaseManager;
import Server.AudioPlayer;

import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AudioPlayerGUI {

    private AudioPlayer audioPlayer;
    private JButton startStopButton;
    private JButton nextButton;
    private JButton prevButton;
    private JButton addTrackButton;
    private JSlider progressBar;
    private JLabel timeLabel;
    private JList<File> playlistList; // Добавлен JList для отображения плейлиста
    private DefaultListModel<File> playlistModel; // Модель для JList


    public AudioPlayerGUI() {
        playlistModel = new DefaultListModel<>();
        audioPlayer = new AudioPlayer(playlistModel);
        createAndShowGUI();

        // Получаем треки из базы данных и добавляем их в playlistModel
        new DataBaseManager().getListofTracks(playlistModel);
    }



    private void createAndShowGUI() {
        JFrame frame = new JFrame("Audio Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();

        startStopButton = new JButton("Start");
        startStopButton.addActionListener(e -> handleStartStopButton());

        nextButton = new JButton("Next");
        nextButton.addActionListener(e -> audioPlayer.playNext());

        prevButton = new JButton("Previous");
        prevButton.addActionListener(e -> audioPlayer.playPrevious());

        addTrackButton = new JButton("Add Track");
        addTrackButton.addActionListener(e -> handleAddTrackButton());

        progressBar = new JSlider();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setValue(0);
        progressBar.addChangeListener(e -> {
            if (!progressBar.getValueIsAdjusting()) {
                int value = progressBar.getValue();
                // audioPlayer.seek(value);
            }
        });

        timeLabel = new JLabel("0:00");

        playlistModel = new DefaultListModel<>();
        playlistList = new JList<>(playlistModel);
        JScrollPane playlistScrollPane = new JScrollPane(playlistList);
        playlistScrollPane.setPreferredSize(new Dimension(200, 150));

        panel.add(playlistScrollPane);

        // Обновите часть создания кнопок в createAndShowGUI
        JButton searchButton = new JButton("Поиск");
        searchButton.addActionListener(e -> handleSearch());
        panel.add(searchButton);


        panel.add(startStopButton);
        panel.add(prevButton);
        panel.add(nextButton);
        panel.add(addTrackButton);
        panel.add(progressBar);
        panel.add(timeLabel);


        frame.getContentPane().add(panel);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void handleStartStopButton() {
        if (audioPlayer.isPlaying()) {
            audioPlayer.stop();
            startStopButton.setText("Start");
        } else {
            audioPlayer.play();
            startStopButton.setText("Stop");
        }
    }

    private void handleAddTrackButton() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            audioPlayer.addToPlaylist(selectedFile);
            playlistModel.addElement(selectedFile); // Добавление трека в модель списка
            JOptionPane.showMessageDialog(null, "Track added to the playlist: " + selectedFile.getName());
        }
    }

    private void handleSearch() {
        // Диалоговое окно для ввода названия трека
        String searchTerm = JOptionPane.showInputDialog(null, "Введите название трека для поиска:");
        List<File> matchingTracks = searchTracksByName(searchTerm);

        if (searchTerm == null && searchTerm.isEmpty() && matchingTracks.isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Совпадений не найдено.");
        }
        else
        {
            for (File track : matchingTracks)
            {
                if (audioPlayer.getPlaylist().contains(track))
                {
                    audioPlayer.playFile(track);
                    break;
                }
            }
        }
    }



    private List<File> searchTracksByName(String searchTerm) {
        List<File> matchingTracks = new ArrayList<>();

        // Проход по плейлисту и добавление соответствующих треков
        for (File track : audioPlayer.getPlaylist()) {
            if (track.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                matchingTracks.add(track);
            }
        }

        return matchingTracks;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AudioPlayerGUI::new);
    }
}
