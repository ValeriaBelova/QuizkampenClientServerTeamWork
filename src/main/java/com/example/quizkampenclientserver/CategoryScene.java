package com.example.quizkampenclientserver;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class CategoryScene
{
    public Label categoryLabel;
    public Button geographyButton;
    public Button animalsButton;
    public Button foodButton;
    public Button swedishHistoryButton;
    public Button sportButton;
    public Button musicButton;
    ObjectOutputStream output;
    ObjectInputStream input;
    BufferedReader userInput;
    Player player;
    String category;
    Question question;
    int round = 1;



    public void switchToQuizScene(ActionEvent event) throws IOException, ClassNotFoundException
    {
        if(player.currentPlayer){
            Button button = (Button) event.getSource();
            this.category = button.getText();
            output.writeObject(category); // Skicka till servern vilken kategori som spelaren har valt
            this.question = (Question) input.readObject();// Be servern om en fråga i den kategorin
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Client.class.getResource("gameScene.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            GameScene controller = loader.getController();
            controller.startQuiz(category, player, round, output,input,question, true);
            Stage stage = (Stage) categoryLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }


    }

    public void setOutput(ObjectOutputStream output){
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

    public void run(ObjectOutputStream output, ObjectInputStream input, BufferedReader userInput,Player player, int round) throws IOException, ClassNotFoundException
    {
        setOutput(output);
        setInput(input);
        setUserInput(userInput);
        setPlayer(player);
        setRound(round);
    }



}
