package com.example.quizkampenclientserver;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class ServerListenerControll implements Initializable
{
    int port;
    Socket socket1;
    Socket socket2;
    @FXML
    public Button button;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        /*
        BEHÖVER INTE DENNA
        this.port = 55557;
        try (ServerSocket serverSocket = new ServerSocket(port);){
            while(true) {
                //controller.setWelcomeText("Waiting for Player 1 to connect...");
                System.out.println("Searching for clients");
                this.socket1 = serverSocket.accept();
                System.out.println("Player 1 connected");
                this.socket2 = serverSocket.accept();
                System.out.println("Player 2 connected");
                MatchController controller = new MatchController();
                controller.runMatch(socket1, socket2);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);}

         */


    }


}
