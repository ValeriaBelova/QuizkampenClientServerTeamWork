package com.example.quizkampenclientserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class serverListener
{
    public static void main(String[] args)
    {
        int port = 55557;
        try (ServerSocket serverSocket = new ServerSocket(port);){
            while(true) {
                //controller.setWelcomeText("Waiting for Player 1 to connect...");
                System.out.println("Searching for clients");
                Socket socket1 = serverSocket.accept();
                MatchController controller1 = new MatchController(socket1);
                System.out.println("Player 1 connected");
                Socket socket2 = serverSocket.accept();
                MatchController controller2 = new MatchController(socket2);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Player 2 connected");
                        try
                        {
                            new startGameWithTwoPlayers(controller1, controller2);
                        } catch (IOException | ClassNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                        System.out.println("CONNECTED");
                    }
                }).start();



            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);}

    }
}
