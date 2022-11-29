package com.example.quizkampenclientserver;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

public class Player implements Serializable
{
    boolean currentPlayer;
    Socket socket;
    String name;
    int points;
    boolean isFirstPlayer;
    ArrayList<String> scoreArray;
    ArrayList<String> scoreLabels;
    int numberOfTurnsPerRound;
    int numberOfRoundsPerGame;
    int questionsAnswered;

    public Socket getSocket() {
        return socket;
    }

    public Player(String n){
        this.name = n;
        this.points = 0;
        this.isFirstPlayer = false;
        this.scoreArray = new ArrayList<>();
        this.scoreLabels = new ArrayList<>();
        this.numberOfRoundsPerGame = 0;
        this.numberOfTurnsPerRound = 0;
        this.questionsAnswered = 0;
    }

    public ArrayList<String> getScoreArray()
    {
        return scoreArray;
    }

    public ArrayList<String> getScoreLabels()
    {
        return scoreLabels;
    }

    public int getNumberOfTurnsPerRound()
    {
        return numberOfTurnsPerRound;
    }

    public void setNumberOfTurnsPerRound(int numberOfTurnsPerRound)
    {
        this.numberOfTurnsPerRound = numberOfTurnsPerRound;
    }

    public int getNumberOfRoundsPerGame()
    {
        return numberOfRoundsPerGame;
    }

    public void setNumberOfRoundsPerGame(int numberOfRoundsPerGame)
    {
        this.numberOfRoundsPerGame = numberOfRoundsPerGame;
    }

    public void setScoreArray(ArrayList<String> scoreArray)
    {
        this.scoreArray = scoreArray;
    }

    public void setScoreLabels(ArrayList<String> scoreLabels)
    {
        this.scoreLabels = scoreLabels;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getPoints()
    {
        return points;
    }

    public void setPoints(int points)
    {
        this.points = points;
    }

    public boolean isFirstPlayer()
    {
        return isFirstPlayer;
    }

    public void setFirstPlayer(boolean firstPlayer)
    {
        isFirstPlayer = firstPlayer;
    }

    public boolean isCurrentPlayer()
    {
        return currentPlayer;
    }

    public void setCurrentPlayer(boolean currentPlayer)
    {
        this.currentPlayer = currentPlayer;
    }
}
