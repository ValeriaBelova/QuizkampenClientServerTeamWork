package com.example.quizkampenclientserver;


import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class ClientController implements Initializable
{
    public Button startGameButton;
    public AnchorPane clientAnchorPane;
    public TextField enterNameTextField;
    public Label enterNameLabel;
    public Button submitNameButton;
    public Label startLabel;
    public Label waitingLabel;
    ObjectOutputStream output;
    ObjectInputStream input;
    BufferedReader userInput;
    Socket socket;
    Player player;
    String category;
    Question question;
    int round = 1;
    public Boolean playerReady = false;
    public Boolean gameCanStart = false;

    Boolean turn;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        System.out.println("Jag kom hit");
        int port = 55557;
        try{
            this.socket = new Socket(InetAddress.getLocalHost(), port);
            this.output = new ObjectOutputStream(socket.getOutputStream());
            this.input = new ObjectInputStream(socket.getInputStream());
            this.userInput = new BufferedReader(new InputStreamReader(System.in));

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            this.player = (Player) input.readObject();
        } catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        startLabel.setText("Connection has been established!");
        // SKRIV IN NAMN
        submitNameButton.setOnAction(event -> {
            player.setName(enterNameTextField.getText());
            startLabel.setText("Start game " + player.getName());

        });
        // STARTA SPELET
        startGameButton.setOnAction(event -> {
            if(!playerReady){
                playerReady = true;
                startLabel.setText("Waiting on other player to press start");
                PauseTransition wait = new PauseTransition(Duration.seconds(2));
                wait.setOnFinished(e ->
                {
                    try
                    {
                        goToScoreScene();

                    } catch (IOException | ClassNotFoundException ex)
                    {
                        ex.printStackTrace();
                    }
                });
                wait.play();

            }

        });


    }

    private void goToScoreScene() throws IOException, ClassNotFoundException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Client.class.getResource("scoreScene.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        scoreController controller = loader.getController();
        controller.run(output, input, userInput, player, round, false, " ", 0);
        Stage stage = (Stage) clientAnchorPane.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /*
    public void startGame(ActionEvent event) throws IOException, ClassNotFoundException
    {

        goToScoreScene();
    }

     */
}
// Lägg till textfield så att man kan skriva in sitt namn