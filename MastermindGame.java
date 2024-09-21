import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.scene.paint.Color;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javafx.scene.control.Alert.*;


/**
 * MastermindGame - Stimulates a game of Mastermind where the player needs to determine 
 * what the 4 color code is by guessing color combinations out of 6 colors in total
 * @author  Dylan Shanley, Aryan Todi, Danielle Smith, Eric Cybulski
 */
public class MastermindGame extends Application implements EventHandler<ActionEvent> {
    // Attributes are GUI components (buttons, text fields, etc.)
    // are declared here.
    // The 6 colors that could be used in a code are Red, Yellow, Green, Blue, Brown, Pink
    
    //FlowPanes
    private FlowPane paneTop;
    private FlowPane paneBot;
    
    //The colored pegs represented as buttons 
    private Board b = new Board();
    private Button btnSave = new Button("Save Log");

       
    // A button to start a new game
    private Button btnNewGame = new Button("New Game");
    // A button that will check to see if the color combination is correct 
    private Button btnCheck = new Button("Check");
    
    private Stage stage;        // The entire window, including title bar and borders
    private Scene scene;        // Interior of window
    // Choose a pane ... BorderPane used here
    private BorderPane root = new BorderPane();
    
    // IO attributes
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;

    // OTHER attributes
    public static final int SERVER_PORT = 32001;
    private Socket socket = null;
    
    /**
     * Main just instantiates an instance of this GUI class
     * @param args unused
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Called automatically after launch sets up javaFX
     */
    public void start(Stage _stage) throws Exception {
        _stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                doDisconnect();
                System.exit(0);
            }
        });

        // save stage as an attribute
        stage = _stage;                        

        // set the text in the title bar
        stage.setTitle("Mastermind");            
        root = new BorderPane();
        paneTop = new FlowPane(10,10);
        paneTop.setAlignment(Pos.CENTER);
        paneBot = new FlowPane(10, 10);
        paneBot.setAlignment(Pos.CENTER);
        paneBot.getChildren().add(btnSave);
        btnSave.setOnAction(this);
        paneBot.getChildren().add(b);
        paneBot.getChildren().add(btnNewGame);
        paneBot.getChildren().add(btnCheck);
        root.setTop(paneTop);
        root.setBottom(paneBot);
        
        // create scene of specified size
        scene = new Scene(root, 300, 600);    

        // associate the scene with the stage
        stage.setScene(scene);                 
        doConnect();
        btnNewGame.setOnAction(this);
        btnCheck.setOnAction(this);

        // display the stage (window)
        stage.show();
    }
    
    /**
     * Handles button clicks
     * @param evt - An action event
     */
    public void handle(ActionEvent evt) {
        // Get the button that was clicked
        Button btn = (Button)evt.getSource();
        
        // Switch on its name
        switch(btn.getText()) {
            case "New Game":
                try{
                    oos.writeObject("New Game");
                    b.startNewGame();
                }
                catch(Exception e){}
                break;
            case "Check":
                try{
                   ArrayList testArray = b.getTurnColors();
                   oos.writeObject("Check");
                   oos.writeObject(testArray);
                   ArrayList<String> result = (ArrayList) ois.readObject();
                   b.setResults(result);
                   if(allRed(result)){
                      Alert al = new Alert(AlertType.INFORMATION, "Congratulations.\nYou have won.");
                      al.setHeaderText("YAYY");
                      al.showAndWait();
                      oos.writeObject("New Game");
                      b.startNewGame();
                   }
                   else if(b.isLost() == true){
                      Alert al = new Alert(AlertType.INFORMATION, "You have lost.");
                      al.setHeaderText("Game Over");
                      al.showAndWait();
                      oos.writeObject("New Game");
                      b.startNewGame();
                   }
                   else{
                      b.next();
                   }
                }
                catch(NullPointerException npe){
                       Alert a = new Alert(AlertType.ERROR, "Please select all colors");
                       a.showAndWait();
                       break;
                }
                catch(Exception e){}
                break;
               
            case "Save Log":
                try{
                    oos.writeObject("Save");
                }
                catch(Exception e){}
                break;
        }
    } 
    
    /**
     * Connects to the server
     */
    public void doConnect() {
        try {
            socket = new Socket("localhost", SERVER_PORT);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        }
        catch(IOException ioe) {}
    }  
    
    /**
     * Disconnects from the server
     */
    public void doDisconnect() {
        try {
            oos.writeObject("Closed");
            socket.close();
            ois.close();
            oos.close();
        }
        catch(IOException ioe) {}
    }

    /**
     * Checks to see if all the feedback pegs are red, meaning that the game is won
     * @param arr - The feedback arraylist from the server
     * @return true if the game is won, false otherwise
     */
    public boolean allRed(ArrayList<String> arr){
        if(arr.size() != 4) return false;
        for(String i : arr){
            if(!i.equals("Red")) return false;
        }
        return true;
    }
}
