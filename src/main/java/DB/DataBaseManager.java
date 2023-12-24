package DB;

import javax.swing.*;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataBaseManager {
    private static final String URL = "jdbc:mysql://192.168.100.11:3306/player";
    private static final String USER = "Pilot";
    private static final String PASSWORD = "password";

    public void getListofTracks(DefaultListModel<File> playlistModel) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM Tracks";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String trackFilePath = resultSet.getString("");
                        File trackFile = new File(trackFilePath);
                        playlistModel.addElement(trackFile);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<File> searchTracksByName(String searchTerm) {
        List<File> matchingTracks = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM tracks WHERE track_name LIKE ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, "%" + searchTerm + "%");

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String trackFilePath = resultSet.getString("file_path");
                        File trackFile = new File(trackFilePath);
                        matchingTracks.add(trackFile);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return matchingTracks;
    }
}
