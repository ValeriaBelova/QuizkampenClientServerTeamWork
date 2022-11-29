package com.example.quizkampenclientserver;

import javafx.fxml.Initializable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MatchController
{
    QuestionsDataBase db;
    Player player1;
    Player player2;
    Player player;
    ObjectOutputStream output;
    ObjectInputStream input;
    ObjectOutputStream outputp1;
    ObjectInputStream inputp1;
    ObjectOutputStream outputp2;
    ObjectInputStream inputp2;
    Socket socket;

    public void setDb(QuestionsDataBase db)
    {
        this.db = db;
    }

    public void setPlayer1(Player player1)
    {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2)
    {
        this.player2 = player2;
    }

    public void setOutputp1(ObjectOutputStream outputp1)
    {
        this.outputp1 = outputp1;
    }

    public void setInputp1(ObjectInputStream inputp1)
    {
        this.inputp1 = inputp1;
    }

    public void setOutputp2(ObjectOutputStream outputp2)
    {
        this.outputp2 = outputp2;
    }

    public void setInputp2(ObjectInputStream inputp2)
    {
        this.inputp2 = inputp2;
    }

    public void setSocket(Socket socket)
    {
        this.socket = socket;
    }

    public void setOutput(ObjectOutputStream output)
    {
        this.output = output;
    }

    public void setInput(ObjectInputStream input)
    {
        this.input = input;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }

    public MatchController(Socket socket) throws IOException, ClassNotFoundException
    {

        //setPlayer1(new Player("Player 1"));
        //setPlayer2(new Player("Player 2"));
        // player1.setCurrentPlayer(true);
        // player1.setFirstPlayer(true);
        int numberOfTurnsPerRound = 2;
        int numberOfRoundsPerGame = 2;
        ArrayList<String> scoreArray = new ArrayList<>();
        for (int i = 0; i < numberOfRoundsPerGame; i++){
            scoreArray.add("");
        }

        setPlayer(new Player("Player"));
        player.setScoreArray(scoreArray);
        player.setNumberOfRoundsPerGame(numberOfRoundsPerGame);
        player.setNumberOfTurnsPerRound(numberOfTurnsPerRound);
        setSocket(socket);
        try
        {
            setOutput(new ObjectOutputStream(this.socket.getOutputStream()));
            setInput(new ObjectInputStream(this.socket.getInputStream()));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public void runMatch(Socket socket1, Socket socket2) throws IOException, ClassNotFoundException
    {


    }




}
