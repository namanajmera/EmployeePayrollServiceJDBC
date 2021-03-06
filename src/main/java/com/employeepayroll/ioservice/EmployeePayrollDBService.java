package com.employeepayroll.ioservice;

import com.employeepayroll.exceptions.DBException;
import com.employeepayroll.modal.EmployeePayrollData;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeePayrollDBService {

    private static EmployeePayrollDBService employeePayrollDBService;
    private PreparedStatement employeePayrollDataStatement;

    private EmployeePayrollDBService() {
    }

    public static EmployeePayrollDBService getInstance() {
        if (employeePayrollDBService == null)
            employeePayrollDBService = new EmployeePayrollDBService();
        return employeePayrollDBService;
    }

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3307/payroll_service";
        String userName = "root";
        String password = "Spider@6426";
        Connection connection;
        System.out.println("Connecting to database:" + jdbcURL);

        connection = DriverManager.getConnection(jdbcURL, userName, password);
        System.out.println("Connection is successful...!!!!" + connection);

        return connection;
    }

    public List<EmployeePayrollData> readData() {
        String sql = "SELECT * from employee_payroll;";
        List<EmployeePayrollData> employeePayrollDataList = new ArrayList<>();
        try (Connection connection = this.getConnection();) {
            Statement statement = connection.createStatement();  //To create the string sql state to sql statement
            ResultSet resultSet = statement.executeQuery(sql);
            employeePayrollDataList = this.employeePayrollData(resultSet);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return employeePayrollDataList;
    }

    public List<EmployeePayrollData> reteriveDate(LocalDate startDate, LocalDate endDate) throws DBException {
        String sql = String.format("select * from employee_payroll where start between '%s' and '%s';", startDate, endDate);
        List<EmployeePayrollData> employeePayrollDataList = new ArrayList<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            employeePayrollDataList = this.employeePayrollData(resultSet);
        } catch (SQLException throwables) {
            throw new DBException("Connection is failed", DBException.ExceptionType.CONNECTION_FAIL);
        }
        return employeePayrollDataList;
    }

    public EmployeePayrollData addEmployeeToPayrollUC7(String name, double salary, LocalDate date, char gender) throws DBException {
        int employeeId = -1;
        String sql = String.format("insert into employee_payroll (name,gender,salary,start) " +
                "values('%s','%s',%.2f,'%s');", name, gender, salary, Date.valueOf(date));
        EmployeePayrollData employeePayrollData;
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    employeeId = resultSet.getInt(1);
                }
            }
            employeePayrollData = new EmployeePayrollData(employeeId, name, salary, date, gender);
        } catch (SQLException e) {
            throw new DBException("Cannot establish connection", DBException.ExceptionType.CONNECTION_FAIL);
        }
        return employeePayrollData;
    }


    public EmployeePayrollData addEmployeeToPayroll(String name, double salary, LocalDate date, char gender) throws DBException {
        int employeeId = -1;
        Connection connection = null;
        EmployeePayrollData employeePayrollData = null;
        try {
            connection = this.getConnection();
        } catch (SQLException throwables) {
            throw new DBException("Conncetion is Failed", DBException.ExceptionType.CONNECTION_FAIL);
        }
        try (Statement statement = connection.createStatement()) {
            String sql = String.format("insert into employee_payroll (name,gender,salary,start) " +
                    "values('%s','%s',%.2f,'%s');", name, gender, salary, Date.valueOf(date));
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    employeeId = resultSet.getInt(1);
                }
            }
        } catch (SQLException throwables) {
            throw new DBException("Failed", DBException.ExceptionType.CONNECTION_FAIL);
        }
        try (Statement statement = connection.createStatement()) {
            double deductions = salary * 0.2;
            double taxablePay = salary - deductions;
            double tax = taxablePay * 0.1;
            double netPay = salary - tax;
            String sql = String.format("Insert into payroll_details" +
                    "(employee_id,basic_pay,deductions,taxable_pay,tax,net_pay)" +
                    "VALUES" +
                    "(%s,%s,%s,%s,%s,%s)",employeeId,salary,deductions,taxablePay,tax,netPay);
            int rowAffected = statement.executeUpdate(sql, statement.RETURN_GENERATED_KEYS);
            if (rowAffected == 1) {
                employeePayrollData=new EmployeePayrollData(employeeId,name,salary,date);
            }
        } catch (SQLException throwables) {
            throw new DBException("Conncetion is Failed", DBException.ExceptionType.CONNECTION_FAIL);
        }
        return employeePayrollData;
    }

    public Map<String, Double> findAvgSalaryByGender() throws DBException {
        String sql = "select gender,avg(salary) as avg_salary from employee_payroll group by gender;";
        Map<String, Double> genderToAverageSalary = new HashMap<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                double avg_salary = resultSet.getDouble("avg_salary");
                genderToAverageSalary.put(gender, avg_salary);
            }
        } catch (SQLException throwables) {
            throw new DBException("Connection is Failed", DBException.ExceptionType.CONNECTION_FAIL);
        }
        return genderToAverageSalary;
    }

    public int reteriveSalary(double salary) throws DBException {
        String sql = String.format("select count(*) as Count from employee_payroll where salary=%.2f;", salary);
        int count = 0;
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                count = resultSet.getInt("Count");
            }
        } catch (SQLException throwables) {
            throw new DBException("Connection is failed", DBException.ExceptionType.CONNECTION_FAIL);
        }
        return count;
    }

    public List<EmployeePayrollData> getEmployeePayrollData(String name) throws DBException {
        List<EmployeePayrollData> employeePayrollDataList = null;
        if (this.employeePayrollDataStatement == null)
            this.prepareStatementForEmployeeData();
        try {
            employeePayrollDataStatement.setString(1, name);
            ResultSet resultSet = employeePayrollDataStatement.executeQuery();
            employeePayrollDataList = this.employeePayrollData(resultSet);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return employeePayrollDataList;
    }

    private List<EmployeePayrollData> employeePayrollData(ResultSet resultSet) {
        List<EmployeePayrollData> employeePayrollDataList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate localDate = resultSet.getDate("start").toLocalDate();
                employeePayrollDataList.add(new EmployeePayrollData(id, name, salary, localDate));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return employeePayrollDataList;
    }

    private void prepareStatementForEmployeeData() {
        try {
            Connection connection = this.getConnection();
            String sql = "SELECT * FROM employee_payroll where name=?";
            employeePayrollDataStatement = connection.prepareStatement(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int updateEmployeeData(String name, double salary) {
        return this.updateEmployeeDataUsingStatement(name, salary);
    }

    private int updateEmployeeDataUsingStatement(String name, double salary) {
        String sql = String.format("update employee_payroll set salary = %.2f where name = '%s';", salary, name);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

}
