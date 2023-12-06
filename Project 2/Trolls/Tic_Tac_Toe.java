package Trolls;

import java.util.Random;
import java.util.Scanner;

public class Tic_Tac_Toe implements Troll{
    /**
     * play the troll
     */
    public String giveInstructions()
    {
        return "Tic Tac Toe  played on a 3x3 grid. \n" +
                "You are X and computer is O in empty cells, aiming to create a row of 3 of their symbols either horizontally, vertically, or diagonally. \n" +
                "If you got into a tie or win against the computer, you win. \n" +
                "enter the number in each space to make a move";
    }
    public boolean playGame() {
        char[] board = {' ', ' ', ' ',' ', ' ', ' ',' ', ' ', ' '};
        System.out.println("[1] [2] [3]\n" +
                           "[4] [5] [6]\n" +
                           "[7] [8] [9]");
        char Player = 'X';

        boolean Win = false;
        int moves = 0;

        do {
            if (Player == 'X') {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Player " + Player + ", enter your move (number of the space): ");
                int x = scanner.nextInt() -1;


                if (0<=x && x<9 && board[x] == ' ') {
                    board[x] = Player;
                    Win = checkWin(board, Player);
                    Player = '0';
                    moves++;
                } else {
                    System.out.println("Invalid move.");
                }
            } else {
                Random random = new Random();
                int x;
                do {
                    x = random.nextInt(9);
                } while (!(board[x] == ' '));
                System.out.println("Computer chooses " + x);
                board[x] = '0';
                Win = checkWin(board, Player);
                Player = 'X';
                moves++;
            }
            for (int i = 0; i < 9; i+=3) {
                System.out.print(" [ "+ board[i] + " ] "+" [ "+ board[i+1] + " ] "+" [ "+ board[i+2] + " ] ");
                System.out.println();
            }
        }while (!Win && moves < 9);
        return !Win || moves == 9;
    }


    /**
     * check if player win or lost or tie
     * @param board: check the board
     * @param Player: check the player that make the move win
     */
    private boolean checkWin(char[] board, char Player) {

        if ((board[0] == Player && board[1] == Player && board[2] == Player) ||
            (board[3] == Player && board[4] == Player && board[5] == Player) ||
            (board[6] == Player && board[7] == Player && board[8] == Player) ||
            (board[0] == Player && board[3] == Player && board[6] == Player) ||
            (board[1] == Player && board[4] == Player && board[7] == Player) ||
            (board[2] == Player && board[5] == Player && board[8] == Player) ||
            (board[0] == Player && board[4] == Player && board[8] == Player) ||
            (board[2] == Player && board[4] == Player && board[6] == Player)   ) {
                return true;
            }

        return false;
    }
    /**
     * Main method, use for debugging
     *
     * @param args: Input arguments
     */
    public static void main(String[] args) throws InterruptedException{
        Tic_Tac_Toe s = new Tic_Tac_Toe();
        boolean a = s.playGame();
    }
}
