package Model;

import java.util.ArrayList;

public class Player {
    public String name;
    public ArrayList<GameObj> inventory;
    public Square position;
    public Bank bank;

    public Player(String name, Square position, Bank bank)
    {
        this.name = name;
        this.inventory = new ArrayList<>();
        this.position = position;
        this.bank = bank;
    }

    public String getName()
    {
        return this.name;
    }

    public ArrayList<GameObj> getInventory()
    {
        return this.inventory;
    }

    public Square getPosition() { return this.position; }

    public Bank getBank(){ return this.bank; }

    public void buy(GameObj obj)
    {
        if (this.bank.buy(obj.getPrice()))
        {
            this.inventory.add(obj);
            this.position.pick_up_object();
        }
        else
        {
            System.out.println("Insufficient funds to buy " + obj.getName());
        }
    }
}