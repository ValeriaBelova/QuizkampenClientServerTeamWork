package com.example.quizkampenclientserver;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class StartScene
{
    @FXML
    public Button startNewButton;
    public TextField nameTextField;
    public Label nameLabel;
    public Button submitButton;
    Player player;
    Socket socket;
    ObjectInputStream input;
    ObjectOutputStream output;
    BufferedReader userInput;
    public void switchToGameScene() throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ServerListener.class.getResource("choiceOfCategoryScreen.fxml")); //ändra detta sen
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage = (Stage) (startNewButton.getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }

    public void startGame(Socket socket) throws IOException, ClassNotFoundException
    {
        setSocket(socket);
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());
        this.userInput = new BufferedReader(new InputStreamReader(System.in));
        Player p = (Player) input.readObject(); // ta emot player så att den kan läggas till i spelet
        setPlayer(p); // lägg till spelaren
        changeName(); // bytt namn på spelare 1 till det som användaren skriver in
        changeName(); // bytt namn på spelare 2 till det som användaren skriver in
        startNewButton.setOnAction(event -> {
            try
            {
                switchToGameScene();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        });
    }

    public void changeName() throws IOException, ClassNotFoundException
    {
        Socket s = (Socket) input.readObject(); // ta emot socket för att se om player är currentPlayer
        // koll om player är currentPlayer och om den är det så ska man lägga till namnet som användaren har skrivit in
        if(s == socket){
            submitButton.setOnAction(event -> {
                String name = nameTextField.getText();
                player.setName(name);
            });
        }
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    public void setSocket(Socket socket){
        this.socket = socket;
    }

    public Player getPlayer(){
        return player;
    }

    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }

}
