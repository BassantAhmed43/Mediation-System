/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Speed
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private String url = "jdbc:postgresql://localhost:5432/Mediation";
    private String dbUsername = "postgres";
    private String dbPassword = "database43";

    public boolean isValidUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM admins WHERE username = ? AND password = ?")) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); // Return true if there is at least one row in the result set
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
