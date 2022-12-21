package org.jetbrains.carsharing.dao;


import org.jetbrains.carsharing.model.Car;

import java.util.List;

public interface CarDao {
    List<Car> findCarsRelatedToCompany(int companyId);
    void createCar(Car car);
}
