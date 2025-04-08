package org.example.SRP;

public class SRPTrue {
    private String name;
    private String position;

    public SRPTrue(String name, String position) {
        this.name = name;
        this.position = position;
    }


    public String getName() {
        return name;
    }
}

class SRPTrueRepository {
    public void saveToDatabase(SRPTrue srrTrue) {
        System.out.println("Сохранение сотрудника в БД: " + srrTrue.getName());
    }
}