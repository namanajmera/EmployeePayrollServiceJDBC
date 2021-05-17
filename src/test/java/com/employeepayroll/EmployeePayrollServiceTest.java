package com.employeepayroll;

import com.employeepayroll.modal.EmployeePayrollData;
import com.employeepayroll.payrollservice.EmployeePayrollService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class EmployeePayrollServiceTest {
    @Test
    public void given3EmployeesWhenWrittenToFileShouldMatchNumberOfEmployeeEntries() {
        EmployeePayrollData[] arrayOfEmployees = {
                new EmployeePayrollData(1, "Naman Ajmera", 800000.0),
                new EmployeePayrollData(2, "Ankita Gupta", 850000.0),
                new EmployeePayrollData(3, "Aditya Verma", 900000.0) };

        EmployeePayrollService payrollServiceObject = new EmployeePayrollService(Arrays.asList(arrayOfEmployees));
        payrollServiceObject.writeEmployeeData(EmployeePayrollService.IOService.FILE_IO);
        payrollServiceObject.printEmployeePayrollData();

        Assertions.assertEquals(3, payrollServiceObject.countEnteries(EmployeePayrollService.IOService.FILE_IO));
    }

    @Test
    public void given3EmployeesWhenReadFromFileShouldMatchNumberOfEmployeeEntries() {

        EmployeePayrollService payrollServiceObject = new EmployeePayrollService();
        payrollServiceObject.readEmployeeData(EmployeePayrollService.IOService.FILE_IO);
        int countOfEntriesRead = payrollServiceObject.sizeOfEmployeeList();
        Assertions.assertEquals(3, countOfEntriesRead);
    }
}
