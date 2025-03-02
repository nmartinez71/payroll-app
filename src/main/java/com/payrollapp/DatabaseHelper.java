package com.payrollapp;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
    private static final String DB_PATH = "db/employee.db"; 

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC"); 
            System.out.println("SQLite JDBC driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found", e);
        }

        
        return DriverManager.getConnection("jdbc:sqlite:" + DB_PATH); 
    }

    public static void initializeDatabase() {

        File dbFile = new File(DB_PATH);
        if (!dbFile.getParentFile().exists()) {
            dbFile.getParentFile().mkdirs();
        }


        try (Connection conn = getConnection(); 
             Statement stmt = conn.createStatement()) { 

            
            stmt.execute("CREATE TABLE IF NOT EXISTS employees (" +
                    "employee_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "first_name TEXT NOT NULL, " +
                    "last_name TEXT NOT NULL, " +
                    "department TEXT NOT NULL, " +
                    "job_title TEXT NOT NULL, " +
                    "status TEXT NOT NULL, " +
                    "date_of_birth TEXT NOT NULL, " +
                    "gender TEXT NOT NULL, " +
                    "pay_type TEXT NOT NULL, " +
                    "company_email TEXT NOT NULL, " +
                    "address_line1 TEXT NOT NULL, " +
                    "address_line2 TEXT, " +
                    "city TEXT NOT NULL, " +
                    "state TEXT NOT NULL, " +
                    "zip TEXT NOT NULL, " +
                    "picture BLOB)");

        
            stmt.execute("CREATE TABLE IF NOT EXISTS payroll (" +
                    "payroll_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "employee_id INTEGER NOT NULL, " +
                    "hours_worked REAL NOT NULL, " +
                    "pto_hours REAL NOT NULL, " +
                    "gross_pay REAL NOT NULL, " +
                    "net_pay REAL NOT NULL, " +
                    "FOREIGN KEY (employee_id) REFERENCES employees (employee_id))");

            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
