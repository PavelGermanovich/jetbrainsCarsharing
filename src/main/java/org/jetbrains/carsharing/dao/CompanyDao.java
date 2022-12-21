package org.jetbrains.carsharing.dao;

import org.jetbrains.carsharing.model.Car;
import org.jetbrains.carsharing.model.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyDao {
    List<Company> getAllCompanies();
    Optional<Company> getCompanyById(int id);
    boolean createCompany(Company company);
    List<Car> getCompanyCars(Company company);
}
