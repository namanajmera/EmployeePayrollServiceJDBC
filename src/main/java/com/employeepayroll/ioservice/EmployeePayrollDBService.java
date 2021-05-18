package com.employeepayroll.ioservice;

import com.employeepayroll.exceptions.DBException;
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

    public List<EmployeePayrollData> getEmployeePayrollData(String name) throws DBException {
        List<EmployeePayrollData> employeePayrollDataList=new ArrayList<>();
        String sql=String.format("select * from employee_payroll where name='%s';",name);
        try(Connection connection=getConnection()) {
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(sql);
            while (resultSet.next()){
                int id=resultSet.getInt("id");
                String empName=resultSet.getString("name");
                double salary=resultSet.getDouble("salary");
                LocalDate startDate=resultSet.getDate("start").toLocalDate();
                employeePayrollDataList.add(new EmployeePayrollData(id,empName,salary,startDate));
            }
        } catch (SQLException throwables) {
            throw new DBException("Cannot establish connection",DBException.ExceptionType.CONNECTION_FAIL);
        }
        return employeePayrollDataList;
    }

    public int updateEmployeeData(String name, double salary) {
        return this.updateEmployeeDataUsingStatement(name,salary);
    }

    private int updateEmployeeDataUsingStatement(String name, double salary) {
        String sql=String.format("update employee_payroll set salary = %.2f where name = '%s';",salary,name);
        try(Connection connection=this.getConnection()) {
            Statement statement=connection.createStatement();
            return statement.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }


}
