package Model;

import java.util.ArrayList;

public class GameQueue
{
    // Attribute
    private ArrayList<Player> elements;

    // Constructor
    public GameQueue()
    {
        elements = new ArrayList<>();
    }

    // Method to add a player to the queue
    public void enqueue(Player player)
    {
        elements.add(player);
    }

    // Method to remove a player from the queue
    public void remove(Player player)
    {
        int index = elements.indexOf(player);
        if (index != -1)
        {
            elements.remove(index);
        }
    }

    // Method to check if the queue is empty
    public boolean isEmpty()
    {
        return elements.isEmpty();
    }

    // Method to get the size of the queue
    public int size()
    {
        return elements.size();
    }

    // Method to get the next player in the queue
    public Player getNextPlayer()
    {
        if (isEmpty())
        {
            return null;
        }
        else
        {
            Player player = elements.get(0);
            elements.remove(0);
            elements.add(player);
            return player;
        }
    }
}
