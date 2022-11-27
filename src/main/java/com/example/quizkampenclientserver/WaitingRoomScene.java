package com.example.quizkampenclientserver;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

public class WaitingRoomScene
{
    public Label waitingLabel;
    boolean turn;
    ObjectOutputStream output;
    ObjectInputStream input;
    BufferedReader userInput;
    Socket socket;
    Player player;
    public void run(ObjectOutputStream output, ObjectInputStream input, BufferedReader userInput, Socket socket, Player player) throws IOException, ClassNotFoundException{
        setOutput(output);
        setInput(input);
        setUserInput(userInput);
        setSocket(socket);
        setPlayer(player);

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

    public void setSocket(Socket socket)
    {
        this.socket = socket;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }
}
