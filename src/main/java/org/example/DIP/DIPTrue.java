package org.example.DIP;

interface Switchable {
    void turnOn();
}

class LightBulb2 implements Switchable {
    @Override
    public void turnOn() {
        System.out.println("Лампочка включена");
    }
}

class Fan implements Switchable {
    @Override
    public void turnOn() {
        System.out.println("Вентилятор включен");
    }
}

class Switch2 {
    private Switchable device;

    public Switch2(Switchable device) {
        this.device = device;
    }

    public void operate() {
        device.turnOn();
    }
}