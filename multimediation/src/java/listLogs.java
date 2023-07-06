
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
@WebServlet(name = "listLogs", urlPatterns = {"/listLogs"})
public class listLogs extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        try {
            Class.forName("org.postgresql.Driver");

            try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Mediation", "postgres", "database43")) {
                String queryString = "SELECT * FROM logs";
                PreparedStatement pstmt = con.prepareStatement(queryString);
                ResultSet rs = pstmt.executeQuery();

                out.println("<html>");
                out.println("<head>");
                out.println("<title>List All Logs</title>");
                out.println("<style>");
                out.println("table { border-collapse: collapse; width: 100%; }");
                out.println("th, td { text-align: left; padding: 8px; }");
                out.println("th { background-color: #5a7233; color: white; }");
                out.println("tr:nth-child(even) { background-color: #f2f2f2; }");
                out.println("</style>");
                out.println("</head>");
                out.println("<body>");
                out.println("<div style='width: 80%; margin: 20px auto; border: 1px solid #ccc; padding: 10px;'>");
                out.println("<h2>Logs List:</h2>");
                out.println("<table>");
                out.println("<tr><th> ID</th><th> </th><th> </th><th> </th><th> </th></tr>");

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String column1 = rs.getString("column1");
                    String column2 = rs.getString("column2");
                    String column3 = rs.getString("column3");
                    String column4 = rs.getString("column4");

                    out.println("<tr>");
                    out.println("<td>" + id + "</td>");
                    out.println("<td>" + column1 + "</td>");
                    out.println("<td>" + column2 + "</td>");
                    out.println("<td>" + column3 + "</td>");
                    out.println("<td>" + column4 + "</td>");
                    out.println("</tr>");
                }

                out.println("</table>");
                out.println("</div>");
                out.println("</body>");
                out.println("</html>");

                rs.close();
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(listLogs.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
