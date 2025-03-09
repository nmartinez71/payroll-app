package com.payrollapp;

public class EmployeeClass {
    private int employeeId;
    private String firstName;
    private String lastName;
    private String payType;
    private double hourlyRate;
    private double salary;
    private String payInfo;
    private String medicalCoverage;
    private int numDependents;
    private boolean isAdmin;  // New flag for admin check

    // Updated constructor to handle isAdmin flag
    public EmployeeClass(int employeeId, String firstName, String lastName, String payType, 
                         double hourlyRate, double salary, String payInfo, 
                         String medicalCoverage, int numDependents, boolean isAdmin) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.payType = payType;
        this.hourlyRate = hourlyRate;
        this.salary = salary;
        this.payInfo = payInfo;
        this.medicalCoverage = medicalCoverage;
        this.numDependents = numDependents;
        this.isAdmin = isAdmin;  // Set the admin flag
    }

    // Getters and Setters
    public int getEmployeeId() { return employeeId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPayType() { return payType; }
    public double getHourlyRate() { return hourlyRate; }
    public double getSalary() { return salary; }
    public String getPayInfo() { return payInfo; }
    public String getMedicalCoverage() { return medicalCoverage; }
    public int getNumDependents() { return numDependents; }
    public boolean isAdmin() { return isAdmin; }  // Getter for isAdmin flag

    // Setters if needed
    public void setPayInfo(String payInfo) { this.payInfo = payInfo; }
    public void setIsAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }
}
