
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Speed
 */
@WebServlet(name = "listRules", urlPatterns = {"/listRules"})
public class listRules extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        String jdbcURL = "jdbc:postgresql://localhost:5432/Mediation";
        String dbUser = "postgres";
        String dbPassword = "database43";

        try {
            // Establish the database connection
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);

            // Create a SQL query to retrieve rules
            String query = "SELECT id, source_server_id, destination_server_id, time FROM rules";

            // Prepare the statement
            PreparedStatement statement = conn.prepareStatement(query);

            // Execute the query and retrieve the result set
            ResultSet rs = statement.executeQuery();

            out.println("<html>");
            out.println("<head>");
            out.println("<title>List All Rules</title>");
            out.println("<style>");
            out.println("table { border-collapse: collapse; width: 100%; }");
            out.println("th, td { text-align: left; padding: 8px; }");
            out.println("th { background-color: #5a7233; color: white; }");
            out.println("tr:nth-child(even) { background-color: #f2f2f2; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div style='width: 80%; margin: 20px auto; border: 1px solid #ccc; padding: 10px;'>");
            out.println("<h2>Rule List:</h2>");
            out.println("<table>");
            out.println("<tr><th>ID</th><th>Source Server ID</th><th>Destination Server ID</th><th>Time</th></tr>");

            int rowId = 1;
            while (rs.next()) {
                int id = rs.getInt("id");
                int sourceServerId = rs.getInt("source_server_id");
                int destinationServerId = rs.getInt("destination_server_id");
                String time = rs.getString("time");

                out.println("<tr id=\"row" + rowId + "\">");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + sourceServerId + "</td>");
                out.println("<td>" + destinationServerId + "</td>");
                out.println("<td>" + time + "</td>");
                out.println("<td><button onclick=\"deleteRule(" + rowId + ", " + id + ")\">Delete</button></td>");
                out.println("</tr>");

                rowId++;
            }

            out.println("</table>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

            rs.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(listRules.class.getName()).log(Level.SEVERE, null, ex);
            out.println("Error: " + ex.getMessage());
        }
    }
}

