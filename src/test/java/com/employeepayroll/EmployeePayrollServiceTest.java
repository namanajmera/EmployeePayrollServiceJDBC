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
<<<<<<< HEAD
                new EmployeePayrollData(1, "Naman Ajmera", 800000.0),
                new EmployeePayrollData(2, "Ankit Gupta", 850000.0),
                new EmployeePayrollData(3, "Aditya Verma", 900000.0) };
||||||| 6831246
                new EmployeePayrollData(1, "Aditya Verma", 800000.0),
                new EmployeePayrollData(2, "Akhil Singh", 850000.0),
                new EmployeePayrollData(3, "Anamika Bhatt", 900000.0) };
=======
                new EmployeePayrollData(1, "Naman Ajmera", 800000.0),
                new EmployeePayrollData(2, "Ankita Gupta", 850000.0),
                new EmployeePayrollData(3, "Aditya Verma", 900000.0) };
>>>>>>> UC1-CreatAJDBCConnection

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
