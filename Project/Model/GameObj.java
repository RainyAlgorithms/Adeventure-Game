package Model;

public class GameObj {
    private String name;
    private Square place;
    private double price;

    public GameObj(String name, Square place, double price) {
        this.name = name;
        this.place = place;
        this.price = price;
    }

    public double getPrice()
    {
        return this.price;
    }

    public Square getPlace()
    {
        return this.place;
    }
    public String getName()
    {
        return this.name;
    }
}