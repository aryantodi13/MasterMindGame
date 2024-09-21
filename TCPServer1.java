import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * TCPServer - This is a fairly complete server. Only allows single connections, 
 * but receives messages and sends a reply.
 * @author Dylan Shanley, Aryan Todi, Danielle Smith, Eric Cybulski
 * @version 1
 */
public class TCPServer1 extends Application {
    // Window attributes
    private Stage stage;
    private Scene scene;
    private VBox root;
    
    // GUI Components
    public Label lblLog = new Label("Log:");
    public TextArea taLog = new TextArea();
    
    // Socket stuff
    private ServerSocket sSocket = null;
    private static final int SERVER_PORT = 32001;
    private static final String FILE_NAME = "data.dat";
    private ArrayList serverCode;
    
    
    /**
     * main program
     * @param args unused
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Launch, draw and set up GUI
     * Do server stuff
     */
    public void start(Stage _stage) {
        // Window setup
        stage = _stage;
        stage.setTitle("TCPServer1");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent evt) { System.exit(0); }
        });
        stage.setResizable(false);
        root = new VBox(8);
    
        // LOG components
        FlowPane fpLog = new FlowPane(8,8);
        fpLog.setAlignment(Pos.CENTER);
        taLog.setPrefRowCount(10);
        taLog.setPrefColumnCount(35);
        fpLog.getChildren().addAll(lblLog, taLog);
        root.getChildren().add(fpLog);
              
        // Show window
        scene = new Scene(root, 475, 200);
        stage.setScene(scene);
        stage.show();      
        
        // Do server work in a thread
        Thread t = new Thread() {
            public void run() { doServerWork(); }
        };
        t.start();
    }
    
    /** 
     * doServerWork - does the basic non-GUI work of the server 
     */
    public void doServerWork() {
        // Claim the port and start listening for new connections
        try {
            sSocket = new ServerSocket(SERVER_PORT);
        }
        catch(IOException ioe) {
            taLog.appendText("IO Exception (1): "+ ioe + "\n");
            return;
        }

        // Socket for comm with client      
        Socket cSocket = null;
        try {
            // Wait for a connection
            cSocket = sSocket.accept();
        }
        catch(IOException ioe) {
            taLog.appendText("IO Exception (2): "+ ioe + "\n");
            return;
        }
        
        // No real processing yet
        taLog.appendText("Client connected!\n");

        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            oos = new ObjectOutputStream(cSocket.getOutputStream());
            ois = new ObjectInputStream(cSocket.getInputStream());
        }
        catch(Exception e) {
            taLog.appendText("Exception opening streams: " + e + "]n");
        }

        while(true){
            try{
                Object o = ois.readObject();
                if(o instanceof ArrayList){
                    System.out.println("Colors recieved.");
                }
                else if(o.equals("New Game")){
                    taLog.appendText("New Game\n");
                    serverCode = getNewCode();
                    taLog.appendText("Server Code: " + serverCode.get(0) + " " + serverCode.get(1) + " " + serverCode.get(2) + " " + serverCode.get(3) + "\n");
                }
                else if(o.equals("Check")){
                    taLog.appendText("Check\n");
                    ArrayList<String> clientCode = (ArrayList) ois.readObject();
                    taLog.appendText("Client Code: " + clientCode.get(0) + " " + clientCode.get(1) + " " + clientCode.get(2) + " " + clientCode.get(3) + "\n");
                    ArrayList<String> result = testCode(clientCode, serverCode);
                    oos.writeObject(result);
                }
                else if(o.equals("Closed")){
                    taLog.appendText("Client disconnected!\n");
                    ois.close();
                    oos.close();
                    break;
                }
                else if(o.equals("Save")){
                    DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(FILE_NAME)));
                    dos.writeUTF(taLog.getText());
                    dos.close();
                }
                else {
                    break;
                }
            }
            catch(Exception e){}
        }
        try {
            ois.close();
            oos.close();
        }
        catch(Exception e) {
            taLog.appendText("Exception closing streams: " + e + "\n");
        }            
    }
    
    /**
     * Plays role of a codemaker, generates a new sequence of pegs
     * @return ArrayList of four different random colors
     */
    public ArrayList<String> getNewCode(){
        ArrayList list1 = new ArrayList ();
        List<String> colors = Arrays.asList("Red", "Blue", "Yellow", "Green", "Brown", "Pink");
        list1.addAll(colors);
        Collections.shuffle(list1);
        while(list1.size() > 4){
            list1.remove(list1.size() - 1);
        }
        return(list1);
    }
    
    /**
     * Compares the server and client codes and generates an arraylist indicating
     * how many similarities there are
     * @param clientCode - ArrayList of Strings that represent the client's guess
     * @param serverCode - ArrayList of Strings that represent the correct code
     * @return ArrayList of Strings that serve as hint for the user's next turn.
     */
    public ArrayList<String> testCode(ArrayList<String> clientCode, ArrayList<String> serverCode){
        int red = 0;
        int white = 0;
        ArrayList<String> test = new ArrayList<String>();
        ArrayList<String> serverCode2 = new ArrayList<String>();
        for(String i : serverCode) serverCode2.add(i);
        for(int i = 0; i < 4; i++){
            if(clientCode.get(i).equals(serverCode2.get(i))){
                red++;
                serverCode2.set(i, "NkULL");
            }
        }
        for(int i = 0; i < 4; i++){
            if(serverCode2.contains(clientCode.get(i))){
                serverCode2.remove(clientCode.get(i));
                white++;
            }
        }
        while(red>0){
            test.add("Red");
            red--;
        }
        while(white>0){
            test.add("White");
            white--;
        }
        return(test);
    }
       
}
