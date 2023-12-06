package views;

import Trolls.*;
import Model.*;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.event.EventHandler;
import javafx.scene.AccessibleRole;

import javax.swing.*;
import java.io.File;
import java.util.Objects;

/**
 * Class .
 *
 * This is the Class that will visualize your model.
 * You are asked to demo your visualization via a Zoom
 * recording. Place a link to your recording below.
 *
 */
public class Visualizer {

    GameController gameControl; //model of the game
    Stage stage; //stage on which all is rendered
    Button quitButton, dieButton, helpButton, infoButton; //buttons
    Boolean helpToggle = false; //is help on display?
    Boolean squareToggle = false;

    Boolean infoToggle = false;

    GridPane gridPane = new GridPane(); //to hold images and buttons
    Label roomDescLabel = new Label(); //to hold room description and/or instructions
    VBox objectsInSquare = new VBox(); //to hold room items
    VBox objectsInInventory = new VBox();
    ImageView roomImageView; //to hold room image
    TextField inputTextField; //for user input

    private MediaPlayer mediaPlayer; //to play audio
    private boolean mediaPlaying; //to know if the audio is playing

    /**
     * Adventure Game View Constructor
     * __________________________
     * Initializes attributes
     */
    public Visualizer(GameController gameControl, Stage stage) {
        this.gameControl = gameControl;
        this.stage = stage;
        intiUI();
    }

    /**
     * Initialize the UI
     */
    public void intiUI() {

        // setting up the stage
        this.stage.setTitle("Monopoly Express");

        //Inventory + Room items
        objectsInInventory.setSpacing(10);
        objectsInInventory.setAlignment(Pos.TOP_CENTER);
        objectsInSquare.setSpacing(10);
        objectsInSquare.setAlignment(Pos.TOP_CENTER);

        // GridPane, anyone?
        gridPane.setPadding(new Insets(20));
        gridPane.setBackground(new Background(new BackgroundFill(
                Color.valueOf("#000000"),
                new CornerRadii(0),
                new Insets(0)
        )));

        //Three columns, three rows for the GridPane
        ColumnConstraints column1 = new ColumnConstraints(150);
        ColumnConstraints column2 = new ColumnConstraints(650);
        ColumnConstraints column3 = new ColumnConstraints(150);
        column3.setHgrow( Priority.SOMETIMES ); //let some columns grow to take any extra space
        column1.setHgrow( Priority.SOMETIMES );

        // Row constraints
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints( 550 );
        RowConstraints row3 = new RowConstraints();
        row1.setVgrow( Priority.SOMETIMES );
        row3.setVgrow( Priority.SOMETIMES );

        gridPane.getColumnConstraints().addAll( column1 , column2 , column1 );
        gridPane.getRowConstraints().addAll( row1 , row2 , row1 );

        // Buttons
        quitButton = new Button("Quit");
        quitButton.setId("Quit");
        customizeButton(quitButton, 100, 50);
        makeButtonAccessible(quitButton, "Quit Button", "This button quits the player with the current turn.", "This button quits the player with the current turn. This is the only player that will exit the game if clicked and the next player will start their turn.");
        addQuitEvent();

        dieButton = new Button("Die");
        dieButton.setId("Die");
        customizeButton(dieButton, 100, 50);
        makeButtonAccessible(dieButton, "Die Button", "This button rolls the die.", "This button rolls the die. Click it in order to land a number 1 - 4 and move that many number of squares.");
        addDiceEvent();

        helpButton = new Button("Help");
        helpButton.setId("Help");
        customizeButton(helpButton, 100, 50);
        makeButtonAccessible(helpButton, "Help Button", "This button gives game instructions.", "This button gives instructions on the game controls. Click it to learn how to play.");
        addInstructionEvent();

        infoButton = new Button("Info");
        infoButton.setId("Info");
        customizeButton(infoButton, 100, 50);
        makeButtonAccessible(infoButton, "Info Button", "This button gives game instructions.", "This button gives instructions on the game controls. Click it to learn how to play.");
        addInfoEvent();

        HBox topButtons = new HBox();
        topButtons.getChildren().addAll(dieButton, helpButton, quitButton, infoButton);
        topButtons.setSpacing(10);
        topButtons.setAlignment(Pos.CENTER);

        inputTextField = new TextField();
        inputTextField.setFont(new Font("Arial", 16));
        inputTextField.setFocusTraversable(true);

        inputTextField.setAccessibleRole(AccessibleRole.TEXT_AREA);
        inputTextField.setAccessibleRoleDescription("Text Entry Box");
        inputTextField.setAccessibleText("Enter commands in this box.");
        inputTextField.setAccessibleHelp("This is the area in which you can enter commands you would like to play.  Enter a command and hit return to continue.");
        addTextHandlingEvent(); //attach an event to this input field

        //labels for inventory and room items
        Label objLabel =  new Label("Objects in Square");
        objLabel.setAlignment(Pos.CENTER);
        objLabel.setStyle("-fx-text-fill: white;");
        objLabel.setFont(new Font("Arial", 16));

        Label invLabel =  new Label("Your Inventory");
        invLabel.setAlignment(Pos.CENTER);
        invLabel.setStyle("-fx-text-fill: white;");
        invLabel.setFont(new Font("Arial", 16));

        //add all the widgets to the GridPane
        gridPane.add( objLabel, 0, 0, 1, 1 );  // Add label
        gridPane.add( topButtons, 1, 0, 1, 1 );  // Add buttons
        gridPane.add( invLabel, 2, 0, 1, 1 );  // Add label

        Label commandLabel = new Label("What would you like to do?");
        commandLabel.setStyle("-fx-text-fill: white;");
        commandLabel.setFont(new Font("Arial", 16));

        updateScene(); //method displays an image and whatever text is supplied
        updateItems(); //update items shows inventory and objects in rooms

        // adding the text area and submit button to a VBox
        VBox textEntry = new VBox();
        textEntry.setStyle("-fx-background-color: #000000;");
        textEntry.setPadding(new Insets(20, 20, 20, 20));
        textEntry.getChildren().addAll(commandLabel, inputTextField);
        textEntry.setSpacing(10);
        textEntry.setAlignment(Pos.CENTER);
        gridPane.add( textEntry, 0, 2, 3, 1 );

        // Render everything
        var scene = new Scene( gridPane ,  1000, 800);
        scene.setFill(Color.BLACK);
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.show();
    }

