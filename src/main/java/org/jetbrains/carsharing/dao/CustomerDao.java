package org.jetbrains.carsharing.dao;

import org.jetbrains.carsharing.model.Car;
import org.jetbrains.carsharing.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    void create(Customer customer);
    List<Customer> getCustomerList();
    Optional<Car> getCustomerCar(Customer customer);
    void removeRentCar(Customer customer);
    void addCar(Customer customer);
}
