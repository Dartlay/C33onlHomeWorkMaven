package org.example.ISP;

interface Workable {
    void work();
}

interface Eatable {
    void eat();
}

interface Sleepable {
    void sleep();
}

class OfficeWorker2 implements Workable, Eatable  {
    @Override
    public void work() { /*...*/ }

    @Override
    public void eat() { /*...*/ }


}

class Robot2 implements Workable {
    @Override
    public void work() { /*...*/ }
}