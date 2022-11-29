package com.example.quizkampenclientserver;

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
    public Label scoreLabel;
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
    ObjectOutputStream output;
    ObjectInputStream input;
    BufferedReader userInput;
    Player player;
    Player opponent;
    String category;
    Question question;
    int round = 1;
    boolean gameFinished = false;
    URL url;
    ResourceBundle resourceBundle;




    public void run(ObjectOutputStream output, ObjectInputStream input, BufferedReader userInput, Player player, int round, boolean gameFinished, String id, int s ) throws IOException, ClassNotFoundException
    {
        if(player.isCurrentPlayer()){
            setTurnLabel("YOUR TURN");
        }
        else {
            setTurnLabel("WAIT");
        }
        setPlayer(player);
        setRound(round);
        setOutput(output);
        setInput(input);
        setUserInput(userInput);
        setGameFinished(gameFinished);
        System.out.println(this.player.getScoreArray());
        System.out.println(this.player.getScoreLabels());
        setLabels(this.player);
        this.opponent = (Player) input.readObject();
        System.out.println(this.opponent.getScoreArray());
        System.out.println(this.opponent.getScoreLabels());
        setLabels(this.opponent);
        System.out.println("Current player: " + player.currentPlayer);
        System.out.println("First player: " + player.isFirstPlayer);
        if(gameFinished){
            setLabels(this.player);
            if(this.player.isFirstPlayer()){
                this.opponent = (Player) input.readObject();
                setLabels(this.opponent);
            }
            System.out.println("Spelet är slut");
            turnLabel.setText("GAME FINISHED!");
        }
        else{
            System.out.println("Kollar om det är min tur...");
            if(this.player.currentPlayer){
                System.out.println("Det är min tur...");
                playButton.setOnAction(event -> {
                    try
                    {
                        System.out.println("Jag tryckte på startknappen");
                        System.out.println("Kollar om jag är player 1...");
                        if(this.player.isFirstPlayer()){
                            System.out.println("Jag är player 1...");
                            System.out.println("Nu ska jag välja kategori...");
                            goToCategoryScene();
                        }
                        else {
                            System.out.println("Jag är inte player 1...");
                            System.out.println("Nu går jag till quizzet...");
                            goToGameScene(false);
                        }
                    } catch (IOException | ClassNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                });
            }


            else{
                turnLabel.setText("WAIT");
                System.out.println("Väntar på att det ska bli min tur att få spela");
                playButton.setOnAction(event -> {
                    try
                    {
                        setPlayer((Player) this.input.readObject());
                        System.out.println("Kollar om jag är player 1...");
                        if(this.player.isFirstPlayer()){

                            System.out.println("Jag är spelare 1...");
                            System.out.println("Nu ska jag välja kategori...");
                            goToCategoryScene();
                        }
                        else {
                            System.out.println("Jag är inte spelare 1...");
                            System.out.println("Nu går jag till spelet...");
                            System.out.println("väntar på att servern ska ge mig en kategori");
                            this.category = (String) this.input.readObject();
                            System.out.println("Kategorin mottagen...");
                            System.out.println("Väntar på att servern ska ge mig en fråga från kategorin " + this.category);
                            this.question = (Question) this.input.readObject();
                            System.out.println("Frågan mottagen...");

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
        for(String s: p.getScoreLabels()){
            for(Node n: scorePane.getChildren()){
                try{
                    l = (Label) n;
                    if(s.equals(l.getId())){
                        l.setText(p.getScoreArray().get(index));
                        index++;
                    }
                }
                catch (Exception e){
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
        controller.startQuiz(category, player, round, output,input,question, b);
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



    public void setScoreRound(String id, int s){

        System.out.println("id: " + id);
        System.out.println(scorePane.getChildren());

    }
    public void setPlayer2Label(Label player2Label)
    {
        Player2Label = player2Label;
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
    public void setPlayer1Label(Label player1Label)
    {
        this.player1Label = player1Label;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public void setQuestion(Question question)
    {
        this.question = question;
    }

    public void setRound(int round)
    {
        this.round = round;
    }

    public void setAvatar1(Circle avatar1)
    {
        this.avatar1 = avatar1;
    }

    public void setScore(String score)
    {
        scoreLabel.setText(score);
    }

    public void setTurnLabel(String s)
    {
        this.turnLabel.setText(s);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
    }
}
