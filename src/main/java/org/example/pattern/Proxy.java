package org.example.pattern;


interface Proxy {
    void display();
}


class RealAnimal implements Proxy {
    private String name;

    public RealAnimal(String name) {
        this.name = name;
        loadFromDatabase();
    }

    private void loadFromDatabase() {
        System.out.println("Loading " + name + " from database...");
    }

    @Override
    public void display() {
        System.out.println("Displaying " + name);
    }
}


class AnimalProxy implements Proxy {
    private RealAnimal realAnimal;
    private String name;
    private String role;

    public AnimalProxy(String name, String role) {
        this.name = name;
        this.role = role;
    }

    @Override
    public void display() {
        if (realAnimal == null) {
            realAnimal = new RealAnimal(name);
        }

        if ("admin".equals(role)) {
            realAnimal.display();
        } else {
            System.out.println("Access denied for " + name + ". Only admins can view animals.");
        }
    }
}


class Zooo {
    public static void main(String[] args) {
        Proxy lion = new AnimalProxy("Lion", "guest");
        Proxy tiger = new AnimalProxy("Tiger", "admin");

        lion.display();
        tiger.display();
    }
}