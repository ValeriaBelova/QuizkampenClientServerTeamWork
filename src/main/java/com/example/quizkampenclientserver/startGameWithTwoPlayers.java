package com.example.quizkampenclientserver;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class startGameWithTwoPlayers
{
    MatchController controller1;
    MatchController controller2;
    QuestionsDataBase db;

    public void setController1(MatchController controller1)
    {
        this.controller1 = controller1;
    }

    public void setController2(MatchController controller2)
    {
        this.controller2 = controller2;
    }

    public void setDb(QuestionsDataBase db)
    {
        this.db = db;
    }

    startGameWithTwoPlayers(MatchController controller1, MatchController controller2) throws IOException, ClassNotFoundException
    {
        Path p = Paths.get("src/main/java/com/example/quizkampenclientserver/questions.txt");
        setDb(new QuestionsDataBase(p));
        setController1(controller1);
        setController2(controller2);
        prepareForGame();
    }
    public void prepareForGame() throws IOException, ClassNotFoundException
    {
        controller1.player.setFirstPlayer(true); // den som väljer kategori
        controller1.player.setCurrentPlayer(true);
        controller1.output.writeObject(controller1.player); // skicka player 1 till första användaren så att den kan läggas till i spelet
        controller2.output.writeObject(controller2.player); // skicka player 2 till andra användaren så att den kan läggas till i spelet
        goToScoreScene();
    }

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
        controller1.player.setCurrentPlayer(true);
        category1 = (String) controller1.input.readObject(); // ta emot kategori från spelare 1
        System.out.println("Tog emot kategori från spelare 1");
        question1 = db.getRandomQuestionFromCategory(category1); // hämta en fråga från denna kategori
        System.out.println("Hämtade en random fråga från kategorin: " + category1);
        controller1.output.writeObject(question1); // Skicka fråga 1 till spelare 1
        System.out.println("Skickade fråga 1 till spelare 1");

        category1 = (String) controller1.input.readObject(); // ta emot kategori från spelare 1
        System.out.println("Tog emot kategori från spelare 1");
        question2 = db.getRandomQuestionFromCategory(category1); // hämta en fråga från denna kategori
        System.out.println("Hämtade en random fråga från kategorin: " + category1);
        controller1.output.writeObject(question2); // Skicka fråga 2 till spelare 1
        System.out.println("Skickade fråga 2 till spelare 1");

        controller1.player = (Player) controller1.input.readObject(); // player 1 har kört klart
        System.out.println("Spelare 1 har kört klart");
        // FIRST PLAYER DONE ------------------------------------------------------

        // SECOND PLAYER ----------------------------------------------------------
        controller2.player.setCurrentPlayer(true);
        controller2.output.writeObject(controller2.player); // meddelar spelare 2 att den kan börja spelet
        controller2.output.writeObject(category1); // skickar kategori 1 till spelare 2
        controller2.output.writeObject(question1); // skickar fråga 1 till spelare 2

        category1 = (String) controller2.input.readObject();
        controller2.output.writeObject(question2); // skickar fråga 2 till spelare 2
        controller2.player = (Player) controller2.input.readObject(); // player 2 har kört klart
        // RUNDA 2 ----------------------------------------------------------------
        // FIRST PLAYER-----------------------------------------------------------
        controller1.player.setCurrentPlayer(true);
        controller1.output.writeObject(controller1.player); // meddelar spelare 1 att den kan börja spelet
        category1 = (String) controller1.input.readObject(); // ta emot kategori från spelare 1
        System.out.println("Tog emot kategori från spelare 1");
        question1 = db.getRandomQuestionFromCategory(category1); // hämta en fråga från denna kategori
        System.out.println("Hämtade en random fråga från kategorin: " + category1);
        controller1.output.writeObject(question1); // Skicka fråga 1 till spelare 1
        System.out.println("Skickade fråga 1 till spelare 1");

        category1 = (String) controller1.input.readObject(); // ta emot kategori från spelare 1
        System.out.println("Tog emot kategori från spelare 1");
        question2 = db.getRandomQuestionFromCategory(category1); // hämta en fråga från denna kategori
        System.out.println("Hämtade en random fråga från kategorin: " + category1);
        controller1.output.writeObject(question2); // Skicka fråga 2 till spelare 1
        System.out.println("Skickade fråga 2 till spelare 1");

        controller1.player = (Player) controller1.input.readObject(); // player 1 har kört klart

        // SECOND PLAYER ----------------------------------------------------------
        controller2.player.setCurrentPlayer(true);
        controller2.output.writeObject(controller2.player); // meddelar spelare 2 att den kan börja spelet
        controller2.output.writeObject(category1); // skickar kategori 1 till spelare 2
        controller2.output.writeObject(question1); // skickar fråga 1 till spelare 2

        category1 = (String) controller2.input.readObject();
        controller2.output.writeObject(question2); // skickar fråga 2 till spelare 2
        controller2.player = (Player) controller2.input.readObject(); // player 2 har kört klart
        controller1.player = (Player) controller2.input.readObject(); // player 2 har kört klart
    }

}
