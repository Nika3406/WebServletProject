import java.sql.Connection;
import java.sql.DriverManager;

public class MariaDBTest {
    public static void main(String[] args) {
        String url = "jdbc:mariadb://localhost:3306/WebDatabase";
        String user = "root";
        String password = "root";

        try {
            Class.forName("org.mariadb.jdbc.Driver"); // Load driver
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("MariaDB JDBC Driver not found!");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Connection failed!");
            e.printStackTrace();
        }
    }
}