package solitaire;



import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * This is the main class to run the game
 * 
 * @author Jia Hong
 */
public class Main extends Application {

    private static final double WINDOW_WIDTH = 1050;
    private static final double WINDOW_HEIGHT = 650;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Card.loadCardImages();
        Game game = new Game();
        game.setTableBackground(new Image("resources/table/nasa.jpg",WINDOW_WIDTH, WINDOW_HEIGHT, true, true));
  

        primaryStage.setTitle("CST8334 Group6 Solitaire");
        primaryStage.setScene(new Scene(game, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.show();
    }

}
