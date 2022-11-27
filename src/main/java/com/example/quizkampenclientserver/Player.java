package com.example.quizkampenclientserver;

import java.io.Serializable;
import java.net.Socket;

public class Player implements Serializable
{
    boolean currentPlayer;
    Socket socket;
    String name;
    int points;
    boolean isFirstPlayer;

    public Socket getSocket() {
        return socket;
    }

    public Player(String n, Socket socket){
        this.socket = socket;
        this.name = n;
        this.points = 0;
        this.isFirstPlayer = true;

    }

    public Player(String n){
        this.name = n;
        this.points = 0;
        this.isFirstPlayer = true;
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
