package com.example.authorizationapp.supportsClass;

public class SalaryGenerator {
    double random;

    public double engineSalary() {
        double salary = 70000 + (Math.random() * 100000);
        return Math.round(salary * 100.0) / 100.0;
    }

    public double managerSalary() {
        double salary = 50000 + (Math.random() * 90000);
        return Math.round(salary * 100.0) / 100.0;
    }

    public double administratorSalary() {
        double salary = 60_000 + (Math.random() * 120_000);
        return Math.round(salary * 100.0) / 100.0;
    }
}
