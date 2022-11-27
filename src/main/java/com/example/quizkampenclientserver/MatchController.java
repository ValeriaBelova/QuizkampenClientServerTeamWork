package com.example.quizkampenclientserver;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class MatchController implements Initializable
{
    int port;
    Socket socket1;
    Socket socket2;
    Player player1;
    Player player2;
    ClientController controller;
    ObjectOutputStream outputp1;
    ObjectInputStream inputp1;
    ObjectOutputStream outputp2;
    ObjectInputStream inputp2;
    String category;
    Question question;
    QuestionsDataBase db;

    @FXML
    public Button button;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Path p = Paths.get("src/main/java/com/example/quizkampenclientserver/questions.txt");
        db = new QuestionsDataBase(p);
        button.setText("Seeking clients...");
        this.port = 55557;
        this.player1 = new Player("Player 1");
        this.player2 = new Player("Player 2");
        player1.setCurrentPlayer(true);
        player1.setFirstPlayer(true);
    }

    public void buttonPressed(ActionEvent event) throws IOException, ClassNotFoundException
    {
        try (ServerSocket serverSocket = new ServerSocket(port);){
            while(true) {
                //controller.setWelcomeText("Waiting for Player 1 to connect...");
                System.out.println("hello");

                this.socket1 = serverSocket.accept();
                System.out.println("Player 1 connected");
                this.socket2 = serverSocket.accept();
                System.out.println("Player 2 connected");

                System.out.println();
                break;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);}
        this.outputp1 = new ObjectOutputStream(socket1.getOutputStream());
        this.inputp1 = new ObjectInputStream(socket1.getInputStream());
        this.outputp2 = new ObjectOutputStream(socket2.getOutputStream());
        this.inputp2 = new ObjectInputStream(socket2.getInputStream());
        button.setText("CONNECTION ESTABLISHED");
        prepareForGame();



    }
    public void prepareForGame() throws IOException, ClassNotFoundException
    {
        outputp1.flush();
        outputp2.flush();
        outputp1.writeObject(player1); // skicka player 1 till första användaren så att den kan läggas till i spelet
        outputp2.writeObject(player2); // skicka player 2 till andra användaren så att den kan läggas till i spelet
        goToScoreScene();
    }
    // Nu kan spelet börja
    // runGame();

    public void goToScoreScene() throws IOException, ClassNotFoundException
    {
        Question question1;
        Question question2;
        Question question3;
        Question question4;
        String category1;
        String category2;


        // RUNDA 1 ---------------------------------------------------------------

        // FIRST PLAYER-----------------------------------------------------------
        System.out.println("Sätter spelare 1 till currentPlayer...");
        player1.setCurrentPlayer(true);
        category1 = (String) inputp1.readObject(); // ta emot kategori från spelare 1
        System.out.println("Tog emot kategori från spelare 1");
        question1 = db.getRandomQuestionFromCategory(category1); // hämta en fråga från denna kategori
        System.out.println("Hämtade en random fråga från kategorin: " + category1);
        outputp1.writeObject(question1); // Skicka fråga 1 till spelare 1
        System.out.println("Skickade fråga 1 till spelare 1");

        category1 = (String) inputp1.readObject(); // ta emot kategori från spelare 1
        System.out.println("Tog emot kategori från spelare 1");
        question2 = db.getRandomQuestionFromCategory(category1); // hämta en fråga från denna kategori
        System.out.println("Hämtade en random fråga från kategorin: " + category1);
        outputp1.writeObject(question2); // Skicka fråga 2 till spelare 1
        System.out.println("Skickade fråga 2 till spelare 1");

        player1 = (Player) inputp1.readObject(); // player 1 har kört klart
        System.out.println("Spelare 1 har kört klart");
        // FIRST PLAYER DONE ------------------------------------------------------

        // SECOND PLAYER ----------------------------------------------------------
        player2.setCurrentPlayer(true);
        outputp2.writeObject(player2); // meddelar spelare 2 att den kan börja spelet
        outputp2.writeObject(category1); // skickar kategori 1 till spelare 2
        outputp2.writeObject(question1); // skickar fråga 1 till spelare 2

        category1 = (String) inputp2.readObject();
        outputp2.writeObject(question2); // skickar fråga 2 till spelare 2
        player2 = (Player) inputp2.readObject(); // player 2 har kört klart
        // RUNDA 2 ----------------------------------------------------------------
        // FIRST PLAYER-----------------------------------------------------------
        player1.setCurrentPlayer(true);
        outputp1.writeObject(player1); // meddelar spelare 1 att den kan börja spelet
        category1 = (String) inputp1.readObject(); // ta emot kategori från spelare 1
        System.out.println("Tog emot kategori från spelare 1");
        question1 = db.getRandomQuestionFromCategory(category1); // hämta en fråga från denna kategori
        System.out.println("Hämtade en random fråga från kategorin: " + category1);
        outputp1.writeObject(question1); // Skicka fråga 1 till spelare 1
        System.out.println("Skickade fråga 1 till spelare 1");

        category1 = (String) inputp1.readObject(); // ta emot kategori från spelare 1
        System.out.println("Tog emot kategori från spelare 1");
        question2 = db.getRandomQuestionFromCategory(category1); // hämta en fråga från denna kategori
        System.out.println("Hämtade en random fråga från kategorin: " + category1);
        outputp1.writeObject(question2); // Skicka fråga 2 till spelare 1
        System.out.println("Skickade fråga 2 till spelare 1");

        player1 = (Player) inputp1.readObject(); // player 1 har kört klart

        // SECOND PLAYER ----------------------------------------------------------
        player2.setCurrentPlayer(true);
        outputp2.writeObject(player2); // meddelar spelare 2 att den kan börja spelet
        outputp2.writeObject(category1); // skickar kategori 1 till spelare 2
        outputp2.writeObject(question1); // skickar fråga 1 till spelare 2

        category1 = (String) inputp2.readObject();
        outputp2.writeObject(question2); // skickar fråga 2 till spelare 2
        player2 = (Player) inputp2.readObject(); // player 2 har kört klart
        player1 = (Player) inputp2.readObject(); // player 2 har kört klart

    }

    public void runGame() throws IOException, ClassNotFoundException
    {
        Question question1;
        Question question2;
        Question question3;
        Question question4;
        String category1;
        String category2;

        // spelare 1 går till kategorier och kör en runda
        System.out.println("Steg 1");

        category1 = (String) inputp1.readObject();
        question1 = db.getRandomQuestionFromCategory(category1);
        outputp1.writeObject(question1); // Skicka fråga 1 till spelare 1

        System.out.println("Steg 2");

        category1 = (String) inputp1.readObject();
        question2 = db.getRandomQuestionFromCategory(category1);
        outputp1.writeObject(question2); // Skicka fråga 2 till spelare 1

        player1 = (Player) inputp1.readObject(); // player 1 har kört klart
        System.out.println("Steg 3");

        outputp2.writeObject(true); // meddelar spelare 2 att den kan börja spelet
        outputp2.writeObject(category1); // skickar kategori 1 till spelare 2
        outputp2.writeObject(question1); // skickar fråga 1 till spelare 2

        System.out.println("Steg 4");

        category1 = (String) inputp2.readObject();
        outputp2.writeObject(question2); // skickar fråga 2 till spelare 2
        player2 = (Player) inputp2.readObject(); // player 2 har kört klart


        System.out.println("Steg 5");

        // nu skickar vi att spelare 1 får starta nästa runda
        outputp1.writeObject(true);
        // skicka kategori 2 och fråga 3
        category2 = (String) inputp1.readObject();
        question3 = db.getRandomQuestionFromCategory(category2);
        outputp1.writeObject(question3);
        System.out.println("Steg 6");

        // skicka fråga 4
        category2 = (String) inputp1.readObject();
        System.out.println("Steg 5.1");
        question4 = db.getRandomQuestionFromCategory(category2);
        System.out.println("Steg 5.2");
        outputp1.writeObject(question4); // Skicka fråga 2 till spelare 1
        System.out.println("Steg 5.3");
        player1 = (Player) inputp1.readObject(); // player 1 har kört klart

        System.out.println("Steg 7");
        outputp2.writeObject(true); // meddelar spelare 2 att den kan börja spelet
        outputp2.writeObject(category2); // skickar kategori 2 till spelare 2
        outputp2.writeObject(question3); // skickar fråga 3 till spelare 2

        System.out.println("Steg 8");
        category2 = (String) inputp2.readObject();
        outputp2.writeObject(question4); // skickar fråga 4 till spelare 2
        player2 = (Player) inputp2.readObject(); // player 2 har kört klart

        System.out.println("Steg 9");
        outputp1.writeObject(false); // säg till spelare 1 att avsluta spelet
        System.out.println("Färdigt");
    }
}
