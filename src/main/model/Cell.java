package main.model;

import java.io.Serial;
import java.io.Serializable;

public class Cell implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    
    private boolean alive;
    private int age;

    public Cell() {
        this.alive = false;
        this.age = 0;
    }

    public boolean isAlive() {
        return this.alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void incrementAge() {
        this.age++;
    }

    public void resetAge() {
        this.age = 0;
    }
}
