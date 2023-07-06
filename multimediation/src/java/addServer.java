


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

///**
// *
// * @author Speed
// */


@WebServlet(name = "addServer", urlPatterns = {"/addServer"})
public class addServer extends HttpServlet {

 
    
    @Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    PrintWriter pt = resp.getWriter();
    String server_name = req.getParameter("server_name");
    String ip_address = req.getParameter("ip_address");
    String port_number = req.getParameter("port_number");
    String userName = req.getParameter("userName");
    String password = req.getParameter("password");
    String cdr_location = req.getParameter("cdr_location");
    String used_protocol = req.getParameter("used_protocol");

    if (server_name != null && !server_name.isEmpty() &&
        ip_address != null && !ip_address.isEmpty() &&
        port_number != null && !port_number.isEmpty() &&
        userName != null && !userName.isEmpty() &&
        password != null && !password.isEmpty() &&
        cdr_location != null && !cdr_location.isEmpty() &&
        used_protocol != null && !used_protocol.isEmpty()) {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Mediation", "postgres", "database43")) {
                System.out.println("connected");
                Statement stmt = con.createStatement();
                String queryString = "INSERT INTO server2 VALUES ('" + server_name + "', '" + ip_address + "', '" + port_number + "', '" + userName + "', '" + password + "', '" + cdr_location + "', '" + used_protocol + "')";
                stmt.executeUpdate(queryString);
                pt.println("success");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(addServer.class.getName()).log(Level.SEVERE, null, ex);
            pt.println("failure");
        }
    } else {
        pt.println("Invalid input");
    }
}

}
