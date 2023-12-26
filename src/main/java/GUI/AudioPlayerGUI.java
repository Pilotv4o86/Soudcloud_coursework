package GUI;

import Server.AudioPlayer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.Month;

public class AudioPlayerGUI {

    private AudioPlayer audioPlayer;
    private JButton startStopButton;
    private JButton nextButton;
    private JButton prevButton;
    private JButton addTrackButton;
    private JList<File> playlistList;
    private DefaultListModel<File> playlistModel;

    public AudioPlayerGUI() {
        DefaultListModel<File> playlistModel = new DefaultListModel<>();
        audioPlayer = new AudioPlayer(playlistModel);
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Audio Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Используем кастомный класс DynamicBackgroundPanel
        DynamicBackgroundPanel panel = new DynamicBackgroundPanel();
        panel.setLayout(new FlowLayout());

        frame.setVisible(true);

        startStopButton = new JButton("<html>&#9658;</html>"); // Кнопка start/stop
        startStopButton.setFont(new Font("Arial", Font.PLAIN, 16));
        startStopButton.addActionListener(e -> handleStartStopButton());

        nextButton = new JButton("<html>&#9197;</html>"); // Кнопка next
        nextButton.addActionListener(e -> audioPlayer.playNext());

        prevButton = new JButton("<html>&#9198;</html>"); // Кнопка prev
        prevButton.addActionListener(e -> audioPlayer.playPrevious());

        addTrackButton = new JButton("<html>&#10010;&#9836;</html>"); // Кнопка add track
        addTrackButton.addActionListener(e -> handleAddTrackButton());

        playlistModel = new DefaultListModel<>();
        playlistList = new JList<>(playlistModel);
        JScrollPane playlistScrollPane = new JScrollPane(playlistList);
        playlistScrollPane.setBounds(100, 200, 400, 100);

        JButton searchButton = new JButton("\uD83D\uDD0E"); // Кнопка поиска
        searchButton.addActionListener(e -> handleSearch());
        panel.add(searchButton);

        panel.add(prevButton);
        panel.add(startStopButton);
        panel.add(nextButton);
        panel.add(addTrackButton);
        panel.add(playlistScrollPane);

        frame.getContentPane().add(panel);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
    }

    private void handleStartStopButton() {
        if (audioPlayer.isPlaying()) {
            audioPlayer.stop();
            startStopButton.setText("<html>&#9658;</html>"); // Кнопка start
        } else {
            audioPlayer.play();
            startStopButton.setText("<html>&#9209;</html>"); // Кнопка stop
        }
    }

    private void handleAddTrackButton() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            audioPlayer.addToPlaylist(selectedFile);
            playlistModel.addElement(selectedFile);
            JOptionPane.showMessageDialog(null, "Track added to the playlist: " + selectedFile.getName());
        }
    }

    private void handleSearch() {
        String searchTerm = JOptionPane.showInputDialog(null, "Введите название трека для поиска:");

        if (searchTerm != null && !searchTerm.isEmpty()) {
            java.util.List<File> matchingTracks = searchTracksByName(searchTerm);

            if (matchingTracks.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Совпадений не найдено.");
            } else {
                for (File track : matchingTracks) {
                    System.out.println("Найден совпадающий трек: " + track.getName());
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Неверный поисковый запрос.");
        }
    }

    private java.util.List<File> searchTracksByName(String searchTerm) {
        java.util.List<File> matchingTracks = new java.util.ArrayList<>();

        for (File track : audioPlayer.getPlaylist()) {
            if (track.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                matchingTracks.add(track);
            }
        }

        return matchingTracks;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AudioPlayerGUI());
    }
}

class DynamicBackgroundPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        File imagePath = new File(getImagePathForCurrentSeason());
        if (imagePath != null) {
            ImageIcon imageIcon;
            try {
                imageIcon = new ImageIcon(imagePath.toURL());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            Image image = imageIcon.getImage();
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        } else {
            System.err.println("Image not found!");
        }
    }

    private String getImagePathForCurrentSeason() {
        Month currentMonth = LocalDate.now().getMonth();

        switch (currentMonth) {
            case DECEMBER:
            case JANUARY:
            case FEBRUARY:
                return ".\\src\\resources\\back1.gif";
            case MARCH:
            case APRIL:
            case MAY:
                return ".\\src\\resources\\back2.gif";
            case JUNE:
            case JULY:
            case AUGUST:
                return ".\\src\\resources\\back3.gif";
            case SEPTEMBER:
            case OCTOBER:
            case NOVEMBER:
                return ".\\src\\resources\\back4.gif";
            default:
                return ".\\src\\resources\\backdef.gif";
        }
    }
}
