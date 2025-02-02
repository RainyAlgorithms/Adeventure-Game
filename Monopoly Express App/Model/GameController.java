package Model;

import Trolls.*;

import java.util.ArrayList;
import java.util.Scanner;

public class GameController {
    public GameQueue queue;
    public Board board;
    public Player currentPlayer;
    public Die die;
    public Player winner;
    private ArrayList<Player> players;

    public GameController() {
        players = new ArrayList<>();
        board = new Board();
        System.out.println("Welcome to the game of Monopoly Express.");
        System.out.println("Who's playing the game? Enter all names using separator ','");

        Scanner scanner = new Scanner(System.in);
        String[] playerNames = scanner.nextLine().split(",");

        this.queue = new GameQueue();
        // Initialize players
        for (String playerName : playerNames) {
            Bank bank = new Bank(1000.0); // Set the balance to 1000
            Square position = new Square(0);
            Player player = new Player(playerName, position, bank);
            queue.enqueue(player);
            players.add(player);
        }

        // Set the currentPlayer to the first player created
        currentPlayer = this.queue.getNextPlayer();

        // Initialize squares and game objects
        String[] gameObjNames = {"Basketball", "Book", "Bread", "Chest", "Cloak", "Lamp", "Map", "Shoe", "Swan", "Teacup"};
        double[] gameObjPrices = {150, 200, 123, 140, 180, 250, 300, 100, 50, 1000};
        int[] gameObjSquares = {1, 2, 3, 5, 6, 8, 9, 10, 11, 13};
        int[] trollSquares = {4, 7, 12, 15};

        for (int i = 1; i <= 15; i++) {
            Square square = new Square(i);

            // Add game objects to certain squares
            for (int j = 0; j < gameObjNames.length; j++) {
                if (i == gameObjSquares[j]) {
                    GameObj gameObj = new GameObj(gameObjNames[j], square, gameObjPrices[j]);
                    square.addObj(gameObj);
                    break;
                }
            }

            // Add trolls to certain squares
            for (int trollSquare : trollSquares){
                if (i == trollSquare)
                {
                    Troll troll;
                    if (i == 4) {
                        troll = new NumberTroll();
                    } else if (i == 7) {
                        troll = new Rock_Paper();
                    } else if (i == 12) {
                        troll = new Tic_Tac_Toe();
                    } else {
                        troll = new Riddle();
                    }
                    square.setObstacle(troll);
                }
            }

            board.squares.add(square);
        }

        // Initialize die
        this.die = new Die();

        // Set winner to null
        this.winner = null;
    }

    /**
     * Move the targeted player(current player) to the designated square.
     * @param square destination Square of player's roll and move
     * @return true iff the player was moved successfully.
     */
    public void movePlayer(Square square) {
        if(square != null)
        {
            currentPlayer.getPosition().remove_player(currentPlayer);
            square.addPlayer(currentPlayer);
            currentPlayer.position = square;
        }

        if (currentPlayer.getPosition().getObj() != null)
        {
            System.out.println("There is an object in this room: " + currentPlayer.getPosition().getObj().getName());
            System.out.println("Enter buy. Price: $" + currentPlayer.getPosition().getObj().getPrice());
        }
    }

    public void quit_play(){
        System.out.println(currentPlayer.getName() + " has quit.");
        currentPlayer.getPosition().remove_player(currentPlayer);
        Player curr = currentPlayer;
        if (queue.size() == 1)
        {
            end_game();
        }
        currentPlayer = queue.getNextPlayer();
        queue.remove(curr);
    }

    public void removePlayer(){
        Player curr = currentPlayer;
        currentPlayer = queue.getNextPlayer();
        queue.remove(curr);
    }

    public String toss_dice(){
        System.out.print(currentPlayer.getName() + " tosses the die...");
        int num = die.toss();
        System.out.println("and got a " + num);
        move_player(num);

        // check if obstacle in room
        if (getPlayer().getPosition().hasObstacle())
        {
            return "TROLL";
        }
        return null;
    }

