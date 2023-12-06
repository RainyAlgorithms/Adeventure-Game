package Trolls;

import java.util.Random;
import java.util.Scanner;

    public class NumberTroll implements Troll{

        int the_number; //current tone
        Scanner scanner; //use to read input from user
        int min;
        int max;
        int max_attempts;


        /* Function: GameTroll Constructor
         * ----------------------
         * Make a GameTroll
         */
        public NumberTroll() {
            this.scanner = new Scanner(System.in);
            this.min = 1;
            this.max = 25;
            this.max_attempts = 5;
            // Using Math.random()
            // min = 1
            // max = 25
            // Math.floor(Math.random() * (max - min + 1) + min)
            this.the_number = (int)(Math.floor(Math.random() * (max - min + 1) + min));
        }

        /**
         * Print GameTroll instructions for the user
         */
        public String giveInstructions()
        {
            return "\n****************************************\n\n"+
                    "   Welcome to the GameTroll's Lair!\n\n"+
                    "****************************************\n\n"+
                    "The objective is to guess the correct number.\n"+
                    "I will pick a random number between 1 and 25 (inclusive), and you have to guess it.\n"+
                    "I will tell you if your guess is too high or too low.\n"+
                    "But there's a catch. You only get five tries.\n\nGood luck!\n\n"+
                    "****************************************\n";
        }

        /**
         * Play the GameTroll game
         *
         * @return true if player wins the game, else false
         */
        public boolean playGame() {

            int attempts = 0;

            while (attempts < max_attempts)
            {
                System.out.print("\nEnter your guess! Pick a number from 1 to 25: ");
                int guess = scanner.nextInt();

                if (guess == the_number)
                {
                    System.out.println("\nCongratulations! You guessed the correct number! You MAY PASS.\n");
                    return true;
                }

                else if (guess < the_number) {System.out.println("Sorry! Too LOW! Try again.");}
                else {System.out.println("Nope. Too HIGH! Try again.");}

                attempts++;
                System.out.println("You have " + (max_attempts - attempts) + " attempts left.");
            }

            System.out.println("\nSorry, you've run out of attempts. The correct number was: " + the_number);
            System.out.println("You've lost this game. You MAY NOT PASS!\n");
            return false;
        }

        /**
         * Main method, use for debugging
         *
         * @param args: Input arguments
         */
        public static void main(String [] args) throws InterruptedException {
            NumberTroll s = new NumberTroll();
            boolean a = s.playGame();
            System.out.println(a);
            s.scanner.close(); // Close the scanner
        }
    }

