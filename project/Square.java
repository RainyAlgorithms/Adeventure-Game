import java.util.ArrayList;
import Trolls.Rock_Paper;
import Trolls.Number;
import Trolls.Riddle;
import Trolls.Tic_Tac_Toe;
public class Square {
    Integer Number;
    Integer obstacle_number;
    boolean obstacle;
    ArrayList<Object> Objects = new ArrayList<>();
    ArrayList<Object> Player = new ArrayList<>();
    /**
     * square of the map
     *
     * @param No: the number of the square
     * @param ob; int 1-4 if obstacle exist, 1: Rock_Paper 2:Number 3:Riddle 4:Tic_Tac_Toe, else obstacle not exist
     */
    public Square(Integer No,ArrayList<Object> obj, Integer ob){
        this.Objects = obj;
        this.Number = No;
        if (ob != 1 && ob != 2 && ob != 3 && ob != 4){
            this.obstacle = true;
            this.obstacle_number = ob;
        }else{
            this.obstacle = false;
        }
    }
    /**
     * Troll in square
     *
     * @return Boolean: if obstacle exist than call troll to player with player return true if win or tie, false if lost, else return null
     */
    public Boolean get_Troll(){
        if (obstacle) {
            if (obstacle_number == 1) {
                Rock_Paper x = new Rock_Paper();
                return x.playGame();
            } else if (obstacle_number == 2) {
                Number x = new Number();
                return x.playGame();
            } else if (obstacle_number == 3) {
                Riddle x = new Riddle();
                return x.playGame();
            } else if (obstacle_number == 4) {
                Tic_Tac_Toe x = new Tic_Tac_Toe();
                return x.playGame();
            }
        }
        return null;
    }
    /**
     * add object
     *
     * @param x: obj you want to add in Objects
     */
    public void add_obj(Object x){
        this.Objects.add(x);
    }
    /**
     * add player
     *
     * @param x: player you want to add in Player
     */
    public void add_player(Object x){
        this.Player.add(x);
    }
    /**
     * drop object
     *
     * @param x: obj you want to remove from square
     */
    public void drop_obj(Object x){this.Objects.remove(x);}
    /**
     * remove player form the square
     *
     * @param x: player you want to remove from square
     */
    public void remove_player(Object x){this.Player.remove(x);}
    /**
     * get room number
     *
     * @return room number
     */
    public Integer get_room_No(){
        return this.Number;
    }
    /**
     * get Objects
     *
     * @return Objects
     */
    public ArrayList<Object> get_obj(){
        return Objects;
    }
    /**
     * get Player
     *
     * @return Player
     */
    public ArrayList<Object> get_Player(){
        return Player;
    }
    /**
     * return if troll exist or not
     *
     * @return obstacle
     */
    public Boolean Troll_exist(){
        return obstacle;
    }
}
