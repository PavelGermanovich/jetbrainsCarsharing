package org.jetbrains.carsharing.util;

import java.sql.*;

public class Database {
    public static Database database = null;
    static final String JDBC_DRIVER = "org.h2.Driver";
    private String DB_URL = "jdbc:h2:file:../task/src/carsharing/db/";
    private final Connection connection;


    public static Database createDatabase(String dbName) {
        if (database == null) {
            database = new Database(dbName);
        }
        return database;
    }

    private Database(String dbName) {
        DB_URL = DB_URL + dbName;
        connection = getConnection();
    }

    public Connection getConnection() {
        Connection con = null;
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DB_URL);
            con.setAutoCommit(true);

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return con;
    }


    public void closeDB() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    public void createCOMPANYTable() {
        try (Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS COMPANY (\n"
                    + "     ID INTEGER auto_increment PRIMARY KEY,\n"
                    + "     NAME VARCHAR(24) UNIQUE NOT NULL"
                    + ");";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createCarTable() {
        try (Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS CAR (\n"
                    + "     ID INTEGER auto_increment PRIMARY KEY,\n"
                    + "     NAME VARCHAR(24) UNIQUE NOT NULL,\n"
                    + "COMPANY_ID INT NOT NULL\n,"
                    + "CONSTRAINT company_fk FOREIGN KEY (COMPANY_ID)\n"
                    + "REFERENCES COMPANY(ID)"
                    + ");";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createCustomerTable() {
        try (Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS CUSTOMER (\n"
                    + "     ID INTEGER auto_increment PRIMARY KEY,\n"
                    + "     NAME VARCHAR UNIQUE NOT NULL,\n"
                    + "RENTED_CAR_ID INT \n,"
                    + "CONSTRAINT car_fk FOREIGN KEY (RENTED_CAR_ID)\n"
                    + "REFERENCES CAR(ID)"
                    + ");";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropTable(String tableName) {
        try {
            PreparedStatement statement = connection.prepareStatement(String.format("DROP TABLE IF EXISTS %s;", tableName));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
