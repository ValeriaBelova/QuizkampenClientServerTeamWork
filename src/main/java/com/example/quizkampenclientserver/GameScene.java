package com.example.quizkampenclientserver;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameScene implements Initializable
{
    @FXML
    private Label playerLabel;
    @FXML
    private Label roundLabel;
    @FXML
    private Label turnLabel;
    @FXML
    private Button answer1Button;
    @FXML
    private Label questionLabel;
    @FXML
    private GridPane gameGridPane;

    Player player;
    String category;
    Question currentQuestion;
    Boolean firstPlayerTurn;
    int currentTurn = 1;
    int numberOfTurnsPerRound = 2;
    int currentRound = 1;
    int numberOfRoundsPerGame = 2;
    ObjectOutputStream output;
    ObjectInputStream input;
    ArrayList<Boolean> answers;
    BufferedReader userInput;
    Socket socket;
    Boolean keepGoing;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
    }

    public void startQuiz(String cat, Player player,int round, ObjectOutputStream output, ObjectInputStream input, BufferedReader userInput, Socket socket, Question q, Boolean firstPlayerTurn)
    {
        this.socket = socket;
        this.output = output;
        this.input = input;
        this.userInput = userInput;
        this.answers = new ArrayList<>();
        this.currentRound = round;
        this.player = player;
        this.currentTurn = 1;
        this.category = cat;
        this.currentQuestion = q;
        this.firstPlayerTurn = firstPlayerTurn;
        setQuestion();
    }

    public void setQuestion()
    {
        questionLabel.setText(currentQuestion.getDescription());
        int index = 0;
        for (Node n : gameGridPane.getChildren())
        {
            try
            {
                Button b = (Button) n;
                b.setBackground(Background.fill(Color.LIGHTSKYBLUE));
                b.setText(currentQuestion.getAnswers()[index]);
                index++;
            } catch (ClassCastException e)
            {
                System.out.println("Fix");
            }
        }
        playerLabel.setText("Player: " + player.getName());
        roundLabel.setText("Round: " + currentRound + "/" + numberOfRoundsPerGame);
        turnLabel.setText("Question: " + currentTurn + "/" + numberOfTurnsPerRound);
    }

    public void checkAnswer(ActionEvent actionEvent)
    {
        Button button = (Button) actionEvent.getSource();
        // Hämta rätta svaret på frågan
        String correctAnswer = currentQuestion.getAnswers()[currentQuestion.getCorrectAnswerindex()];
        if (button.getText().equals(correctAnswer))
        {
           player.setPoints(player.getPoints() + 1);
        }
        showCorrectAnswer();
        PauseTransition wait = new PauseTransition(Duration.seconds(2));
        wait.setOnFinished(event ->
        {
            System.out.println("Current turn: " + currentTurn);
            if (currentTurn < numberOfTurnsPerRound)
            {

                try
                {
                    nextTurn();
                } catch (IOException | ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
            else if(firstPlayerTurn)
            {
                //nextRound();
                try
                {
                    switchPlayer();
                } catch (IOException | ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
            else {
                try
                {
                    nextRound();
                } catch (IOException | ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
        });
        wait.play();
    }

    public void showCorrectAnswer()
    {
        String correctAnswer = currentQuestion.getAnswers()[currentQuestion.getCorrectAnswerindex()];
        for (Node n : gameGridPane.getChildren())
        {
            try
            {
                Button b = (Button) n;
                if (b.getText().equals(correctAnswer))
                {
                    b.setBackground(Background.fill(Color.LIMEGREEN));
                } else
                {
                    b.setBackground(Background.fill(Color.RED));
                }

            } catch (ClassCastException e)
            {
                System.out.println("This is not a button");
            }
        }
    }
    public void nextTurn() throws IOException, ClassNotFoundException
    {
        currentTurn++;
        output.writeObject(category);
        this.currentQuestion = (Question)input.readObject();
        setQuestion();
    }

    public void nextRound() throws IOException, ClassNotFoundException
    {
        if (currentRound < numberOfRoundsPerGame)
        {
            currentRound++;
            //MEDDELA SERVER ATT SPELARE 2 HAR KÖRT KLART
            switchPlayer();
        }
        else{
            output.writeObject(player);
            endGame();
        }
    }

    private void switchPlayer() throws IOException, ClassNotFoundException
    {
        firstPlayerTurn = !firstPlayerTurn;
        output.writeObject(player); // skickar till servern att spelaren har kört klart
        if(player.isCurrentPlayer()){
            // wait for your turn
            System.out.println(4);
            keepGoing = (Boolean) input.readObject();
            System.out.println("KEEP GOING ÄR: " + keepGoing);
            if(keepGoing){
                System.out.println("NU GÅR JAG TILL KATEGORIER");
                switchToCategoryScene();
                System.out.println();
            }
            else {
                System.out.println("NU AVSLUTAS SPELET!");
                endGame();
            }

            System.out.println(5);
        }
        else{
            System.out.println(4);
            input.readObject();
            this.category = (String) input.readObject();
            this.currentQuestion = (Question) input.readObject();
            startQuiz(category, player, currentRound, output,input,userInput, socket, currentQuestion, false);
        }

    }

    private void endGame() throws IOException
    {
        switchToScoreScene();
    }



    public void switchToCategoryScene() throws IOException, ClassNotFoundException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Client.class.getResource("categoryScene.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        CategoryScene controller = loader.getController();
        controller.run(output, input, userInput, socket, player, currentRound);
        Stage stage = (Stage) gameGridPane.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void switchToScoreScene() throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Client.class.getResource("scoreScene.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage = (Stage) answer1Button.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
