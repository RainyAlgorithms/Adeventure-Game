package Model;

import java.util.ArrayList;

import Trolls.*;

public class Square {
    public int number;
    public Troll obstacle;
    public boolean obstacle_exist;
    public GameObj objectInRoom;
    public ArrayList<Player> players;
    /**
     * square of the map
     *
     * @param number: the number of the square
     */
    public Square(int number){
        this.number = number;
        this.players = new ArrayList<>();
        this.objectInRoom = null;
        this.obstacle_exist = false;
    }
    /**
     * Troll in square
     *
     * @return Boolean: if obstacle exist than call troll to player with player return true if win or tie, false if lost, else return null
     */
    public Troll getObstacle(){
        return this.obstacle;
    }
    /**
     * add object
     *
     * @param x: obj you want to add in Objects
     */
    public void addObj(GameObj x){
        this.objectInRoom = x;
    }
    /**
     * add player
     *
     * @param player: player you want to add in Player
     */
    public void addPlayer(Player player){
        this.players.add(player);
    }
    /**
     * drop object
     *
     */
    public void pick_up_object()
    {
        this.objectInRoom = null;}
    /**
     * remove player form the square
     *
     * @param x: player you want to remove from square
     */
    public void remove_player(Player x){this.players.remove(x);}
    /**
     * get room number
     *
     * @return room number
     */
    public Integer get_room_No(){
        return this.number;
    }
    /**
     * get Objects
     *
     * @return Objects
     */
    public GameObj getObj(){
        return objectInRoom;
    }
    /**
     * get Player
     *
     * @return Player
     */
    public ArrayList<Player> getPlayers(){
        return players;
    }
    /**
     * return if troll exist or not
     *
     * @return Boolean
     */
    public Boolean hasObstacle(){
        return obstacle_exist;
    }

    /**
     * set Obstacle to <obstacle></>
     *
     */
    public void setObstacle(Troll obstacle)
    {
        this.obstacle = obstacle;
        this.obstacle_exist = true;
    }
}
