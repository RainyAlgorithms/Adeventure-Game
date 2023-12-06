package Trolls;

import java.util.Random;
import java.util.Scanner;
public class Riddle implements Troll{
    /**
     * riddles and answers
     */
    static String Riddle_1 = "I feel your every move. I know your every thought. I'm with you from birth, and I'll see you when you rot.";
    static String Answer_1 = "reflection";
    static String Riddle_2 = "What is always on time but never arrives?";
    static String Answer_2 = "tomorrow";
    static String Riddle_3 = "Hope shines brightly in a city this dark, find the source of the signal, and you'll soon hit your mark.";
    static String Answer_3 = "bat signal";
    static String Riddle_4 = "I can be cracked. I can be made. I can be told. I can be played. What am I?";
    static String Answer_4 = "joke";
    static String Riddle_5 = "I can start a war or end one. I can give you the strength of heroes or leave you powerless. I might be snared with a glance, but no force can compel me to stay. What am I?";
    static String Answer_5 = "love";
    static String Riddle_6 = "What is the beginning of eternity, the end of time and space, the beginning of every end, and the end of every race?";
    static String Answer_6 = "e";
    static String Riddle_7 = "13, 15, & 8";
    static String Answer_7 = "mre";

    /**
     * generate a random number from 1-7 that 3 and 7 will have only 10% 1,2,4,5,6 will share the 90%
     * @return the random number
     */
    private static Integer guess_witch_one_it_is() {
        Random r = new Random();
        int[] weights = {15, 15, 7, 15, 15, 15, 3};
        int totalWeight = 0;
        for (int weight : weights) {
            totalWeight += weight;
        }
        int randomNumber = r.nextInt(totalWeight);
        int cumulativeWeight = 0;
        int chosenOption = -1;

        for (int i = 0; i < weights.length; i++) {
            cumulativeWeight += weights[i];
            if (randomNumber < cumulativeWeight) {
                chosenOption = i + 1; // Options are 1-indexed
                break;
            }
        }
        return chosenOption;
    }

    public String giveInstructions()
    {
        return "Ah, greetings, dear interlocutors! \n" +
                "Prepare your intellect for a delightful dance with enigma. \n" +
                "Behold, as the Riddler unveils mysteries woven with the threads of complexity. \n" +
                "What has keys but can't open locks? \n" +
                "Riddle me this, and perhaps you shall glimpse the brilliance that dances within the shadows of my mind! \n" +
                "But beware, mere mortals, for you have only three chances to unravel the strands of my puzzle and bask in the sweet taste of victory!";
    }

    /**
     * play the troll
     * @return true if player win, false if lost
     */
    public boolean playGame(){
        Integer x = guess_witch_one_it_is();

        System.out.println("");
        String y = "1145141919";
        if(x==1){
            System.out.println(Riddle_1);
            y = Answer_1;
        }else if(x==2){
            System.out.println(Riddle_2);
            y = Answer_2;
        }else if(x==3){
            System.out.println(Riddle_3);
            y = Answer_3;
        }else if(x==4){
            System.out.println(Riddle_4);
            y = Answer_4;
        }else if(x==5){
            System.out.println(Riddle_5);
            y = Answer_5;
        }else if(x==6){
            System.out.println(Riddle_6);
            y = Answer_6;
        }else if(x==7){
            System.out.println(Riddle_7);
            y = Answer_7;
        }
        Scanner scanner = new Scanner(System.in);
        for(int i = 0; i<3; i++){
            String input = scanner.nextLine();
            String lowercaseInput = input.toLowerCase();
            if (lowercaseInput.matches(".*\\b" + y + "\\b.*")){
                System.out.println("Congratulations, clever soul! \n" +
                        "You've cracked the code and danced through the corridors of my riddles. \n" +
                        "A triumph in the face of complexity! \n" +
                        "Until our next cerebral rendezvous, revel in the sweet taste of victory, for you have outwitted the Riddler himself!");
                return true;
            }
        }
        System.out.println("Ah, a valiant effort, but the web of my riddles proves too intricate for your mortal mind. \n" +
                "The answer slips through your grasp like elusive shadows. \n" +
                "Fear not, for in failure, there is a lesson. The enigma endures, and with it, the dance of challenges. \n" +
                "Two chances remain; will you rise from the shadows or succumb to the mysteries that linger in the air?");
        return false;
    }
    public static void main(String [] args) throws InterruptedException {
        Riddle s = new Riddle();
        boolean a = s.playGame();
    }
}
