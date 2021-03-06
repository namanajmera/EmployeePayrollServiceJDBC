package com.employeepayroll.modal;

import java.time.LocalDate;

public class EmployeePayrollData {
    public int employeeId;
    public String employeeName;
    public double salary;
    public LocalDate startDate;
    public char gender;

    public EmployeePayrollData() {
    }

    public EmployeePayrollData(int employeeId, String employeeName, double salary) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.salary = salary;
    }

    public EmployeePayrollData(int employeeId, String employeeName, double salary, LocalDate startDate) {
        this(employeeId, employeeName, salary);
        this.startDate = startDate;
    }

    public EmployeePayrollData(int employeeId, String employeeName, double salary, LocalDate startDate, char gender) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.salary = salary;
        this.startDate = startDate;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "EmployeePayrollData{" +
                "employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", salary=" + salary +
                ", startDate=" + startDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeePayrollData that = (EmployeePayrollData) o;
        return employeeId == that.employeeId && Double.compare(that.salary, salary) == 0 &&
                employeeName.equals(that.employeeName);

    }
}
