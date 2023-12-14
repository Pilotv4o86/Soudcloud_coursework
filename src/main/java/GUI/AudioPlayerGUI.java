package GUI;

import Server.AudioPlayer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class AudioPlayerGUI {

    private AudioPlayer audioPlayer;
    private JButton startStopButton;
    private JButton nextButton;
    private JButton prevButton;
    private JButton addTrackButton;
    private JSlider progressBar;
    private JLabel timeLabel;
    private JTextField searchField; // Добавлено поле для ввода строки поиска

    public AudioPlayerGUI() {
        audioPlayer = new AudioPlayer();
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Audio Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();

        startStopButton = new JButton("Start");
        startStopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleStartStopButton();
            }
        });

        nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                audioPlayer.playNext();
            }
        });

        prevButton = new JButton("Previous");
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                audioPlayer.playPrevious();
            }
        });

        addTrackButton = new JButton("Add Track");
        addTrackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddTrackButton();
            }
        });

        progressBar = new JSlider();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setValue(0);
        progressBar.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!progressBar.getValueIsAdjusting()) {
                    int value = progressBar.getValue();
                    audioPlayer.seek(value);
                }
            }
        });

        timeLabel = new JLabel("0:00");

        searchField = new JTextField(20); // 20 - количество символов в поле для ввода
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSearch();
            }
        });

        panel.add(startStopButton);
        panel.add(prevButton);
        panel.add(nextButton);
        panel.add(addTrackButton);
        panel.add(progressBar);
        panel.add(timeLabel);

        // Добавлен переход на новую строку
        panel.add(Box.createVerticalStrut(10)); // Вертикальный отступ
        panel.add(searchField); // Добавлено поле для ввода строки поиска

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
            audioPlayer.start();
            startStopButton.setText("Stop");
        }
    }

    private void handleAddTrackButton() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            audioPlayer.addToPlaylist(selectedFile);
            JOptionPane.showMessageDialog(null, "Track added to the playlist: " + selectedFile.getName());
        }
    }

    private void handleSearch() {
        // Реализуйте логику поиска музыкальных произведений на основе введенной строки
        String searchText = searchField.getText();
        // Ваш код для обработки строки поиска
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AudioPlayerGUI();
            }
        });
    }
}
