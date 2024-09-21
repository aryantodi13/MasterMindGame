import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.scene.paint.Color;

import java.util.*;

/**
 * OneTurn - Represents one row of the board, i.e., a guess and the feedback for that guess
 */
class OneTurn extends FlowPane{
    private FlowPane userDec = new FlowPane(10, 10);
    private GridPane serverDec = new GridPane();
    private ServerPeg[] serverPegList = {new ServerPeg(), new ServerPeg(), new ServerPeg(), new ServerPeg()};
    private UserPeg[] userPegList = {new UserPeg(), new UserPeg(), new UserPeg(), new UserPeg()};

    /**
     * Constructor that sets the layout of the components
     */
    OneTurn(){
        for(int i = 0; i < userPegList.length; i ++){
            userPegList[i].setDisable(true);
            userDec.getChildren().add(userPegList[i]);
        }
        userDec.setPrefWidth(200);
        serverDec.add(serverPegList[0], 1, 0);
        serverDec.add(serverPegList[1], 2, 0);
        serverDec.add(serverPegList[2], 1, 1);
        serverDec.add(serverPegList[3], 2, 1);
        serverDec.setHgap(2);
        serverDec.setVgap(2);
        this.getChildren().addAll(userDec, serverDec);
    }

    /**
     * Allows the buttons to be clicked, which are disabled by default
     */
    public void activate(){
        for(int i = 0; i < userPegList.length; i ++){
            userPegList[i].setDisable(false);
        }
    }

    /**
     * Disallows the user from clicking the buttons/ changing the color of the pegs
     */
    public void deactivate(){
        for(int i = 0; i < userPegList.length; i ++){
            userPegList[i].setDisable(true);
        }
    }

    /**
     * Resets the colors of the user and feedback pegs, normally when a new game begins
     */
    public void resetColors(){
        for(int i = 0; i < userPegList.length; i ++){
            userPegList[i].resetColor();
            serverPegList[i].reset();
        }
    }

    /**
     * Shows the user the feedback for the guess once it recieves it from the server
     * @param result - The feedback from the server
     */
    public void setServerPegs(ArrayList<String> result){
        for(int i = 0; i < result.size(); i++){
            if(result.get(i).equals("Red")){
               serverPegList[i].setColorOne();
            }
            else{
               serverPegList[i].setColorTwo();
            }
        }
    }

    /**
     * Returns an array of colors, the colors of each peg that the user has changed, in order
     * @return an array of colors representing the user's guess
     */
    public Color[] getColors(){
        Color[] colors = new Color[userPegList.length];
        for(int i = 0; i < userPegList.length; i ++){
            colors[i] = userPegList[i].getColor();
        }
        return colors;
    }
}
