package org.jetbrains.carsharing.dao;

import org.jetbrains.carsharing.model.Car;
import org.jetbrains.carsharing.model.Company;
import org.jetbrains.carsharing.util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompanyDaoImpl implements CompanyDao {
    private Database database;

    public CompanyDaoImpl(Database database) {
        this.database = database;
    }

    @Override
    public List<Company> getAllCompanies() {
        Connection con = database.getConnection();
        List<Company> companyList = new ArrayList<>();

        try {
            Statement statement = con.createStatement();
            String request = "SELECT * FROM COMPANY ORDER BY ID";
            ResultSet rs = statement.executeQuery(request);
            while (rs.next()) {
                companyList.add(new Company(rs.getInt("ID"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return companyList;
    }

    @Override
    public Optional<Company> getCompanyById(int id) {
        Connection con = database.getConnection();

        Optional<Company> company = Optional.empty();
        try {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM COMPANY WHERE ID = ?");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                company = Optional.of(new Company(rs.getInt("ID"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return company;
    }

    @Override
    public boolean createCompany(Company company) {
        Connection con = database.getConnection();
        boolean result = false;
        try {
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO COMPANY (name) values (?)");
            preparedStatement.setString(1, company.getName());
            result = preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<Car> getCompanyCars(Company company) {
        Connection con = database.getConnection();
        List<Car> carList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM CAR LEFT JOIN CUSTOMER\n" +
                    "ON CAR.ID = RENTED_CAR_ID\n" +
                    "WHERE COMPANY_ID = ? AND RENTED_CAR_ID IS NULL");
            preparedStatement.setInt(1, company.getId());
            ResultSet result = preparedStatement.executeQuery();
            while (result.next()) {
                Car car = new Car();
                car.setId(result.getInt("ID"));
                car.setName(result.getString("NAME"));
                car.setCompanyId(company.getId());
                carList.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return carList;
    }
}
