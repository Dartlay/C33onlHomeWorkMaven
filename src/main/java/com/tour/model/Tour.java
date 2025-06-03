package com.tour.model;

public class Tour {
    private String name;
    private double price;

    public Tour() {

    }

    public Tour(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
