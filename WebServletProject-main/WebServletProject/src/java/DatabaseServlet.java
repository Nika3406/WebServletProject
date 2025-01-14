import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/DatabaseServlet")
public class DatabaseServlet extends HttpServlet 
{
    private static final String DB_URL = "jdbc:mariadb://localhost:3306/WebServlet";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");
        String id = request.getParameter("id");

        try 
        {
            Class.forName("org.mariadb.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) 
            {
                if ("view".equals(action)) 
                {
                    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Staff WHERE id = ?");
                    stmt.setString(1, id);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) 
                    {
                        out.println("<h2>Staff Information</h2>");
                        out.println("<p>ID: " + rs.getString("id") + "</p>");
                        out.println("<p>Last Name: " + rs.getString("lastName") + "</p>");
                        out.println("<p>First Name: " + rs.getString("firstName") + "</p>");
                        out.println("<p>MI: " + rs.getString("mi") + "</p>");
                        out.println("<p>Address: " + rs.getString("address") + "</p>");
                        out.println("<p>City: " + rs.getString("city") + "</p>");
                        out.println("<p>State: " + rs.getString("state") + "</p>");
                        out.println("<p>Telephone: " + rs.getString("telephone") + "</p>");
                        out.println("<p>Email: " + rs.getString("email") + "</p>");
                        out.println("<p>Gender: " + rs.getString("gender") + "</p>");
                    } 
                    else 
                    {
                        out.println("<p>Record not found!</p>");
                    }
                } 
                else if ("insert".equals(action)) {
                    PreparedStatement stmt = conn.prepareStatement(
                            "INSERT INTO Staff (id, lastName, firstName, mi, address, city, state, telephone, email, gender) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    stmt.setString(1, id);
                    stmt.setString(2, request.getParameter("lastName"));
                    stmt.setString(3, request.getParameter("firstName"));
                    stmt.setString(4, request.getParameter("mi"));
                    stmt.setString(5, request.getParameter("address"));
                    stmt.setString(6, request.getParameter("city"));
                    stmt.setString(7, request.getParameter("state"));
                    stmt.setString(8, request.getParameter("telephone"));
                    stmt.setString(9, request.getParameter("email"));
                    stmt.setString(10, request.getParameter("gender"));
                    stmt.executeUpdate();
                    out.println("<p>Record inserted successfully!</p>");
                } 
                else if ("update".equals(action)) 
                {
                    PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE Staff SET lastName = ?, firstName = ?, mi = ?, address = ?, city = ?, state = ?, telephone = ?, email = ?, gender = ? WHERE id = ?");
                    stmt.setString(1, request.getParameter("lastName"));
                    stmt.setString(2, request.getParameter("firstName"));
                    stmt.setString(3, request.getParameter("mi"));
                    stmt.setString(4, request.getParameter("address"));
                    stmt.setString(5, request.getParameter("city"));
                    stmt.setString(6, request.getParameter("state"));
                    stmt.setString(7, request.getParameter("telephone"));
                    stmt.setString(8, request.getParameter("email"));
                    stmt.setString(9, request.getParameter("gender"));
                    stmt.setString(10, id);
                    stmt.executeUpdate();
                    out.println("<p>Record updated successfully!</p>");
                }
                else 
                {
                    out.println("<p>Invalid action!</p>");
                }
            } 
            catch (SQLException e) 
            {
                out.println("<p>Error connecting to the database!</p>");
                e.printStackTrace(out);
            }
        } 
        catch (ClassNotFoundException e) 
        {
            out.println("<p>MariaDB JDBC Driver not found! Please check your classpath.</p>");
            e.printStackTrace(out);
        }
    }
}