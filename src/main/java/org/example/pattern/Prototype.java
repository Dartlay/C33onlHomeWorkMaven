package org.example.pattern;

import java.util.HashMap;
import java.util.Map;




interface Animal extends Cloneable {
    Animal clone() throws CloneNotSupportedException;
    void makeSound();
}


class Dog implements Animal {
    @Override
    public Animal clone() throws CloneNotSupportedException {
        return (Dog) super.clone();
    }

    @Override
    public void makeSound() {
        System.out.println("Woof!");
    }
}

class Cat implements Animal {
    @Override
    public Animal clone() throws CloneNotSupportedException {
        return (Cat) super.clone();
    }

    @Override
    public void makeSound() {
        System.out.println("Meow!");
    }
}


class Zoo {
    private Map<String, Animal> prototypes = new HashMap<>();

    public Zoo() {
        prototypes.put("dog", new Dog());
        prototypes.put("cat", new Cat());
    }

    public Animal createAnimal(String type) throws CloneNotSupportedException {
        return prototypes.get(type).clone();
    }
}