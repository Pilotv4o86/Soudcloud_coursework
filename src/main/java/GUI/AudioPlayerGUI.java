package GUI;

import Server.AudioPlayer;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class AudioPlayerGUI {

    private AudioPlayer audioPlayer;
    private JButton startStopButton;
    private JButton nextButton;
    private JButton prevButton;
    private JButton addTrackButton;
    private JList<File> playlistList;
    private DefaultListModel<File> playlistModel;
    private AdvancedPlayer player;
    private Timer timer;
    private JProgressBar progressBar;
    private JLabel currentTrackLabel;
    private JButton playPauseButton; // Добавим поле playPauseButton

    public AudioPlayerGUI() {
        DefaultListModel<File> playlistModel = new DefaultListModel<>();
        audioPlayer = new AudioPlayer(playlistModel);
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Audio Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BackgroundPanel panel = new BackgroundPanel();
        panel.setLayout(new FlowLayout());

        startStopButton = new JButton("<html>&#9658;</html>");
        startStopButton.setFont(new Font("Arial", Font.PLAIN, 16));
        startStopButton.addActionListener(e -> handleStartStopButton());

        nextButton = new JButton("<html>&#9197;</html>");
        nextButton.addActionListener(e -> audioPlayer.playNext());

        prevButton = new JButton("<html>&#9198;</html>");
        prevButton.addActionListener(e -> audioPlayer.playPrevious());

        addTrackButton = new JButton("<html>&#10010;&#9836;</html>");
        addTrackButton.addActionListener(e -> handleAddTrackButton());

        playlistModel = new DefaultListModel<>();
        playlistList = new JList<>(playlistModel);
        JScrollPane playlistScrollPane = new JScrollPane(playlistList);
        playlistScrollPane.setBounds(50, 70, 400, 100);
        frame.add(playlistScrollPane);

        JButton searchButton = new JButton("\uD83D\uDD0E");
        searchButton.addActionListener(e -> handleSearch());
        panel.add(searchButton);

        panel.add(prevButton);

        // Добавим playPauseButton в GUI
        playPauseButton = new JButton("<html>&#9658;</html>");
        playPauseButton.addActionListener(e -> handlePlayPauseButton());
        panel.add(playPauseButton);

        panel.add(nextButton);
        panel.add(addTrackButton);

        progressBar = new JProgressBar(0, 100);
        panel.add(progressBar);

        currentTrackLabel = new JLabel("Currently Playing: ");
        panel.add(currentTrackLabel);

        frame.getContentPane().add(panel);
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void handleStartStopButton() {
    }

    private void handleAddTrackButton() {
    }

    private void handleSearch() {
    }

    private void handlePlayPauseButton() {
        togglePlayPause();
    }

    private void togglePlayPause() {
        if (audioPlayer.isPlaying()) {
            audioPlayer.stop();
            stop();
        } else {
            audioPlayer.play();
            playPauseButton.setText("<html>&#9209;</html>");
            play();
        }
    }

    private void play() {
        File selectedFile = playlistList.getSelectedValue();
        if (selectedFile != null) {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                player = new AdvancedPlayer(fileInputStream);
                player.setPlayBackListener(new PlaybackListener() {
                    @Override
                    public void playbackFinished(PlaybackEvent evt) {
                        stop();
                    }
                });

                timer = new Timer(100, e -> updateProgressBar());
                timer.start();

                playPauseButton.setText("<html>&#9209;</html>");
                currentTrackLabel.setText("Currently Playing: " + selectedFile.getName());

                new Thread(() -> {
                    try {
                        player.play();
                    } catch (JavaLayerException e) {
                        e.printStackTrace();
                    }
                }).start();

            } catch (JavaLayerException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stop() {
        if (player != null) {
            player.close();
            player = null;
            timer.stop();
            progressBar.setValue(0);
            playPauseButton.setText("<html>&#9658;</html>");
            currentTrackLabel.setText("Currently Playing: No Track");
        }
    }

    private void updateProgressBar() {
        if (audioPlayer != null) {
            int currentPosition = audioPlayer.getCurrentPosition();
            int currentTrackDuration = audioPlayer.getCurrentTrackDuration();

            if (currentTrackDuration > 0) {
                int progress = (int) (((double) currentPosition / currentTrackDuration) * 100);
                progressBar.setValue(progress);
            }
        }
    }


    // Остальные методы остаются без изменений

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AudioPlayerGUI());
    }
}

// Класс BackgroundPanel остается без изменений
class BackgroundPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        File imageURL = new File(".\\src\\resources\\back1.gif");

        if (imageURL != null) {
            ImageIcon imageIcon = null;
            try {
                imageIcon = new ImageIcon(imageURL.toURL());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            Image image = imageIcon.getImage();
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        } else {
            System.err.println("Image not found!");
        }
    }
}