package Model;

public class Bank {
    private double balance;

    public Bank(double balance)
    {
        this.balance = balance;
    }

    public double getBalance()
    {
        return this.balance;
    }

    public boolean buy(double price)
    {
        if (this.balance >= price)
        {
            this.balance -= price;
            return true;
        }
        else
        {
            return false;
        }
    }
}