package com.employeepayroll.ioservice;

import com.employeepayroll.modal.EmployeePayrollData;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollDBService {

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
        List<EmployeePayrollData> employeePayrollData = new ArrayList<>();
        try (Connection connection = this.getConnection();) {
            Statement statement = connection.createStatement();  //To create the string sql state to sql statement
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                LocalDate localDate = resultSet.getDate("start").toLocalDate();
                employeePayrollData.add(new EmployeePayrollData(id, name, salary, localDate));
            }
//            connection.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return employeePayrollData;
    }
}
