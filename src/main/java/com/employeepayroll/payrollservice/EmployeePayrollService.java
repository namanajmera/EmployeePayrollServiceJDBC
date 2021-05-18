package com.employeepayroll.payrollservice;

import com.employeepayroll.exceptions.DBException;
import com.employeepayroll.exceptions.SQLUpdateFailedException;
import com.employeepayroll.ioservice.EmployeePayrollDBService;
import com.employeepayroll.ioservice.FileIOService;
import com.employeepayroll.modal.EmployeePayrollData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeePayrollService {
    public enum IOService {
        CONSOLE_IO, FILE_IO, DB_IO, REST_IO
    }

    public static final Scanner SC = new Scanner(System.in);
    private List<EmployeePayrollData> employeePayrollDataList;
    private EmployeePayrollDBService employeePayrollDBService;

    public EmployeePayrollService() {
        employeePayrollDBService=EmployeePayrollDBService.getInstance();
        this.employeePayrollDataList = new ArrayList<EmployeePayrollData>();
    }

    public EmployeePayrollService(List<EmployeePayrollData> employeeList) {
        this();
        this.employeePayrollDataList = employeeList;
    }

    public int sizeOfEmployeeList() {
        return this.employeePayrollDataList.size();
    }

    public List<EmployeePayrollData> readEmployeePayrollData(IOService ioService) {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollDataList =employeePayrollDBService.readData();
        return this.employeePayrollDataList;
    }


    public List<EmployeePayrollData> readEmployeeDataWithGivenDateRange(IOService ioService, LocalDate startDate, LocalDate endDate) throws DBException {
        if (ioService.equals(IOService.DB_IO))
            this.employeePayrollDataList=employeePayrollDBService.reteriveDate(startDate,endDate);
        return this.employeePayrollDataList;
    }

    public int readEmployeeDataWtihGivenSalary(double salary) throws DBException {
        int result=employeePayrollDBService.reteriveSalary(salary);
        if (result==0){
            try {
                throw new SQLUpdateFailedException("Query is failed.");
            } catch (SQLUpdateFailedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public void updateEmployeeSalary(String name, double salary) {
        int result=employeePayrollDBService.updateEmployeeData(name,salary);
        if(result==0) {
            try {
                throw new SQLUpdateFailedException("Query is failed.");
            } catch (SQLUpdateFailedException e) {
                e.printStackTrace();
            }
        }
        EmployeePayrollData employeePayrollData=this.getEmployeePayrollData(name);
        if (employeePayrollData!=null) employeePayrollData.salary=salary;

    }

    private EmployeePayrollData getEmployeePayrollData(String name) {
        return this.employeePayrollDataList.stream()
                .filter(employeePayrollDataItem -> employeePayrollDataItem.employeeName.equals(name))
                .findFirst()
                .orElse(null);
    }

    public boolean checkEmployeePayrollInSyncWithDB(String name) throws DBException {
        List<EmployeePayrollData> employeePayrollDataList=employeePayrollDBService.getEmployeePayrollData(name);
        return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
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
            employeePayrollDataList.add(newEmployee);
        } else if (ioType.equals(IOService.FILE_IO)) {
            this.employeePayrollDataList = new FileIOService().readData();
        }
    }

    public void writeEmployeeData(IOService ioType) {
        if (ioType.equals(IOService.CONSOLE_IO)) {
            for (EmployeePayrollData o : employeePayrollDataList)
                System.out.println(o.toString());
        } else if (ioType.equals(IOService.FILE_IO)) {
            new FileIOService().writeData(employeePayrollDataList);
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
