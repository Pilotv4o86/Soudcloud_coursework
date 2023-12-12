package GUI;

import Server.AudioPlayer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class AudioPlayerGUI {

    private AudioPlayer audioPlayer;
    private JButton startStopButton;
    private JButton nextButton;
    private JButton prevButton;

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

        panel.add(startStopButton);
        panel.add(nextButton);
        panel.add(prevButton);

        JButton addTrackButton = new JButton("Add Track");
        addTrackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddTrackButton();
            }
        });
        panel.add(addTrackButton);

        frame.getContentPane().add(panel);
        frame.setSize(400, 150);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AudioPlayerGUI();
            }
        });
    }
}
