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
    int currentRound = 1;
    ObjectOutputStream output;
    ObjectInputStream input;
    BufferedReader userInput;
    int scoreForThisRound = 0;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
    }

    // DET HÄR KÖRS FRÅN SCORECONTROLLER
    public void startQuiz(String cat, Player player,int round, ObjectOutputStream output, ObjectInputStream input, Question q, Boolean firstPlayerTurn)
    {
        this.output = output;
        this.input = input;
        this.currentRound = round;
        this.player = player;
        this.currentTurn = 1;
        this.category = cat;
        this.currentQuestion = q;
        this.firstPlayerTurn = firstPlayerTurn;
        // SÄTT FRÅGAN
        setQuestion();
    }

    // VISAR FRÅGAN
    public void setQuestion()
    {
        questionLabel.setText(currentQuestion.getDescription());
        // LOOPA GENOM ALLA BUTTONS OCH UPPDATERA DEM MED SVARSALTERNATIVEN FÖR FRÅGAN
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
                System.out.println("Ignore.");
            }
        }
        // SÄTT LABELS FÖR SPELARENS NAMN, NUVARANDE RUNDA OCH NUVARANDE FRÅGA
        playerLabel.setText("Player: " + player.getName());
        roundLabel.setText("Round: " + currentRound + "/" + player.getNumberOfRoundsPerGame());
        turnLabel.setText("Question: " + currentTurn + "/" + player.getNumberOfTurnsPerRound());
    }

    // OM SPELAREN TRYCKER PÅ ETT AV ALTERNATIVEN
    public void checkAnswer(ActionEvent actionEvent)
    {
        // TA REDA PÅ VILKEN KNAPP SPELAREN TRYCKTE PÅ
        Button button = (Button) actionEvent.getSource();
        // HÄMTA DET KORREKTA SVARET
        String correctAnswer = currentQuestion.getAnswers()[currentQuestion.getCorrectAnswerindex()];

        // OM SPELAREN SVARADE RÄTT
        if (button.getText().equals(correctAnswer))
        {
            // LÄGG TILL ETT POÄNG I SPELARENS TOTALA POÄNG
            player.setPoints(player.getPoints() + 1);
            // LÄGG TILL ETT POÄNG FÖR NUVARANDE RUNDA
            scoreForThisRound ++;
        }
        // ÖKA ANTALET GÅNGER SPELAREN HAR SVARAT PÅ FRÅGOR
        player.questionsAnswered ++;
        // VISA RÄTT SVAR
        showCorrectAnswer();
        // PAUSA FÖR ATT SPELAREN SKA SE RÄTT SVAR
        PauseTransition wait = new PauseTransition(Duration.seconds(2));
        wait.setOnFinished(event ->
        {
            // OM SPELAREN KAN KÖRA FLERA FRÅGOR
            if (currentTurn < player.getNumberOfTurnsPerRound())
            {
                try
                {
                    // GÅ TILL NÄSTA FRÅGA
                    nextTurn();
                } catch (IOException | ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
            // OM SPELAREN HAR KÖRT ALLA FRÅGOR OCH DET ÄR SPELAREN SOM VALT KATEGORI
            else if(firstPlayerTurn)
            {
                try
                {
                    // UPPDATERA SPELARENS POÄNG FÖR RUNDAN
                    ArrayList<String> array = player.getScoreArray();
                    array.set(currentRound - 1, String.valueOf(scoreForThisRound));
                    player.setScoreArray(array);
                    // KOLLA OM SPELAREN KAN KÖRA FLERA RUNDOR
                    if(currentRound < player.getNumberOfRoundsPerGame()){
                        // BYT SPELARE
                        switchPlayer();
                    }
                    // OM MAN HAR KÖRT SISTA RUNDAN
                    else {
                        // AVSLUTA SPELET
                        endGame();
                    }

                } catch (IOException | ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
            // OM SPELAREN HAR KÖRT ALLA FRÅGOR OCH DET INTE ÄR SPELAREN SOM VALT KATEGORI
            else {
                try
                {
                    // GÅ TILL NÄSTA RUNDA
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
        // HÄMTA KORREKT SVAR
        String correctAnswer = currentQuestion.getAnswers()[currentQuestion.getCorrectAnswerindex()];
        // LOOPA GENOM KNAPPARNA
        for (Node n : gameGridPane.getChildren())
        {
            try
            {
                Button b = (Button) n;
                // OM KNAPPEN HAR RÄTT SVARSALTERNATIV
                if (b.getText().equals(correctAnswer))
                {
                    // GÖR KNAPPEN GRÖN
                    b.setBackground(Background.fill(Color.LIMEGREEN));
                }
                // OM KNAPPEN INTE HAR RÄTT SVARSALTERNATIV
                else
                {
                    // GÖR KNAPPEN RÖD
                    b.setBackground(Background.fill(Color.RED));
                }

            } catch (ClassCastException e)
            {
                System.out.println("Fixa knapp");
            }
        }
    }
    public void nextTurn() throws IOException, ClassNotFoundException
    {
        // ÖKA ANTALET FRÅGOR SOM SVARATS
        currentTurn++;
        // BE SERVERN OM ATT FÅ EN NY FRÅGA
        this.currentQuestion = (Question)input.readObject();
        // SÄTT NYA FRÅGAN
        setQuestion();
    }

    public void nextRound() throws IOException, ClassNotFoundException
    {
        if (currentRound < player.getNumberOfRoundsPerGame())
        {
            ArrayList<String> array = player.getScoreArray();
            array.set(currentRound - 1, String.valueOf(scoreForThisRound));
            player.setScoreArray(array);
            switchPlayer();
        }
        else{
            ArrayList<String> array = player.getScoreArray();
            array.set(currentRound - 1, String.valueOf(scoreForThisRound));
            player.setScoreArray(array);
            endGame();
        }
    }

    private void switchPlayer() throws IOException, ClassNotFoundException
    {
        // ÖKA ANTALET RUNDOR
        currentRound++;
        // DET ÄR INTE LÄNGRE NUVARANDE SPELARENS TUR
        player.currentPlayer = false;
        // MEDDELA SERVERN ATT NUVARANDE SPELARE HAR KÖRT KLART
        output.writeObject(player);
        // GÅ TILL SCORE SCENE
        switchToScoreScene(false);
    }

    private void endGame() throws IOException, ClassNotFoundException
    {
        // DET ÄR INTE LÄNGRE NUVARANDE SPELARENS TUR
        player.currentPlayer = false;
        // MEDDELA SERVERN ATT NUVARANDE SPELARE HAR KÖRT KLART
        output.writeObject(player);
        // SÄTT GAMEFINISHED TILL TRUE OCH GÅ TILL SCORE SCENE
        switchToScoreScene(true);
    }



    private void switchToScoreScene(Boolean gameFinished) throws IOException, ClassNotFoundException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scoreScene.fxml"));
        Parent root = loader.load();
        scoreController controller = loader.getController();
        controller.run(output,input,userInput,player,currentRound,gameFinished);
        Stage stage = (Stage) answer1Button.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
