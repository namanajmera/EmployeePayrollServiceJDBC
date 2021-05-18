package com.employeepayroll;

import com.employeepayroll.exceptions.DBException;
import com.employeepayroll.modal.EmployeePayrollData;
import com.employeepayroll.payrollservice.EmployeePayrollService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.employeepayroll.payrollservice.EmployeePayrollService.IOService.DB_IO;

public class EmployeePayrollServiceTest {
    @Test
    public void given3EmployeesWhenWrittenToFileShouldMatchNumberOfEmployeeEntries() {
        EmployeePayrollData[] arrayOfEmployees = {
                new EmployeePayrollData(1, "Naman Ajmera", 800000.0),
                new EmployeePayrollData(2, "Ankit Gupta", 850000.0),
                new EmployeePayrollData(3, "Aditya Verma", 900000.0)};

        EmployeePayrollService payrollServiceObject = new EmployeePayrollService(Arrays.asList(arrayOfEmployees));
        payrollServiceObject.writeEmployeeData(EmployeePayrollService.IOService.FILE_IO);
        payrollServiceObject.printEmployeePayrollData();

        Assertions.assertEquals(3, payrollServiceObject.countEnteries(EmployeePayrollService.IOService.FILE_IO));
    }

    @Test
    public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
        EmployeePayrollService employeePayrollService = new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData = employeePayrollService.readEmployeePayrollData(DB_IO);
        Assertions.assertEquals(5, employeePayrollData.size());
    }

    @Test
    public void givenNewSalaryForEmployee_WhenUpdated_ShouldMatch() throws DBException {
        EmployeePayrollService employeePayrollService=new EmployeePayrollService();
        List<EmployeePayrollData> employeePayrollData=employeePayrollService.readEmployeePayrollData(DB_IO);
        employeePayrollService.updateEmployeeSalary("Terisa",3000000.00);
        boolean result=employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
        Assertions.assertTrue(result);
    }

    @Test
    public void givenEmployeePayrollInDB_WhenReterivedEmployeeByGivenRange_ShouldReturnEmployeeCount() throws DBException {
        EmployeePayrollService employeePayrollService=new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        LocalDate startDate=LocalDate.of(2019,01,01);
        LocalDate endDate=LocalDate.of(2020,01,01);
        List<EmployeePayrollData> employeePayrollDataList=
                employeePayrollService.readEmployeeDataWithGivenDateRange(DB_IO,startDate,endDate);
        Assertions.assertEquals(2,employeePayrollDataList.size());
    }

    @Test
    public void givenEmployeePayrollInDB_WhereCOuntBySalary_ShouldReturnEmployeeCount() throws DBException {
        EmployeePayrollService employeePayrollService=new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        double salary=3000000.00;
        int result=employeePayrollService.readEmployeeDataWtihGivenSalary(salary);
        Assertions.assertEquals(3,result);
    }

    @Test
    public void givenPayrollData_WhenAverageSalaryReterivedByGender_Should_ReturnProperValue() throws DBException {
        EmployeePayrollService employeePayrollService=new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        Map<String,Double> averageSalaryByGender=employeePayrollService.averageSalaryByGender(DB_IO);
        Assertions.assertTrue(averageSalaryByGender.get("M").equals(2000000.00) &&
                averageSalaryByGender.get("F").equals(3000000.00));
    }

    @Test
    public void givenEmployeeDetail_WhenAdded_ShowSyncWithDB() throws DBException {
        EmployeePayrollService employeePayrollService=new EmployeePayrollService();
        employeePayrollService.readEmployeePayrollData(DB_IO);
        employeePayrollService.addEmployeePayrollDetail("Naman",500000.00,LocalDate.now(),'M');
        boolean result=employeePayrollService.checkEmployeePayrollInSyncWithDB("Naman");
        Assertions.assertTrue(result);
    }
}
