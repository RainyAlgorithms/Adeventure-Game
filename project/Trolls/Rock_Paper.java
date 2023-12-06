package Trolls;

import java.util.Random;
import java.util.Scanner;


public class Rock_Paper{
    /**
     * compute the player's input is Valid
     *
     * @return true if player input is Valid
     */
    private static boolean isValidChoice(String choice) {
        choice = choice.toUpperCase();
        return choice.equals("R") || choice.equals("P") || choice.equals("S");
    }
    /**
     * generate a random move
     *
     * @return random move
     */
    private static String generateTrollChoice(Random random) {
        int trollChoice = random.nextInt(3);
        String[] choices = {"R", "P", "S"};
        return choices[trollChoice];
    }
    /**
     * compute the player's move and random move
     *
     * @return true if player wins the game, else false
     */
    private static boolean didPlayerWin(String playerChoice, String trollChoice) {
        return (playerChoice.equals("R") && trollChoice.equals("S"))
                || (playerChoice.equals("P") && trollChoice.equals("R"))
                || (playerChoice.equals("S") && trollChoice.equals("P")
                || (playerChoice.equals(trollChoice)));
    }
    /**
     * Play the GameTroll game
     *
     * @return true if player wins the game, else false
     */
    public boolean playGame(){
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        giveInstructions();
        while (true) {

            String playerChoice = scanner.nextLine().toUpperCase();


            if (!isValidChoice(playerChoice)) {
                System.out.println("Invalid choice. Please enter rock, paper, or scissors.");
                continue;
            }

            String trollChoice = generateTrollChoice(random);

            System.out.println("Troll chose: " + trollChoice);

            if (didPlayerWin(playerChoice, trollChoice)) {
                System.out.println("You win!\n");
                return true;
            } else if (playerChoice.equals(trollChoice)) {
                System.out.println("It's a tie! Do it again\n");
            } else {
                System.out.println("You lose!\n");
                return false;
            }
        }
    }

    /**
     * Print GameTroll instructions for the user
     */
    public void giveInstructions()
    {
        System.out.print("I am a Game TROLL. You must beat me at my game to pass.\n");
        System.out.print("We play rock, paper, scissors\n");
        System.out.print("Enter your choice (R, P, S): \n>");
    }


    /**
     * Main method, use for debugging
     *
     * @param args: Input arguments
     */
    public static void main(String [] args) throws InterruptedException {
        Rock_Paper s = new Rock_Paper();
        boolean a = s.playGame();
    }
}
