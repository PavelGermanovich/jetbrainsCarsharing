package org.jetbrains.carsharing;

import org.jetbrains.carsharing.dao.CarDaoImpl;
import org.jetbrains.carsharing.dao.CompanyDaoImpl;
import org.jetbrains.carsharing.dao.CustomerDaoImpl;
import org.jetbrains.carsharing.dao.Menu;
import org.jetbrains.carsharing.util.Database;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        String dbName = "default";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-databaseFileName")) {
                dbName = args[i+1];
                break;
            }
        }

        Database database = Database.createDatabase(dbName);
        database.dropTable("CUSTOMER");

        database.dropTable("CAR");
        database.dropTable("COMPANY");


        database.createCOMPANYTable();
        database.createCarTable();
        database.createCustomerTable();

        Menu menu = new Menu(new CompanyDaoImpl(database), new CarDaoImpl(database), new CustomerDaoImpl(database));
        menu.mainFlow();
        database.closeDB();
    }
}