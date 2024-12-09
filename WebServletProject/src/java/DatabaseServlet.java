import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/DatabaseServlet")
public class DatabaseServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/WebDatabase";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect GET requests to POST handler
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");
        String id = request.getParameter("id");

        try {
            // Explicitly load the MariaDB JDBC driver
            Class.forName("org.mariadb.jdbc.Driver"); // Ensure the driver is loaded

            // Attempt to establish a connection
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                if ("view".equals(action)) {
                    // Handle the "view" action
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM YourTableName WHERE id = ?");
                    stmt.setString(1, id);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        out.println("<p>ID: " + rs.getString("id") + "</p>");
                        out.println("<p>Last Name: " + rs.getString("lastName") + "</p>");
                        out.println("<p>First Name: " + rs.getString("firstName") + "</p>");
                    } else {
                        out.println("<p>Record not found!</p>");
                    }
                } else if ("insert".equals(action)) {
                    // Handle the "insert" action
                    PreparedStatement stmt = conn.prepareStatement(
                            "INSERT INTO YourTableName (id, lastName, firstName) VALUES (?, ?, ?)");
                    stmt.setString(1, id);
                    stmt.setString(2, request.getParameter("lastName"));
                    stmt.setString(3, request.getParameter("firstName"));
                    stmt.executeUpdate();
                    out.println("<p>Record inserted successfully!</p>");
                } else if ("update".equals(action)) {
                    // Handle the "update" action
                    PreparedStatement stmt = conn.prepareStatement(
                            "UPDATE YourTableName SET lastName = ?, firstName = ? WHERE id = ?");
                    stmt.setString(1, request.getParameter("lastName"));
                    stmt.setString(2, request.getParameter("firstName"));
                    stmt.setString(3, id);
                    stmt.executeUpdate();
                    out.println("<p>Record updated successfully!</p>");
                } else {
                    out.println("<p>Invalid action!</p>");
                }
            } catch (SQLException e) {
                e.printStackTrace(out);
            }
        } catch (ClassNotFoundException e) {
            out.println("<p>MariaDB JDBC Driver not found! Please check your classpath.</p>");
            e.printStackTrace(out);
        }
    }
}
