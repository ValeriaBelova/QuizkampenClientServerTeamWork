package com.example.quizkampenclientserver;

import javafx.fxml.Initializable;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.ResourceBundle;

public class MatchController
{
    Player player;
    ObjectOutputStream output;
    ObjectInputStream input;
    Socket socket;
    int numberOfTurnsPerRound = 0;
    int numberOfRoundsPerGame = 0;


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
        Properties p = new Properties();
        try{
            p.load(new FileInputStream("src/main/java/com/example/quizkampenclientserver/quizKampen.properties"));
        }
        catch (Exception e){
            System.out.println("Filen kunde inte hittas");
        }
        this.numberOfTurnsPerRound = Integer.parseInt(p.getProperty("numberOfTurnsPerRound"));
        this.numberOfRoundsPerGame = Integer.parseInt(p.getProperty("numberOfRoundsPerGame"));

        ArrayList<String> scoreArray = new ArrayList<>();
        for (int i = 0; i < this.numberOfRoundsPerGame; i++){
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