    /**
     * makeButtonAccessible
     * __________________________
     * For information about ARIA standards, see
     * https://www.w3.org/WAI/standards-guidelines/aria/
     *
     * @param inputButton the button to add screenreader hooks to
     * @param name ARIA name
     * @param shortString ARIA accessible text
     * @param longString ARIA accessible help text
     */
    public static void makeButtonAccessible(Button inputButton, String name, String shortString, String longString) {
        inputButton.setAccessibleRole(AccessibleRole.BUTTON);
        inputButton.setAccessibleRoleDescription(name);
        inputButton.setAccessibleText(shortString);
        inputButton.setAccessibleHelp(longString);
        inputButton.setFocusTraversable(true);
    }

    /**
     * customizeButton
     * __________________________
     *
     * @param inputButton the button to make stylish :)
     * @param w width
     * @param h height
     */
    private void customizeButton(Button inputButton, int w, int h) {
        inputButton.setPrefSize(w, h);
        inputButton.setFont(new Font("Arial", 16));
        inputButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
    }

    /**
     * addTextHandlingEvent
     * __________________________
     * Add an event handler to the myTextField attribute
     *
     * Your event handler should respond when users
     * hits the ENTER or TAB KEY. If the user hits
     * the ENTER Key, strip white space from the
     * input to myTextField and pass the stripped
     * string to submitEvent for processing.
     *
     * If the user hits the TAB key, move the focus
     * of the scene onto any other node in the scene
     * graph by invoking requestFocus method.
     */
    private void addTextHandlingEvent() {
        inputTextField.addEventHandler(KeyEvent.KEY_PRESSED, event ->
        {
            if (event.getCode() == KeyCode.ENTER)
            {
                String input = inputTextField.getText().trim();
                submitEvent(input);
                inputTextField.clear();
            }
            else if (event.getCode() == KeyCode.TAB)
            {
                event.consume();
                if (helpButton.isDisabled())
                {
                    helpButton.setDisable(false);
                }
                helpButton.setVisible(true);
                helpButton.requestFocus();
            }
        });
    }


