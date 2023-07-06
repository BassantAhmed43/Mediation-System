import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "addRule", urlPatterns = {"/addRule"})
public class addRule extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter pt = resp.getWriter();
        String id = req.getParameter("id");
        String sourceServerId = req.getParameter("source_server_id");
        String destinationServerId = req.getParameter("destination_server_id");
        String time = req.getParameter("time");

        if (id != null && !id.isEmpty() &&
                sourceServerId != null && !sourceServerId.isEmpty() &&
                destinationServerId != null && !destinationServerId.isEmpty() &&
                time != null && !time.isEmpty()) {
            try {
                Class.forName("org.postgresql.Driver");
                try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Mediation", "postgres", "database43")) {
                    System.out.println("connected");
                    Statement stmt = con.createStatement();
                    String queryString = "INSERT INTO rules (id, source_server_id, destination_server_id, time) VALUES (" +
                            Integer.parseInt(id) + ", " +
                            Integer.parseInt(sourceServerId) + ", " +
                            Integer.parseInt(destinationServerId) + ", '" +
                            time + "')";
                    stmt.executeUpdate(queryString);
                    pt.println("success");
                }
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(addRule.class.getName()).log(Level.SEVERE, null, ex);
                pt.println("failure");
            }
        } else {
            pt.println("Invalid input");
        }
    }
}



//@WebServlet(name = "addRule", urlPatterns = {"/addRule"})
//public class addRule extends HttpServlet {
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        PrintWriter pt = resp.getWriter();
//        String id = req.getParameter("id");
//        String sourceServerId = req.getParameter("source_server_id");
//        String destinationServerId = req.getParameter("destination_server_id");
//        String time = req.getParameter("time");
//
//        if (id != null && !id.isEmpty() &&
//                sourceServerId != null && !sourceServerId.isEmpty() &&
//                destinationServerId != null && !destinationServerId.isEmpty() &&
//                time != null && !time.isEmpty()) {
//            try {
//                Class.forName("org.postgresql.Driver");
//                try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Mediation", "postgres", "database43")) {
//                    System.out.println("connected");
//                    String sourceServerName = getServerName(sourceServerId);
//                    String destinationServerName = getServerName(destinationServerId);
//
//                    String queryString = "INSERT INTO rules (id, source_server_id, destination_server_id, time) VALUES (?, ?, ?, ?)";
//
//                    try (PreparedStatement pstmt = con.prepareStatement(queryString)) {
//                        pstmt.setString(1, id);
//                        pstmt.setString(2, sourceServerName);
//                        pstmt.setString(3, destinationServerName);
//                        pstmt.setString(4, time);
//
//                        pstmt.executeUpdate();
//                    }
//
//                    pt.println("success");
//                }
//            } catch (SQLException | ClassNotFoundException ex) {
//                Logger.getLogger(addRule.class.getName()).log(Level.SEVERE, null, ex);
//                pt.println("failure");
//            }
//        } else {
//            pt.println("Invalid input");
//        }
//    }
//
//    private String getServerName(String serverId) {
//        switch (serverId) {
//            case "1":
//                return "Server 1";
//            case "2":
//                return "Server 2";
//            case "3":
//                return "Server 3";
//            case "4":
//                return "Server 4";
//            default:
//                return "";
//        }
//    }
//}
