package org.example.pattern;




abstract class Logistics {
    public void planDelivery() {
        Transport transport = createTransport();
        System.out.println("Planning delivery...");
        transport.deliver();
    }


    public abstract Transport createTransport();
}


class RoadLogistics extends Logistics {
    @Override
    public Transport createTransport() {
        return new Truck();
    }
}

class SeaLogistics extends Logistics {
    @Override
    public Transport createTransport() {
        return new Ship();
    }
}


interface Transport {
    void deliver();
}


class Truck implements Transport {
    @Override
    public void deliver() {
        System.out.println("Deliver by land in a box");
    }
}

class Ship implements Transport {
    @Override
    public void deliver() {
        System.out.println("Deliver by sea in a container");
    }
}