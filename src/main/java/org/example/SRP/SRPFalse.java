package org.example.SRP;

public class SRPFalse {
    private String name;
    private String position;

    public SRPFalse(String name, String position) {
        this.name = name;
        this.position = position;
    }


    public void saveToDatabase() {
        System.out.println("Сохранение сотрудника в БД: " + name);
    }
}