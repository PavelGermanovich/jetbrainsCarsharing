package org.jetbrains.carsharing.dao;

import org.jetbrains.carsharing.model.Car;
import org.jetbrains.carsharing.util.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarDaoImpl implements CarDao {

    private Database database;

    public CarDaoImpl(Database database) {
        this.database = database;
    }

    @Override
    public List<Car> findCarsRelatedToCompany(int companyId) {
        Connection connection = database.getConnection();
        List<Car> carList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT * FROM CAR WHERE COMPANY_ID = ?");
            preparedStatement.setInt(1, companyId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                carList.add(new Car(rs.getInt("ID"), rs.getString("NAME")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return carList;
    }

    @Override
    public void createCar(Car car) {
        Connection connection = database.getConnection();
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("INSERT INTO CAR (NAME, COMPANY_ID) values (?, ?)");
            preparedStatement.setString(1, car.getName());
            preparedStatement.setInt(2, car.getCompanyId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
