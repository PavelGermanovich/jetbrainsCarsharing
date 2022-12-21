package org.jetbrains.carsharing.model;

public class Company {
    private int id;
    private String name;

    public Company() {
    }

    public Company(int id, String companyName) {
        this.id = id;
        this.name = companyName;
    }

    public Company(String companyName) {
        this.name = companyName;
    }

    @Override
    public String toString() {
        return id + ". " + name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
