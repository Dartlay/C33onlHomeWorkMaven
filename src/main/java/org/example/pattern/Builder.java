package org.example.pattern;



class Car {
    private String type;
    private int seats;
    private String engine;
    private String transmission;
    private String tripComputer;
    private String gpsNavigator;

    public Car(String type) {
        this.type = type;
    }


    public void setSeats(int seats) { this.seats = seats; }
    public void setEngine(String engine) { this.engine = engine; }
    public void setTransmission(String transmission) { this.transmission = transmission; }
    public void setTripComputer(String tripComputer) { this.tripComputer = tripComputer; }
    public void setGpsNavigator(String gpsNavigator) { this.gpsNavigator = gpsNavigator; }

    @Override
    public String toString() {
        return String.format(
                "Car: %s, seats: %d, engine: %s, transmission: %s, trip computer: %s, GPS: %s",
                type, seats, engine, transmission, tripComputer, gpsNavigator
        );
    }
}


interface Builder {
    void setSeats(int seats);
    void setEngine(String engine);
    void setTransmission(String transmission);
    void setTripComputer(String tripComputer);
    void setGpsNavigator(String gpsNavigator);
}


class CarBuilder implements Builder {
    private Car car;

    public CarBuilder(String type) {
        this.car = new Car(type);
    }

    @Override
    public void setSeats(int seats) { car.setSeats(seats); }
    @Override
    public void setEngine(String engine) { car.setEngine(engine); }
    @Override
    public void setTransmission(String transmission) { car.setTransmission(transmission); }
    @Override
    public void setTripComputer(String tripComputer) { car.setTripComputer(tripComputer); }
    @Override
    public void setGpsNavigator(String gpsNavigator) { car.setGpsNavigator(gpsNavigator); }

    public Car getResult() {
        return car;
    }
}


class Director {
    public void constructSportsCar(Builder builder) {
        builder.setSeats(2);
        builder.setEngine("Sport Engine");
        builder.setTransmission("Semi-automatic");
        builder.setTripComputer("Sport Trip Computer");
        builder.setGpsNavigator("Sport GPS");
    }

    public void constructSUV(Builder builder) {
        builder.setSeats(4);
        builder.setEngine("SUV Engine");
        builder.setTransmission("Manual");
        builder.setGpsNavigator("Basic GPS");
    }
}