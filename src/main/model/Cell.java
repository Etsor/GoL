package main.model;
import java.io.Serializable;

public class Cell implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private boolean alive;
    private int age;

    public Cell() {
        this.alive = false;
        this.age = 0;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void toggle() {
        alive = !alive;
    }

    public void incrementAge() {
        this.age++;
    }

    public void resetAge() {
        age = 0;
    }
}
