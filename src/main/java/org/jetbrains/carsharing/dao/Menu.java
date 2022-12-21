package org.jetbrains.carsharing.dao;


import org.jetbrains.carsharing.model.Car;
import org.jetbrains.carsharing.model.Company;
import org.jetbrains.carsharing.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Menu {
    private CompanyDao companyDao;
    private CarDao carDao;
    private CustomerDao customerDao;

    public Menu(CompanyDao companyDao, CarDao carDao, CustomerDao customerDao) {
        this.carDao = carDao;
        this.companyDao = companyDao;
        this.customerDao = customerDao;
    }

    public void mainFlow() {
        System.out.println("1. Log in as a manager\n" +
                "2. Log in as a customer\n" +
                "3. Create a customer\n" +
                "0. Exit");
        int optionSelected = Integer.parseInt(new Scanner(System.in).nextLine());

        switch (optionSelected) {
            case 0: {
                System.exit(0);
            }
            case 1: {
                logInAsManager();
                break;
            }
            case 2: {
                logInAsCustomer();
            }
            case 3: {
                createCustomer();
                break;
            }
            default: {
                System.out.println("incorrect option");
            }
        }
    }

    private void createCustomer() {
        System.out.println("Enter the customer name:");
        Customer customer = new Customer();
        customer.setName(new Scanner(System.in).nextLine());
        customerDao.create(customer);
        System.out.println("The customer was added!");
        mainFlow();
    }

    private void logInAsCustomer() {
        List<Customer> customerList = customerDao.getCustomerList();
        if (customerList.isEmpty()) {
            System.out.println("The customer list is empty!");
            mainFlow();
        } else {
            System.out.println("Customer list:");
            customerList.forEach(System.out::println);
            System.out.println("0. Back");
            int optionSelected = Integer.parseInt(new Scanner(System.in).nextLine());
            if (optionSelected == 0) {
                mainFlow();
            } else {
                printCustomerMenu(customerList.get(optionSelected - 1));
            }
        }
    }

    private void printCustomerMenu(Customer customer) {
        System.out.println("1. Rent a car\n" +
                "2. Return a rented car\n" +
                "3. My rented car\n" +
                "0. Back");
        int optionSelected = Integer.parseInt(new Scanner(System.in).nextLine());
        customer.setId(customer.getId());
        switch (optionSelected) {
            case 0: {
                mainFlow();
                break;
            }
            case 1: {
                rentCar(customer);
                break;
            }
            case 2: {
                returnRentedCar(customer);
                break;
            }
            case 3: {
                printCarInfo(customer);
                break;
            }
        }

    }

    private void printCarInfo(Customer customer) {
        Optional<Car> car = customerDao.getCustomerCar(customer);
        if (car.isPresent()) {
            System.out.println(String.format("Your rented car:\n" +
                    "%s\n" +
                    "Company:\n" +
                    "%s", car.get().getName(), companyDao.getCompanyById(car.get()
                    .getCompanyId()).get().getName()));
            printCustomerMenu(customer);
        } else {
            System.out.println("You didn't rent a car!");
            printCustomerMenu(customer);
        }
    }

    private void returnRentedCar(Customer customer) {
        Optional<Car> car = customerDao.getCustomerCar(customer);
        if (car.isPresent()) {
            customerDao.removeRentCar(customer);
            System.out.println("You've returned a rented car!");
            printCustomerMenu(customer);
        } else {
            System.out.println("You didn't rent a car!");
            printCustomerMenu(customer);
        }
    }

    private void rentCar(Customer customer) {
        Optional<Car> car = customerDao.getCustomerCar(customer);
        if (car.isPresent()) {
            System.out.println("You've already rented a car!");
            printCustomerMenu(customer);
        } else {
            List<Company> companyList = companyDao.getAllCompanies();
            if (companyList.isEmpty()) {
                System.out.println("The company list is empty!");
                printCustomerMenu(customer);
            } else {
                System.out.println("Choose a company: ");
                for (int i = 0; i < companyList.size(); i++) {
                    System.out.println(i + 1 + ". " + companyList.get(i).getName());
                }
                System.out.println("0. Back");
                try {
                    int companyId = Integer.parseInt(new Scanner(System.in).reset().nextLine());
                    if (companyId == 0) {
                        printCustomerMenu(customer);
                    }
                    showCompanyCar(companyList.get(companyId - 1), customer);
                } catch (NumberFormatException e) {
                    showCompanyList();
                }
            }
        }
    }

    private void showCompanyCar(Company company, Customer customer) {
        List<Car> carList = companyDao.getCompanyCars(company);
        if (carList.isEmpty()) {
            System.out.printf("No available cars in the '%s' company\n", company.getName());
            printCustomerMenu(customer);
        } else {
            System.out.println("Choose a car:");
            for (int i = 0; i < carList.size(); i++) {
                System.out.println(i + 1 + ". " + carList.get(i).getName());
            }
            System.out.println("0. Back\n");

            int carSelected = Integer.parseInt(new Scanner(System.in).nextLine());
            if (carSelected == 0) {
                printCustomerMenu(customer);

            } else {
                carSelected = carSelected - 1;
                customer.setCarId(carList.get(carSelected).getId());
                customerDao.addCar(customer);
                System.out.printf("You rented '%s'\n", carList.get(carSelected).getName());
                printCustomerMenu(customer);
            }
        }
    }

    private void logInAsManager() {
        System.out.println("1. Company list\n" +
                "2. Create a company\n" +
                "0. Back");
        int optionSelected = Integer.parseInt(new Scanner(System.in).nextLine());

        switch (optionSelected) {
            case 1: {
                showCompanyList();
                break;
            }
            case 2: {
                createCompany();
                break;
            }
            case 0: {
                mainFlow();
                break;
            }
            default: {
                System.out.println("incorrect option");
            }
        }
    }

    private void showCompanyList() {
        List<Company> companyList = companyDao.getAllCompanies();
        if (companyList.isEmpty()) {
            System.out.println("The company list is empty!");
            logInAsManager();
        } else {
            System.out.println("Choose a company: ");
            for (int i = 0; i < companyList.size(); i++) {
                System.out.println(i + 1 + ". " + companyList.get(i).getName());
            }
            System.out.println("0. Back");
            try {
                int companyId = Integer.parseInt(new Scanner(System.in).reset().nextLine());
                if (companyId == 0) {
                    logInAsManager();
                }
                showCompanyInfo(companyList.get(companyId - 1));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                showCompanyList();
            }
        }
    }

    private void showCompanyInfo(Company company) {
//        Optional<Company> optionalCompany = companyDao.getCompanyById(id);
//        if (optionalCompany.isEmpty()) {
//            showCompanyList();
//        }
//        Company company = optionalCompany.get();
        System.out.println(String.format("'%s' company:\n" +
                "1. Car list\n" +
                "2. Create a car\n" +
                "0. Back", company.getName()));
        int option = Integer.parseInt(new Scanner(System.in).nextLine());
        switch (option) {
            case 1: {
                showCarList(company);
                break;
            }
            case 2: {
                Car car = new Car();
                car.setCompanyId(company.getId());
                createCar(car);
            }
            case 0: {
                logInAsManager();
                break;
            }
            default: {
                System.out.println("incorrect option!");
            }
        }
    }

    private void showCarList(Company company) {
        List<Car> cars = carDao.findCarsRelatedToCompany(company.getId());
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");

        } else {
            System.out.println("Car list:");
            for (int i = 0; i < cars.size(); i++) {
                System.out.println(i + 1 + ". " + cars.get(i).getName());
            }
        }
        showCompanyInfo(company);
    }

    private void createCompany() {
        System.out.println("Enter the company name:");
        String companyName = new Scanner(System.in).nextLine();
        if (companyDao.createCompany(new Company(companyName))) {
            System.out.println("The company was created!");
        }
        logInAsManager();
    }

    private void createCar(Car car) {
        System.out.println("Enter the car name:");
        String carName = new Scanner(System.in).nextLine();
        car.setName(carName);
        carDao.createCar(car);
        System.out.println("The car was added!");
        showCompanyInfo(companyDao.getCompanyById(car.getCompanyId()).orElseThrow());
    }


}
