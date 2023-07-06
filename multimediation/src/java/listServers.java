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

@WebServlet(name = "listServers", urlPatterns = {"/listServers"})

public class listServers extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
//out.print("hhiiiiiiiiiiiiii");

       String jdbcURL = "jdbc:postgresql://localhost:5432/Mediation";
String dbUser = "postgres";
String dbPassword = "database43";


            try {
                // Establish the database connection
                Class.forName("org.postgresql.Driver");
              
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(listServers.class.getName()).log(Level.SEVERE, null, ex);
                out.print("not connected");
            }

    Connection conn;
            try {
                conn = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
            

    // Create a SQL query to retrieve caller ID and record URL
    //String query = "SELECT userid , url FROM rateurl";
    String query = "SELECT server_name,ip_address,port_number,username,cdr_location,used_protcol FROM server2";

    // Prepare the statement
    PreparedStatement statement = conn.prepareStatement(query);

    // Execute the query and retrieve the result set
    ResultSet rs = statement.executeQuery();
   

                out.println("<html>");
                out.println("<head>");
                out.println("<title>List All Servers</title>");
                out.println("<style>");
                out.println("table { border-collapse: collapse; width: 100%; }");
                out.println("th, td { text-align: left; padding: 8px; }");
                out.println("th { background-color: #5a7233; color: white; }");
                out.println("tr:nth-child(even) { background-color: #f2f2f2; }");
                out.println("</style>");
                out.println("</head>");
                out.println("<body>");
                out.println("<div style='width: 80%; margin: 20px auto; border: 1px solid #ccc; padding: 10px;'>");
                out.println("<h2>Server List:</h2>");
                out.println("<table>");
                out.println("<tr><th>Server Name</th><th>IP Address</th><th>Port Number</th><th>UserName</th><th>CDR Location</th><th>Used Protocol</th></tr>");
                
                
                int rowId = 1;
               while (rs.next()) {
                    String serverName = rs.getString("server_name");
                    String ipAddress = rs.getString("ip_address");
                    String portNumber = rs.getString("port_number");
                    String userName = rs.getString("username");
                    String cdrLocation = rs.getString("cdr_location");
                    String usedProtocol = rs.getString("used_protcol");


                        out.println("<tr id=\"row" + rowId + "\">");
                        out.println("<td>" + serverName + "</td>");
                        out.println("<td>" + ipAddress + "</td>");
                        out.println("<td>" + portNumber + "</td>");
                        out.println("<td>" + userName + "</td>");
                        out.println("<td>" + cdrLocation + "</td>");
                        out.println("<td>" + usedProtocol + "</td>");
                        out.println("<td><button onclick=\"deleteServer(" + rowId + ", '" + serverName + "')\">Delete</button></td>");
                        out.println("</tr>");

                        rowId++;
                }


                out.println("</table>");
                out.println("</div>");
                out.println("</body>");
                out.println("</html>");

                rs.close();
            } catch (SQLException ex) {
              
            Logger.getLogger(listServers.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    }