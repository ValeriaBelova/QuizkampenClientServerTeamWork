package com.example.quizkampenclientserver;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class scoreController implements Initializable
{
    public Label player1Label;
    public Label Player2Label;
    public Circle avatar1;
    public Circle avatar2;
    public Button playButton;
    public Label turnLabel;
    public Label score13;
    public Label score11;
    public Label score21;
    public Label score12;
    public Label score22;
    public Label score23;
    public Label score24;
    public Label score14;
    public Label score15;
    public Label score25;
    public Label score16;
    public Label score26;
    public Pane scorePane;
    public Label score1Label;
    public Label score2Label;
    public Label hyphenLabel;
    ObjectOutputStream output;
    ObjectInputStream input;
    BufferedReader userInput;
    Player player;
    Player opponent;
    String category;
    Question question;
    int round = 1;
    boolean gameFinished = false;

    // DENNA METOD KÖRS BÅDE FRÅN CLIENTCONTROLLER NÄR SPELET BÖRJAR OCH FRÅN GAMESCENE
    public void run(ObjectOutputStream output, ObjectInputStream input, BufferedReader userInput, Player player, int round, boolean gameFinished) throws IOException, ClassNotFoundException
    {
        if (player.isCurrentPlayer())
        {
            setTurnLabel("YOUR TURN");
        }
        // CONSTRUCTOR
        setPlayer(player);
        setRound(round);
        setOutput(output);
        setInput(input);
        setUserInput(userInput);
        setGameFinished(gameFinished);

        // UPPDATERA NUVARANDE SPELARES SCORE
        setLabels(this.player);
        // fråga servern efter motståndar-player för att sätta dess labels
        this.opponent = (Player) input.readObject();
        // UPPDATERA MOTSTÅNDARENS SCORE
        setLabels(this.opponent);
        // KOLLA OM SPELET ÄR SLUT
        if (gameFinished)
        {
            // UPPDATERA NUVARANDE SPELARES SCORE
            setLabels(this.player);
            // OM DET ÄR SPELAREN SOM HAR VALT KATEGORI
            if (this.player.isFirstPlayer())
            {
                // UPPDATERA SLUTRESULTAT
                setHyphenLabel("-");
                setScore1Label(String.valueOf(this.player.getPoints()));
                // VÄNTA PÅ ATT ANDRA SPELAREN HAR KÖRT KLART SIN SISTA RUNDA
                this.opponent = (Player) input.readObject();
                // UPPDATERA MOTSTÅNDARENS SCORE
                setLabels(this.opponent);
                // UPPDATERA MOTSTÅNDARENS SLUTRESULTAT
                setScore2Label(String.valueOf(this.opponent.getPoints()));
            }
            // OM DET ÄR SPELAREN SOM INTE HAR VALT KATEGORI
            else if (!player.isFirstPlayer())
            {
                // UPPDATERA SLUTRESULTAT
                setHyphenLabel("-");
                setScore1Label(String.valueOf(this.opponent.getPoints()));
                setScore2Label(String.valueOf(this.player.getPoints()));
            }
            turnLabel.setText("GAME FINISHED!");
        }
        // OM SPELET INTE ÄR SLUT
        else
        {
            // OM DET ÄR SPELARENS TUR ATT KÖRA EN RUNDA
            // DÅ SKA SPELAREN ANTINGEN VÄLJA EN KATEGORI ELLER KÖRA EN RUNDA MED SAMMA FRÅGOR SOM TIDIGARE SPELARE
            if (this.player.currentPlayer)
            {
                playButton.setOnAction(event ->
                {
                    try
                    {
                        // OM DET ÄR SPELAREN SOM SKA VÄLJA KATEGORI
                        if (this.player.isFirstPlayer())
                        {
                            // GÅ TILL KATEGORIER
                            goToCategoryScene();
                        }
                        // ANNARS
                        else
                        {
                            // GÅ TILL FRÅGORNA
                            goToGameScene(false); // false betyder att det är spelaren som inte ska välja kategori som kör
                        }
                    } catch (IOException | ClassNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                });
            }

            // OM DET INTE ÄR SPELARENS TUR ATT KÖRA EN RUNDA
            // DÅ SKA SPELAREN VÄNTA PÅ ATT ANDRA SPELAREN HAR KÖRT KLART
            else
            {
                turnLabel.setText("Press play and wait");
                playButton.setOnAction(event ->
                {
                    try
                    {
                        // VÄNTA PÅ ATT MOTSTÅNDAREN HAR KÖRT KLART
                        setPlayer((Player) this.input.readObject()); // här väntar spelaren på att serverns ska säga att det är deras tur
                        turnLabel.setText("YOUR TURN");
                        // OM DET ÄR SPELAREN SOM SKA VÄLJA KATEGORI SOM VÄNTAR
                        if (this.player.isFirstPlayer())
                        {
                            // GÅ TILL KATEGORIER
                            goToCategoryScene();
                        } else
                        {
                            // ANNARS
                            // SÄTTER FRÅGAN SOM SKA SKICKAS TILL GAME SCENE
                            this.question = (Question) this.input.readObject(); // ta emot frågan
                            // GÅ TILL GAME SCENE
                            goToGameScene(false);
                        }
                    } catch (IOException | ClassNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                });
            }
        }


    }

    private void setLabels(Player p)
    {
        Label l;
        int index = 0;
        for (String s : p.getScoreLabels())
        {
            for (Node n : scorePane.getChildren())
            {
                try
                {
                    l = (Label) n;
                    if (s.equals(l.getId()))
                    {
                        l.setText(p.getScoreArray().get(index));
                        index++;
                    }
                } catch (Exception e)
                {
                    System.out.println("Ignore this.");
                }
            }
        }
    }

    private void goToGameScene(boolean b) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Client.class.getResource("gameScene.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        GameScene controller = loader.getController();
        controller.startQuiz(category, player, round, output, input, question, b);
        Stage stage = (Stage) playButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void goToCategoryScene() throws IOException, ClassNotFoundException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Client.class.getResource("categoryScene.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        CategoryScene controller = loader.getController();
        controller.run(output, input, userInput, player, round);
        Stage stage = (Stage) playButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void setOutput(ObjectOutputStream output)
    {
        this.output = output;
    }

    public void setInput(ObjectInputStream input)
    {
        this.input = input;
    }

    public void setUserInput(BufferedReader userInput)
    {
        this.userInput = userInput;
    }

    public void setGameFinished(boolean gameFinished)
    {
        this.gameFinished = gameFinished;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }

    public void setRound(int round)
    {
        this.round = round;
    }

    public void setTurnLabel(String s)
    {
        this.turnLabel.setText(s);
    }

    public void setHyphenLabel(String s)
    {
        this.hyphenLabel.setText(s);
    }

    public void setScore1Label(String s)
    {
        this.score1Label.setText(s);
    }

    public void setScore2Label(String s)
    {
        this.score2Label.setText(s);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
    }
}