    public void playTroll()
    {
        System.out.println(currentPlayer.getName() + " has encountered a Troll!");
        if (!currentPlayer.getPosition().getObstacle().playGame()) {//player fails the mini-game
            System.out.println("Oh no, " + currentPlayer.getName() + " has died to the troll!");
            quit_play();
        } else { // player wins the mini-game
            System.out.println("Yes! " + currentPlayer.getName() + " has beat the troll!");
            System.out.println("Game Continued.");
            setWinner();
            move_player(1);
            currentPlayer = this.queue.getNextPlayer();
        }
    }

    public void setWinner()
    {
        if (currentPlayer.getPosition().equals(board.squares.get(14)))
        { // reaches the end
            System.out.println("Congratulations! " + currentPlayer.getName() + " has reached the end!");
            if (winner == null) {//first one reaches the end
                winner = currentPlayer;
                removePlayer();
            } else if (winner.getBank().getBalance() < currentPlayer.getBank().getBalance()) { // new leading
                winner = currentPlayer;
                removePlayer();
            }
        }
    }

    public void end_game(){
        System.out.println("Game over!");
        if (winner != null){
            System.out.println("The winner is " + winner.getName());
        }else if (this.queue.isEmpty()){
            System.out.println("There's no winner, all players have died or quit.");
        }
    }

    public void buy_object(GameObj obj){
        currentPlayer.buy(obj);
    }

    public void move_player(int num){
        System.out.println(currentPlayer.getName() + " now move forward " + num + " steps.");
        Square destinated_square = board.showMove(currentPlayer.getPosition(), num);

        movePlayer(destinated_square);
    }

    public String GamePlay(String text) {
        if (getPlayer().getPosition().getObj() != null && !text.equalsIgnoreCase("buy"))
        {
            currentPlayer = this.queue.getNextPlayer();
            return null;
        }
        if (text.equalsIgnoreCase("die") || text.equalsIgnoreCase("roll"))
        { // die
            return toss_dice();
        }
        if (text.equalsIgnoreCase("buy")) {
            System.out.println(currentPlayer.getPosition().getObj());
            System.out.println(currentPlayer.getPosition().get_room_No());
            System.out.println(currentPlayer.getName());
            System.out.println(currentPlayer.getName() + " has bought " + currentPlayer.getPosition().getObj().getName() + ".");
            buy_object(currentPlayer.getPosition().getObj());
            currentPlayer = this.queue.getNextPlayer();
            return null;
        }
        if (text.equalsIgnoreCase("Play"))
        { // die
            playTroll();
            return null;
        }

        if(queue.isEmpty())
        {
            end_game();
            return "GAME OVER";
        }
        if(queue.size() == 1)
        {
            winner = queue.getNextPlayer();
            end_game();
            return "GAME OVER";
        }
        currentPlayer = this.queue.getNextPlayer();
        return null;
    }


    public Player getPlayer()
    {
        return this.currentPlayer;
    }

    public ArrayList<Player> getAllPlayers()
    {
        return this.players;
    }

    public String getInstructions(){
        return "There are FOUR players in the game. The objective of the game is to get to the last square of the board\n" +
                "and pick up and buy as many objects as you can.\n\n Players may click on the objects under ObjectsInSquare, or enter 'buy' to buy object.\n\n" +
                "Players advance based on their roll on the DIE. Player may click on the DIE icon, enter 'roll', \n" +
                "or enter 'die' to roll the die. Players may only buy objects that are on the squares they landed on. \n\n" +
                "Note that Players may quit the game at anytime by clicking the QUIT button or by entering 'quit', but the game will continue without them."+
                "\n\nPlayer may click on any square to know more about that square."+
                "\n\nThe game end when all players have QUIT or all players got to the finish line, the last square on the board\n\n"+
                "After the game ends, the player with the most values objects wins the game.\n"+
                "That is, the player does not win if they have more objects, but if all their objects combined are worth more.\n\n"+
                "In the case that players have tied, the first of them who reached the last square wins.\n\n" +
        "ENJOY!!";
    }
}
