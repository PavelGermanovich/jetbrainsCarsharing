package org.jetbrains.carsharing.dao;

import org.jetbrains.carsharing.model.Car;
import org.jetbrains.carsharing.model.Customer;
import org.jetbrains.carsharing.util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDaoImpl implements CustomerDao {
    private Database database;

    public CustomerDaoImpl(Database database) {
        this.database = database;
    }

    @Override
    public void create(Customer customer) {
        Connection connection = database.getConnection();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("INSERT INTO CUSTOMER (name) values (?)");
            preparedStatement.setString(1, customer.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> getCustomerList() {
        Connection connection = database.getConnection();
        List<Customer> customerList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM CUSTOMER");
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("ID"));
                customer.setName(rs.getString("NAME"));
                customer.setCarId(rs.getInt("RENTED_CAR_ID"));
                customerList.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerList;
    }

    @Override
    public Optional<Car> getCustomerCar(Customer customer) {
        Connection connection = database.getConnection();
        Optional<Car> carOptional = Optional.empty();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT * FROM CUSTOMER INNER JOIN CAR\n" +
                            "ON RENTED_CAR_ID = CAR.ID\n"
                            + "WHERE CUSTOMER.ID = ?");
            preparedStatement.setInt(1, customer.getId());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Car car = new Car();
                car.setId(rs.getInt("ID"));
                car.setName(rs.getString("CAR.NAME"));
                car.setCompanyId(rs.getInt("COMPANY_ID"));
                carOptional = Optional.of(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carOptional;
    }

    @Override
    public void removeRentCar(Customer customer) {
        Connection connection = database.getConnection();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("UPDATE CUSTOMER SET RENTED_CAR_ID = NULL\n" +
                            "WHERE ID =?");
            preparedStatement.setInt(1, customer.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addCar(Customer customer) {
        Connection connection = database.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE CUSTOMER SET RENTED_CAR_ID = ?" +
                    "WHERE ID =?");
            preparedStatement.setInt(1, customer.getCarId());
            preparedStatement.setInt(2, customer.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
