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
 * Board - The base of GUI that the user interacts with
 */
class Board extends VBox{
    private OneTurn[] boardList = new OneTurn[10];
    private int active = 0;

    /**
     * Constructor which lays out the essential components
     */
    public Board(){
        super(8);
        for(int i = 0; i < boardList.length; i ++){
            boardList[i] = new OneTurn();
        }
        for(int i = 9; i >= 0; i --){
            this.getChildren().add(boardList[i]);
        }
    }

    /**
     * Starts a new game
     */
    public void startNewGame(){
        active = 0;
        deactivateAll();
        activate(active);
    }

    /**
     * Once a turn is over, it disables the row and enables the next one
     */
    public void next(){
        deactivate(active++);
        activate(active);
    }

    /**
     * activates a row 
     * @param i The index of the row to be activated
     */
    public void activate(int i){
        boardList[i].activate();
    }

    /**
     * deactivates a row
     * @param i The index of the row to be deactivated
     */
    public void deactivate(int i){
        boardList[i].deactivate();
    }

    /** 
     * deactivates all rows
     */
    public void deactivateAll(){
        for(OneTurn i : boardList){
            i.deactivate();
            i.resetColors();
        }
    }

    /**
     * Gets the colors of the row which is currently active
     * @return ArrayList of Strings that represent the active row's colors
     */
    public ArrayList<String> getTurnColors(){
        OneTurn a = boardList[active];
        Color[] turnColors = a.getColors();
        ArrayList<String> colorsInWords = new ArrayList<String>();
        for(int i = 0; i < turnColors.length; i ++){
            colorsInWords.add(getColorName(turnColors[i].toString()));
        }
        if(colorsInWords.contains("no")) return null;
        return colorsInWords;
    }

    /**
     * Sets the feedback on the active row
     * @param result - The feedback recieved from the server
     */
    public void setResults(ArrayList<String> result){
         boardList[active].setServerPegs(result);
    }

    /**
     * Retrieves the layman name of the color, given the RGB code
     * @param str - RGB Code of color
     * @return Name of the color
     */
    public String getColorName(String str){
        if(str.equals("0xff0000ff")){
            return "Red";
        }
        else if (str.equals("0xffff00ff")){
            return "Yellow";
        }
        else if (str.equals("0x008000ff")){
            return "Green";
        }
        else if(str.equals("0x0000ffff")){
            return "Blue";
        }
        else if(str.equals("0xa52a2aff")){
            return "Brown";
        }
        else if(str.equals("0xffc0cbff")){
            return "Pink";
        }
        else{
            return "no";
        }
    }

    /**
     * Checks if the player has lost the game yet
     * @return true if player has lost, false otherwise
     */
    public boolean isLost(){
        if(active > 8){
            return true;
        }
        else{
            return false;
        }
    }
}
