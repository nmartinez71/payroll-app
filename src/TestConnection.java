package src;
import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        try (Connection conn = DatabaseHelper.getConnection()) {
            System.out.println("Connected to the database successfully!"); // Debug statement
        } catch (Exception e) {
            System.err.println("Failed to connect to the database!"); // Debug statement
            e.printStackTrace();
        }
    }
}