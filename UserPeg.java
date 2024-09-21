import javafx.scene.control.Button;
import javafx.scene.shape.Circle;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.geometry.Insets;
import javafx.event.*;

/**
 * UserPeg - Class for each peg
 */
class UserPeg extends Button implements EventHandler<ActionEvent>{
    /**
     * Stores the color of the user peg
     */
    private Color color = null;

    /**
     * Stores all possible colors the peg can cycle through
     */
    private Color[] colorList = {Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.BROWN, Color.PINK};

    /**
     * Stores which index of the cycle the peg is currently at
     */
    private int index = 0;

    /**
     * Constructor - sets the dimensions and event handler for the peg
     */
    UserPeg(){
        this.setShape(new Circle(30));
        this.setMinSize(30,30);
        this.setMaxSize(30,30);
        this.setOnAction(this);
    }

    /**
     * Cycles through the colors of the peg
     */
    private void cycle(){
        this.setBackground(new Background(new BackgroundFill(colorList[index], CornerRadii.EMPTY, Insets.EMPTY)));
        color = colorList[index];
        index++;
        if(index == 6){
            index = 0;
        }
    }

    /**
     * Returns the color of the peg
     * @return Color of peg
     */
    public Color getColor(){
        return color;
    }

    /**
     * handles the button clicks
     * @param evt - Click of button
     */
    public void handle(ActionEvent evt){
        cycle();
    }

    /**
     * Sets the color to grey
     */
    public void resetColor(){
        this.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
        color = null;
        index = 0;
    }
}
