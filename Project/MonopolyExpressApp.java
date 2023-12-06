import Model.GameController;
import javafx.application.Application;
import javafx.stage.Stage;
import views.Visualizer;

import java.io.IOException;

/**
 * Class MonopolyExpressApp.
 */
public class MonopolyExpressApp extends  Application {

    GameController model;
    Visualizer view;

    public static void main(String[] args) {
        launch(args);
    }

    /*
     * JavaFX is a Framework, and to use it we will have to
     * respect its control flow!  To start the game, we need
     * to call "launch" which will in turn call "start" ...
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        this.model = new GameController();
        this.view = new Visualizer(model, primaryStage);
    }
}