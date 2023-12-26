package java.DB;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseManager {
    private static final String URL = "jdbc:mysql://localhost:3309/player";
    private static final String USER = "root";
    private static final String PASSWORD = "Fkmnfbh20";

    public List<File> getListofTracks()
    {
        List<File> playlistModel = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT file_path FROM Tracks"; // Укажите конкретные столбцы, если нужно
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String trackFilePath = resultSet.getString("file_path"); // Укажите название столбца
                        File trackFile = new File(trackFilePath);
                        playlistModel.add(trackFile);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playlistModel;
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
