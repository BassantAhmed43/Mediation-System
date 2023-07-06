
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.PreparedStatement;
import javax.servlet.annotation.WebServlet;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Speed
 */


@WebServlet(name = "deleteServer", urlPatterns = {"/deleteServer"})
public class deleteServer extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();

        String serverName = req.getParameter("serverName");

        try {
            Class.forName("org.postgresql.Driver");

            try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Mediation", "postgres", "database43")) {
                String deleteQuery = "DELETE FROM server2 WHERE server_name = ?";
                PreparedStatement pstmt = con.prepareStatement(deleteQuery);
                pstmt.setString(1, serverName);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    out.println("Server deleted successfully.");
                } else {
                    out.println("Server not found.");
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(deleteServer.class.getName()).log(Level.SEVERE, null, ex);
            out.println("Error deleting server: " + ex.getMessage());
        }
    }
}
