package org.example.project1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:mydb.db";

    public DatabaseHelper() {
        createTableIfNotExists();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS records (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "credit TEXT," +
                "roll TEXT," +
                "t1 TEXT," +
                "t2 TEXT," +
                "grade TEXT" +
                ")";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
        }
    }

    public ObservableList<String> getAllRecords() {
        ObservableList<String> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM records ORDER BY id";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String line = formatRecord(rs);
                list.add(line);
            }
        } catch (SQLException e) {
        }
        return list;
    }

    public ObservableList<String> getRecordsByRoll(String roll) {
        ObservableList<String> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM records WHERE roll = ? ORDER BY id";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roll);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String line = formatRecord(rs);
                    list.add(line);
                }
            }
        } catch (SQLException e) {
        }
        return list;
    }

    public void insertRecord(String name, String credit, String roll, String t1, String t2, String grade) {
        String sql = "INSERT INTO records(name,credit,roll,t1,t2,grade) VALUES(?,?,?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, credit);
            ps.setString(3, roll);
            ps.setString(4, t1);
            ps.setString(5, t2);
            ps.setString(6, grade);
            ps.executeUpdate();
        } catch (SQLException e) {
        }
    }

    public void deleteByRoll(String roll) {
        String sql = "DELETE FROM records WHERE roll = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roll);
            ps.executeUpdate();
        } catch (SQLException e) {
        }
    }

    public ObservableList<String> searchRecordsByRoll(String roll) {
        return getRecordsByRoll(roll);
    }

    public double calculateCgpaForRoll(String roll) {
        double totalPoints = 0.0;
        double totalCredits = 0.0;
        String sql = "SELECT credit, grade FROM records WHERE roll = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roll);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String creditStr = rs.getString("credit");
                    String grade = rs.getString("grade");
                    double credit = 0.0;
                    try {
                        credit = Double.parseDouble(creditStr);
                    } catch (NumberFormatException ex) {
                    }
                    double gp = gradeToPoint(grade);
                    totalCredits += credit;
                    totalPoints += gp * credit;
                }
            }
        } catch (SQLException e) {
        }
        if (totalCredits == 0.0) return 0.0;
        return totalPoints / totalCredits;
    }

    private String formatRecord(ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        String credit = rs.getString("credit");
        String roll = rs.getString("roll");
        String t1 = rs.getString("t1");
        String t2 = rs.getString("t2");
        String grade = rs.getString("grade");
        return String.format("%s, Cr: %s,roll:%s T1: %s, T2: %s, Grade: %s",
                name, credit, roll, t1, t2, grade);
    }



    private double gradeToPoint(String grade) {
        if (grade == null) return 0.0;
        return switch (grade) {
            case "A+" -> 4.0;
            case "A"  -> 3.75;
            case "A-" -> 3.5;
            case "B+" -> 3.25;
            case "B"  -> 3.0;
            case "B-" -> 2.75;
            case "C"  -> 2.5;
            case "F"  -> 0.0;
            default   -> 0.0;
        };
    }
}