    /**
     * submitEvent
     * __________________________
     *
     * @param text the command that needs to be processed
     */
    private void submitEvent(String text) {

        text = text.strip(); //get rid of white space
        stopArticulation(); //if speaking, stop

        if (text.equalsIgnoreCase("HELP") || text.equalsIgnoreCase("H"))
        {
            showInstructions();
            return;
        }
        else if (text.equalsIgnoreCase("QUIT") || text.equalsIgnoreCase("Q"))
        {
            stopArticulation();
            gameControl.quit_play();
        }

        //try to move!
        String output = gameControl.GamePlay(text); //process the command!

        if (output == null)
        {
            updateScene();
            updateItems();
        }
        else if (output.equals("GAME OVER"))
        {
            updateScene();
            updateItems();
            PauseTransition pause = new PauseTransition(Duration.seconds(5));
            pause.setOnFinished(event -> {
                Platform.exit();
            });
            pause.play();
        }
        else if (output.equals("TROLL"))
        {
            updateScene();
            updateItems();
            new Thread(() -> gameControl.GamePlay("Play")).start();
//            gameControl.GamePlay("Play");
        }
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> {
            Platform.exit();
        });
        pause.play();
    }

    /**
     * updateScene
     * __________________________
     *
     * Show the current room, and print some text below it.
     * If the input parameter is not null, it will be displayed
     * below the image.
     * Otherwise, the current room description will be displayed
     * below the image.
     *currentPlayer = this.queue.getNextPlayer();
     */
    public void updateScene()
    {
        gridPane.getChildren().removeIf(node ->
                GridPane.getColumnIndex(node) == 1 && GridPane.getRowIndex(node) == 1
        );

        int squareNumber = this.gameControl.getPlayer().getPosition().get_room_No();

        if (squareNumber == 4 || squareNumber == 7 || squareNumber == 12 || squareNumber == 15)
        {
            getRoomImage(); //get the troll of the current room
        }
        else {
            // Create a GridPane for the buttons
            GridPane buttonGrid = new GridPane();
            buttonGrid.setHgap(10);
            buttonGrid.setVgap(10);

            // Creating 15 buttons

            Button[] buttons = new Button[15];
            for (int i = 0; i < 15; i++) {
                buttons[i] = new Button("Square " + (i + 1));
                buttons[i].setId("Square " + (i + 1));
                buttons[i].setMinWidth(50);
                buttons[i].setMinHeight(50);
                addSquareEvent(buttons[i]);
            }

            // Add the buttons to the GridPane in the desired layout
            buttonGrid.add(buttons[0], 0, 0);
            buttonGrid.add(buttons[1], 1, 0);
            buttonGrid.add(buttons[2], 2, 0);
            buttonGrid.add(buttons[3], 2, 1);
            buttonGrid.add(buttons[4], 2, 2);
            buttonGrid.add(buttons[5], 1, 2);
            buttonGrid.add(buttons[6], 0, 2);
            buttonGrid.add(buttons[7], 0, 3);
            buttonGrid.add(buttons[8], 0, 4);
            buttonGrid.add(buttons[9], 1, 4);
            buttonGrid.add(buttons[10], 2, 4);
            buttonGrid.add(buttons[11], 2, 5);
            buttonGrid.add(buttons[12], 2, 6);
            buttonGrid.add(buttons[13], 1, 6);
            buttonGrid.add(buttons[14], 0, 6);

            // Add the button grid to the middle column of the main grid
            gridPane.add(buttonGrid, 1, 1);

            buttonGrid.setAlignment(Pos.CENTER);
        }

    }

    /**
     * formatText
     * __________________________
     *
     * Format text for display.
     *
     * @param obstacle the text to be formatted for display.
     */
    private void formatText(Troll obstacle)
    {
        String textToDisplay = obstacle.giveInstructions() + "\n\n" + "Play in the Console.";
        roomDescLabel.setText(textToDisplay);
        roomDescLabel.setStyle("-fx-text-fill: white;");
        roomDescLabel.setFont(new Font("Arial", 16));
        roomDescLabel.setAlignment(Pos.CENTER);
        roomDescLabel.setPrefWidth(500);
        roomDescLabel.setPrefHeight(500);
        roomDescLabel.setTextOverrun(OverrunStyle.CLIP);
        roomDescLabel.setWrapText(true);
    }

    /**
     * getRoomImage
     * __________________________
     *
     * Get the image for the current room and place
     * it in the roomImageView
     */
    private void getRoomImage() {
        String roomImage = "troll-images/Troll_" + gameControl.getPlayer().getPosition().get_room_No() + ".png";

        Image roomImageFile = new Image(roomImage);
        roomImageView = new ImageView(roomImageFile);
        roomImageView.setPreserveRatio(true);
        roomImageView.setFitWidth(400);
        roomImageView.setFitHeight(400);

        //set accessible text
        roomImageView.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        roomImageView.setAccessibleText(gameControl.getPlayer().getPosition().getObstacle().giveInstructions() + "\n\n" + "Play in the Console.");
        roomImageView.setFocusTraversable(true);

        formatText(gameControl.getPlayer().getPosition().getObstacle()); //format the text to display

        VBox roomPane = new VBox(roomImageView,roomDescLabel);
        roomPane.setPadding(new Insets(10));
        roomPane.setAlignment(Pos.TOP_CENTER);
        roomPane.setStyle("-fx-background-color: #000000;");

        gridPane.add(roomPane, 1, 1);
        stage.sizeToScene();


        //finally, articulate the description
        articulateRoomDescription("Troll");
    }


    /**
     * updateItems
     * __________________________
     *
     * This method is partially completed, but you are asked to finish it off.
     *
     * The method should populate the objectsInSquare and objectsInInventory Vboxes.
     * Each Vbox should contain a collection of nodes (Buttons, ImageViews, you can decide)
     * Each node represents a different object.
     * Images of each object are in the assets
     * folders of the given adventure game.
     */
    public void updateItems() {
        //please use setAccessibleText to add "alt" descriptions to your images!

        objectsInSquare.getChildren().clear();
        objectsInInventory.getChildren().clear();

        // Adding buttons
        GameObj obj = gameControl.getPlayer().getPosition().getObj();

        if (obj != null)
        {
            String obj_price = String.valueOf(obj.getPrice());
            VBox vbox = getVBox(obj.getName().toUpperCase(), "$" + obj_price);
            Button object_button = setButton(vbox, "take");
            objectsInSquare.getChildren().add(object_button);
        }

        for (GameObj object : gameControl.getPlayer().getInventory())
        {
            String obj_price2 = String.valueOf(object.getPrice());
            VBox vbox2 = getVBox(object.getName().toUpperCase(), "$" + obj_price2);
            Button object_button2 = setButton(vbox2, "no take");
            objectsInInventory.getChildren().add(object_button2);
        }

        removeParent(objectsInSquare, objectsInInventory);

        ScrollPane scO = new ScrollPane(objectsInSquare);
        scO.setPadding(new Insets(10));
        scO.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        scO.setFitToWidth(true);
        gridPane.add(scO,0,1);

        ScrollPane scI = new ScrollPane(objectsInInventory);
        scI.setPadding(new Insets(10));
        scI.setFitToWidth(true);
        scI.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        gridPane.add(scI,2,1);
    }

    private VBox getVBox(String object, String objectPrice) {
        String imagePath = "object-images/" + object + ".jpg";
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitWidth(100);
        imageView.setFitHeight(75);

        Label name = new Label(objectPrice);
        name.setStyle("-fx-text-fill: black; -fx-alignment: center;");
        name.setFont(new Font("Times New Roman", 16));

        VBox vbox = new VBox(imageView, name);
        vbox.setAlignment(Pos.CENTER);
        return vbox;
    }

    private Button setButton(VBox vbox, String command){

        Button button = new Button("", vbox);
        button.setStyle("-fx-background-color: white;");

        if (command.equals("take"))
        {
            button.setOnAction(e ->
            {
                submitEvent("buy");
                updateItems();
            });
        }

        button.setPrefSize(100, 75);
        return button;
    }

    private void removeParent(VBox... v_boxes) {
        for (VBox vbox : v_boxes)
        {
            if (vbox.getParent() != null)
            {
                ((Pane) vbox.getParent()).getChildren().remove(vbox);
            }
        }
    }

    /*
     * Show the game instructions.
     *
     * If helpToggle is FALSE:
     * -- display the help text in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- use whatever GUI elements to get the job done!
     * -- set the helpToggle to TRUE
     * -- REMOVE whatever nodes are within the cell beforehand!
     *
     * If helpToggle is TRUE:
     * -- redraw the room image in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- set the helpToggle to FALSE
     * -- Again, REMOVE whatever nodes are within the cell beforehand!
     */
    public void showInstructions() {

        gridPane.getChildren().removeIf(grid ->
                GridPane.getColumnIndex(grid) == 1 && GridPane.getRowIndex(grid) == 1);

        if (!helpToggle)
        {
            Button hideButton = new Button("Hide Instructions");
            customizeButton(hideButton, 200, 20);
            makeButtonAccessible(hideButton, "Hide Instructions", "Hide Instructions", "Use this button to hide the instructions.");
            hideButton.setOnAction(e -> showInstructions());

            Label helpText = new Label(gameControl.getInstructions());
            helpText.setStyle("-fx-text-fill: white;");
            helpText.setFont(new Font("Arial", 16));
            helpText.setWrapText(true);

            VBox vbox = new VBox(hideButton, helpText);
            vbox.setAlignment(Pos.CENTER);

            ScrollPane scroll = new ScrollPane(vbox);
            scroll.setStyle("-fx-background: black;");
            scroll.setFitToWidth(true);

            gridPane.add(scroll, 1, 1);
            helpToggle = true;
            articulateRoomDescription("help");
        }
        else
        {
            stopArticulation();
            helpToggle = false;
            updateScene();
        }

    }

    public void showSquare(String button_number)
    {
        gridPane.getChildren().removeIf(grid ->
                GridPane.getColumnIndex(grid) == 1 && GridPane.getRowIndex(grid) == 1);

        if (!squareToggle)
        {
            Button hideSquareButton = new Button("Hide Square Info");
            customizeButton(hideSquareButton, 200, 20);
            makeButtonAccessible(hideSquareButton, "Hide Square Info", "Hide Square Info", "Use this button to close the square information.");
            hideSquareButton.setOnAction(e -> showSquare(button_number));

            String squareStr = "Square " + button_number;
            String objects = "There are no objects on this square.";
            String troll = "There is no obstacle on this square.";
            String players = "There are no players currently on this square.";

            Square square = gameControl.board.squares.get(Integer.parseInt(button_number)-1);

            if (square.getObj() != null)
            {
                objects = "Object on Square: " + square.getObj().getName() + "\nPrice: $" + square.getObj().getPrice();
            }

            if (square.hasObstacle())
            {
                troll = "There is ONE OBSTACLE on this square.";
            }

            if (!square.getPlayers().isEmpty())
            {
                StringBuilder playersBuilder = new StringBuilder();
                for (Player player : square.getPlayers())
                {
                    playersBuilder.append(player.getName()).append(", ");
                }

                playersBuilder.setLength(playersBuilder.length() - 2);
                String playerNames = playersBuilder.toString();
                players = "Players on this square: " + playerNames;
            }

            Label squareText = new Label("\n\n" + squareStr + "\n\n" + objects + "\n\n" + troll + "\n\n" + players);
            squareText.setStyle("-fx-text-fill: white;");
            squareText.setFont(new Font("Times New Roman", 16));
            squareText.setWrapText(true);

            VBox vbox = new VBox(hideSquareButton, squareText);
            vbox.setAlignment(Pos.CENTER);

            ScrollPane scroll = new ScrollPane(vbox);
            scroll.setStyle("-fx-background: black;");
            scroll.setFitToWidth(true);

            gridPane.add(scroll, 1, 1);
            squareToggle = true;
        }
        else
        {
            squareToggle = false;
            updateScene();
            updateItems();
        }

    }

    public void displayInfo()
    {
        gridPane.getChildren().removeIf(grid ->
                GridPane.getColumnIndex(grid) == 1 && GridPane.getRowIndex(grid) == 1);

        if (!infoToggle)
        {
            Button hideinfoButton = new Button("Hide Info");
            customizeButton(hideinfoButton, 200, 20);
            makeButtonAccessible(hideinfoButton, "Hide Info", "Hide Info", "Use this button to close the player information.");
            hideinfoButton.setOnAction(e -> displayInfo());

            String player1 = "";
            String player2 = "";
            String player3 = "";
            String player4 = "";

            Player p1 = gameControl.getAllPlayers().get(0);

            player1 = p1.getName() + "\nInventory: ";
            
            for (GameObj inv : p1.getInventory())
            {
                player1 = player1 + inv.getName();
            }

            if (p1.getInventory().isEmpty())
            {
                player1 = player1 + "No objects";
            }
            if (p1.getPosition() == null)
            {
                player1 = player1 + "\nPlayer has died or quit.";
            }
            else
            {
                player1 = player1 + "\nPosition: Square" + p1.getPosition().get_room_No();
            }
            if (p1.getPosition().get_room_No() == 15)
            {
                player1 = player1 + "\nPlayer has reached the finish line.";
            }
            player1 = player1 + "\n\n";

            Player p2 = gameControl.getAllPlayers().get(1);

            player2 = p2.getName() + "\nInventory: ";

            for (GameObj inv : p2.getInventory())
            {
                player2 = player2 + inv.getName();
            }

            if (p2.getInventory().isEmpty())
            {
                player2 = player2 + "No objects";
            }
            if (p2.getPosition() == null)
            {
                player2 = player2 + "\nPlayer has died or quit.";
            }
            else
            {
                player2 = player2 + "\nPosition: Square" + p2.getPosition().get_room_No();
            }
            if (p2.getPosition().get_room_No() == 15)
            {
                player2 = player2 + "\nPlayer has reached the finish line.";
            }
            player2 = player2 + "\n\n";

            Player p3 = gameControl.getAllPlayers().get(2);

            player3 = p3.getName() + "\nInventory: ";

            for (GameObj inv : p3.getInventory())
            {
                player3 = player3 + inv.getName();
            }

            if (p3.getInventory().isEmpty())
            {
                player3 = player3 + "No objects";
            }
            if (p3.getPosition() == null)
            {
                player3 = player3 + "\nPlayer has died or quit.";
            }
            else
            {
                player3 = player3 + "\nPosition: Square" + p3.getPosition().get_room_No();
            }
            if (p3.getPosition().get_room_No() == 15)
            {
                player3 = player3 + "\nPlayer has reached the finish line.";
            }
            player3 = player3 + "\n\n";

            Player p4 = gameControl.getAllPlayers().get(3);

            player4 = p4.getName() + "\nInventory: ";

            for (GameObj inv : p4.getInventory())
            {
                player4 = player4 + inv.getName();
            }

            if (p4.getInventory().isEmpty())
            {
                player4 = player4 + "No objects";
            }
            if (p4.getPosition() == null)
            {
                player4 = player4 + "\nPlayer has died or quit.";
            }
            else
            {
                player4 = player4 + "\nPosition: Square" + p4.getPosition().get_room_No();
            }
            if (p4.getPosition().get_room_No() == 15)
            {
                player4 = player4 + "\nPlayer has reached the finish line.";
            }
            player4 = player4 + "\n\n";

            String finalString = player1 + player2 + player3 + player4;

            Label squareText = new Label(finalString);
            squareText.setStyle("-fx-text-fill: white;");
            squareText.setFont(new Font("Times New Roman", 16));
            squareText.setWrapText(true);

            VBox vbox = new VBox(hideinfoButton, squareText);
            vbox.setAlignment(Pos.CENTER);

            ScrollPane scroll = new ScrollPane(vbox);
            scroll.setStyle("-fx-background: black;");
            scroll.setFitToWidth(true);

            gridPane.add(scroll, 1, 1);
            infoToggle = true;
        }
        else
        {
            infoToggle = false;
            updateScene();
            updateItems();
        }
    }

    /**
     * This method handles the event related to the
     * help button.
     */
    public void addInstructionEvent() {
        helpButton.setOnAction(e -> {
            stopArticulation(); //if speaking, stop
            showInstructions();
        });
    }

    public void addQuitEvent()
    {
        quitButton.setOnAction(e -> {
            stopArticulation();
            submitEvent("quit");
        });
    }

    public void addDiceEvent()
    {
        dieButton.setOnAction(e -> {
            stopArticulation();
            submitEvent("roll");
        });
    }

    public void addSquareEvent(Button squareButton)
    {
        squareButton.setOnAction(e -> {
            stopArticulation();
            String[] buttonName = squareButton.getText().split(" ");
            String buttonNumber = buttonName[1];
            showSquare(buttonNumber);
        });
    }

    public void addInfoEvent()
    {
        infoButton.setOnAction(e -> {
            stopArticulation();
            displayInfo();
        });
    }

    /**
     * This method articulates Room Descriptions
     */
    public void articulateRoomDescription(String text) {
        String musicFile;

        if (text.equals("help"))
        {
            musicFile = "sounds/help.m4a";
        }
        else
        {
            int troll = gameControl.getPlayer().getPosition().get_room_No();
            String troll_name = null;

            if (troll == 4)
            {
                troll_name = "NumberTroll";
            }
            else if (troll == 15)
            {
                troll_name = "Riddle";
            }
            else if (troll == 7)
            {
                troll_name = "Rock_Paper";
            }
            else if (troll == 12)
            {
                troll_name = "Tic_Tac_Toe";
            }
            musicFile = "sounds/" + troll_name + ".m4a";
        }

        Media sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        mediaPlaying = true;
    }

    /**
     * This method stops articulations
     * (useful when transitioning to a new room or loading a new game)
     */
    public void stopArticulation() {
        if (mediaPlaying) {
            mediaPlayer.stop(); //shush!
            mediaPlaying = false;
        }
    }
}
