package com.payrollapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

public class AddTestUser {
    public static void main(String[] args) {
        String firstName = "John";
        String lastName = "Doe";
        String department = "IT";
        String jobTitle = "Developer";
        String status = "Active";
        String dateOfBirth = "1990-01-01";
        String gender = "Male";
        String payType = "Salary";
        String email = "john.doe@example.com";
        String addressLine1 = "123 Main St";
        String addressLine2 = "";
        String city = "Some City";
        String state = "Some State";
        String zip = "12345";
        String plainPassword = "password123"; 

        //hashes password
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

        try (Connection conn = DatabaseHelper.getConnection()) {
            String sql = "INSERT INTO employees (first_name, last_name, department, job_title, status, date_of_birth, gender, pay_type, company_email, address_line1, address_line2, city, state, zip, password) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, department);
            stmt.setString(4, jobTitle);
            stmt.setString(5, status);
            stmt.setString(6, dateOfBirth);
            stmt.setString(7, gender);
            stmt.setString(8, payType);
            stmt.setString(9, email);
            stmt.setString(10, addressLine1);
            stmt.setString(11, addressLine2);
            stmt.setString(12, city);
            stmt.setString(13, state);
            stmt.setString(14, zip);
            stmt.setString(15, hashedPassword); 

            stmt.executeUpdate();
            System.out.println("Test user added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
