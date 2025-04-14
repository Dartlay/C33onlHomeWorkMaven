package org.example.pattern;


public class PatternsDemonstrator {
    public void demonstrateAllPatterns() {
        System.out.println("=== Демонстрация паттернов проектирования ===");

        demonstrateSingleton();
        demonstrateAbstractFactory();
        demonstrateFactoryMethod();
        demonstrateBuilder();
        demonstratePrototype();
        demonstrateProxy();
    }

    private void demonstrateSingleton() {
        System.out.println("\n--- 1. Singleton ---");
        Singleton instance1 = Singleton.getInstance();
        Singleton instance2 = Singleton.getInstance();

        instance1.showMessage();
        System.out.println("instance1 == instance2: " + (instance1 == instance2));
    }

    private void demonstrateAbstractFactory() {
        System.out.println("\n--- 2. Abstract Factory ---");

        System.out.println("\nWindows GUI:");
        Application windowsApp = new Application(new WindowsFactory());
        windowsApp.render();

        System.out.println("\nmacOS GUI:");
        Application macApp = new Application(new MacOSFactory());
        macApp.render();
    }

    private void demonstrateFactoryMethod() {
        System.out.println("\n--- 3. Factory Method ---");

        Logistics roadLogistics = new RoadLogistics();
        roadLogistics.planDelivery();

        Logistics seaLogistics = new SeaLogistics();
        seaLogistics.planDelivery();
    }

    private void demonstrateBuilder() {
        System.out.println("\n--- 4. Builder ---");

        Director director = new Director();
        CarBuilder builder = new CarBuilder("Sports Car");

        director.constructSportsCar(builder);
        Car sportsCar = builder.getResult();
        System.out.println(sportsCar);

        builder = new CarBuilder("SUV");
        director.constructSUV(builder);
        Car suv = builder.getResult();
        System.out.println(suv);
    }

    private void demonstratePrototype() {
        System.out.println("\n--- 5. Prototype ---");

        try {
            Zoo zoo = new Zoo();

            Animal dog1 = zoo.createAnimal("dog");
            Animal dog2 = zoo.createAnimal("dog");
            Animal cat = zoo.createAnimal("cat");

            System.out.println("\nAnimals sounds:");
            dog1.makeSound();
            dog2.makeSound();
            cat.makeSound();

            System.out.println("dog1 == dog2: " + (dog1 == dog2));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    private void demonstrateProxy() {
        System.out.println("\n--- 6. Proxy ---");

        Proxy lionForGuest = new AnimalProxy("Lion", "guest");
        Proxy lionForAdmin = new AnimalProxy("Lion", "admin");
        Proxy tigerForAdmin = new AnimalProxy("Tiger", "admin");

        System.out.println("\nGuest tries to view Lion:");
        lionForGuest.display();

        System.out.println("\nAdmin views Lion:");
        lionForAdmin.display();

        System.out.println("\nAdmin views Tiger:");
        tigerForAdmin.display();
    }
}