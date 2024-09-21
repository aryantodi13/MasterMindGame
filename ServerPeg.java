import javafx.scene.shape.Circle;
import javafx.scene.paint.*;

/**
 * ServerPeg - The peg which serves as feedback for the user's guess
 */
class ServerPeg extends Circle{

    /**
     * Constructor that sets the color and radius of the peg
     */
    ServerPeg(){
        this.setRadius(10);
        this.setFill(Color.GREY);
    }

    /**
     * Sets the color of the peg to red
     */
    public void setColorOne(){
        this.setFill(Color.RED);
    }

    /**
     * Sets the color of the peg to white
     */
    public void setColorTwo(){
        this.setFill(Color.WHITE);
    }
    
    /**
     * Resets the color of the peg
     */
    public void reset(){
        this.setFill(Color.GREY);
    }
}
