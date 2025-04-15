package org.example.facade;


class CPU {
    public void start() {
        System.out.println("CPU is starting...");
    }
}

class Memory {
    public void load() {
        System.out.println("Memory is loading data...");
    }
}

class HardDrive {
    public void read() {
        System.out.println("HardDrive is reading data...");
    }
}


class ComputerFacade {
    private CPU cpu;
    private Memory memory;
    private HardDrive hardDrive;

    public ComputerFacade() {
        this.cpu = new CPU();
        this.memory = new Memory();
        this.hardDrive = new HardDrive();
    }

    public void startComputer() {
        cpu.start();
        memory.load();
        hardDrive.read();
        System.out.println("Computer started successfully!");
    }
}


public class FacadeDemo {
    public static void main(String[] args) {
        ComputerFacade computer = new ComputerFacade();
        computer.startComputer();
    }
}