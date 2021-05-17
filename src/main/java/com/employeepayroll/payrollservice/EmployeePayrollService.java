package com.employeepayroll.payrollservice;

import com.employeepayroll.ioservice.EmployeePayrollDBService;
import com.employeepayroll.ioservice.FileIOService;
import com.employeepayroll.modal.EmployeePayrollData;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {

    public enum IOService {
        CONSOLE_IO, FILE_IO, DB_IO, REST_IO
    }

    public static final Scanner SC = new Scanner(System.in);
    private List<EmployeePayrollData> employeeList;

    public EmployeePayrollService() {
        this.employeeList = new ArrayList<EmployeePayrollData>();
    }

    public EmployeePayrollService(List<EmployeePayrollData> employeeList) {
        this.employeeList = employeeList;
    }

    public int sizeOfEmployeeList() {
        return this.employeeList.size();
    }

    public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            this.employeeList = new EmployeePayrollDBService().readData();
        return this.employeeList;
    }


    public void readEmployeeData(IOService ioType) {
        if (ioType.equals(IOService.CONSOLE_IO)) {
            System.out.println("Enter employee id:");
            int employeeId = SC.nextInt();
            System.out.println("Enter employee name:");
            SC.nextLine();
            String employeeName = SC.nextLine();
            System.out.println("Enter employee salary:");
            double employeeSalary = SC.nextDouble();
            EmployeePayrollData newEmployee = new EmployeePayrollData(employeeId, employeeName, employeeSalary);
            employeeList.add(newEmployee);
        } else if (ioType.equals(IOService.FILE_IO)) {
            this.employeeList = new FileIOService().readData();
        }
    }

    public void writeEmployeeData(IOService ioType) {
        if (ioType.equals(IOService.CONSOLE_IO)) {
            for (EmployeePayrollData o : employeeList)
                System.out.println(o.toString());
        } else if (ioType.equals(IOService.FILE_IO)) {
            new FileIOService().writeData(employeeList);
        }
    }

    public long countEnteries(IOService ioType) {
        if (ioType.equals(IOService.FILE_IO))
            return new FileIOService().countEntries();
        return 0;
    }

    public void printEmployeePayrollData() {
        new FileIOService().printEmployeePayrolls();
    }
}
