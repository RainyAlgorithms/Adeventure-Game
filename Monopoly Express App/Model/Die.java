package Model;

import java.util.Random;

public class Die {
    private int faces;
    private Random random;

    public Die() {
        this.faces = 4;
        this.random = new Random();
    }

    public int toss() {
        return random.nextInt(faces) + 1;
    }
}
