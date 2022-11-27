package com.example.quizkampenclientserver;


import javafx.animation.PauseTransition;
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
            this.player = (Player) input.readObject(); // ta emot spelaren
            startLabel.setText("Connection has been established! \n" +
                    "Hello " + player.getName());
            // SKRIV IN NAMN
            submitNameButton.setOnAction(event -> {
                player.setName(enterNameTextField.getText());
                startLabel.setText("Hello " + player.name + "\nStart game by pressing the button Start game!");
            });
            // STARTA SPELET
            startGameButton.setOnAction(event -> {
                if(!playerReady){
                    playerReady = true;

                    try
                    {
                        output.writeObject(true); // meddela att spelaren har tryckt på start
                        //stannat
                        startLabel.setText("Waiting on other player to press start");
                        PauseTransition wait = new PauseTransition(Duration.seconds(2));
                        wait.setOnFinished(e ->
                        {
                            try
                            {
                                gameCanStart = (Boolean) input.readObject();
                            } catch (IOException | ClassNotFoundException ex)
                            {
                                ex.printStackTrace();
                            }
                            startLabel.setText("Starting game...");
                            try
                            {
                                startGame();
                            } catch (IOException | ClassNotFoundException ex)
                            {
                                ex.printStackTrace();
                            }
                        });
                        wait.play();

                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }

            });


        } catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    private void startGame() throws IOException, ClassNotFoundException
    {

            if(player.isCurrentPlayer()){
                goToCategoryScene();
            }
            else {
                input.readObject(); // väntar på att spelare 1 har kört klart sin runda
                this.category = (String) input.readObject();
                this.question = (Question) input.readObject();
                this.round = 1;
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(Client.class.getResource("gameScene.fxml"));
                Parent parent = loader.load();
                Scene scene = new Scene(parent);
                GameScene controller = loader.getController();
                controller.startQuiz(category, player, round, output,input,userInput, socket, question, false);
                Stage stage = (Stage) clientAnchorPane.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }

    }
    private void goToCategoryScene() throws IOException, ClassNotFoundException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Client.class.getResource("categoryScene.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        CategoryScene controller = loader.getController();
        controller.run(output, input, userInput, socket, player, 1);
        Stage stage = (Stage) clientAnchorPane.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
// Lägg till textfield så att man kan skriva in sitt namn