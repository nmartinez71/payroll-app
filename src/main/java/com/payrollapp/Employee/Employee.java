package com.payrollapp.Employee;

import java.util.List;

public class Employee {

    private int employeeId;
    private String firstName;
    private String lastName;
    private String payType; // "Salary" or "Hourly"
    private double hourlyRate;
    private double salary; // Only for salaried employees
    private String medicalCoverage; // "Single" or "Family"
    private List<Dependent> dependents; // List of dependents
    private List<TimeEntry> timeEntries; // List of time entries (for hourly employees)
    private double grossPay;
    private double netPay;

    // Default Constructor
    public Employee() {
    }

    // Constructor for Hourly Employee
    public Employee(int employeeId, String firstName, String lastName, String payType, double hourlyRate) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.payType = payType;
        this.hourlyRate = hourlyRate;
    }

    // Constructor for Salaried Employee
    public Employee(int employeeId, String firstName, String lastName, String payType, double salary) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.payType = payType;
        this.salary = salary;
    }

    // Getters and Setters

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getMedicalCoverage() {
        return medicalCoverage;
    }

    public void setMedicalCoverage(String medicalCoverage) {
        this.medicalCoverage = medicalCoverage;
    }

    public List<Dependent> getDependents() {
        return dependents;
    }

    public void setDependents(List<Dependent> dependents) {
        this.dependents = dependents;
    }

    public List<TimeEntry> getTimeEntries() {
        return timeEntries;
    }

    public void setTimeEntries(List<TimeEntry> timeEntries) {
        this.timeEntries = timeEntries;
    }

    public double getGrossPay() {
        return grossPay;
    }

    public void setGrossPay(double grossPay) {
        this.grossPay = grossPay;
    }

    public double getNetPay() {
        return netPay;
    }

    public void setNetPay(double netPay) {
        this.netPay = netPay;
    }

    @Override
    public String toString() {
        return "Employee{id=" + employeeId + ", name=" + firstName + " " + lastName + ", payType=" + payType + "}";
    }
}
