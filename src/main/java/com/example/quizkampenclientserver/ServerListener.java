package com.example.quizkampenclientserver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener extends Application
{

    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(ServerListener.class.getResource("serverListenerScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Server Listener");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args)
    {
        launch();
    }
}
/*
    int port = 55557;
    serverListenerController controller = fxmlLoader.getController();
        try (ServerSocket serverSocket = new ServerSocket(port);){
                while(true) {
                //controller.setWelcomeText("Waiting for Player 1 to connect...");
                System.out.println("hello");

                Socket socket1 = serverSocket.accept();
                System.out.println("Player 1 connected");
                Socket socket2 = serverSocket.accept();
                System.out.println("Player 2 connected");

                System.out.println();
                break;
                }
                } catch (IOException e) {
                throw new RuntimeException(e);}
                */