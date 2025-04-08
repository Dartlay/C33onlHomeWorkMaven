package org.example.DIP;

class LightBulb {
    public void turnOn() {
        System.out.println("Лампочка включена");
    }
}

class Switch {
    private LightBulb bulb;

    public Switch(LightBulb bulb) {
        this.bulb = bulb;
    }

    public void operate() {
        bulb.turnOn();
    }
}
