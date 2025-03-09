package com.payrollapp;

public class PayrollCalculator {

    public static double calculatePay(double hourlyRate, int[] hoursWorked) {
        double paycheck = 0.0;

        for (int i = 0; i < 7; i++) {
            if (i == 6) { // Saturday (index 6) - time and a half
                paycheck += hoursWorked[i] * hourlyRate * 1.5;
            } else if (hoursWorked[i] > 8) { // Overtime pay
                paycheck += 8 * hourlyRate + (hoursWorked[i] - 8) * hourlyRate * 1.5;
            } else {
                paycheck += hoursWorked[i] * hourlyRate;
            }
        }

        return paycheck;
    }

    public static double calculateStateTax(double paycheck) {
        return paycheck * 0.0315;
    }

    public static double calculateFederalTax(double paycheck) {
        return paycheck * 0.0765;
    }

    public static double calculateSocialSecurityTax(double paycheck) {
        return paycheck * 0.062;
    }

    public static double calculateMedicareTax(double paycheck) {
        return paycheck * 0.0145;
    }

    public static double calculateMedicalCost(String medicalCoverage) {
        if ("Single".equals(medicalCoverage)) {
            return 50.0;
        } else if ("Family".equals(medicalCoverage)) {
            return 100.0;
        }
        return 0.0;
    }

    public static double calculateDependentStipend(int numDependents) {
        return numDependents * 45.0;
    }
}
