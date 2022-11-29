package com.example.quizkampenclientserver;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class startGameWithTwoPlayers
{
    MatchController controller1;
    MatchController controller2;
    QuestionsDataBase db;
    Player player1;
    Player player2;
    Player currentPlayer;
    int currentRound = 1;
    int currentTurn = 1;

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
        ArrayList<String> scoreLabels1 =  new ArrayList<String>(Arrays.asList("score11", "score12", "score13", "score14", "score15", "score16"));
        controller1.player.setScoreLabels(scoreLabels1);
        ArrayList<String> scoreLabels2=  new ArrayList<String>(Arrays.asList("score21", "score22", "score23", "score24", "score25", "score26"));
        controller2.player.setScoreLabels(scoreLabels2);
        controller1.output.writeObject(controller1.player); // skicka player 1 till första användaren så att den kan läggas till i spelet
        controller2.output.writeObject(controller2.player); // skicka player 2 till andra användaren så att den kan läggas till i spelet
        // goToScoreScene();
        goToScoreSceneLoop();
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
        controller1.output.writeObject(controller2.player);
        controller2.output.writeObject(controller1.player);
        System.out.println("Skickar spelarna så att de kan skriva ut alla labels");
        // FIRST PLAYER-----------------------------------------------------------
        System.out.println("Runda 1 börjar");
        System.out.println("Första spelarens tur");
        controller1.player.setCurrentPlayer(true);
        category1 = (String) controller1.input.readObject(); // ta emot kategori från spelare 1
        question1 = db.getRandomQuestionFromCategory(category1); // hämta en fråga från denna kategori
        controller1.output.writeObject(question1); // Skicka fråga 1 till spelare 1


        category1 = (String) controller1.input.readObject(); // ta emot kategori från spelare 1
        question2 = db.getRandomQuestionFromCategory(category1); // hämta en fråga från denna kategori
        controller1.output.writeObject(question2); // Skicka fråga 2 till spelare 1

        System.out.println("Spelare 1 har kört klart");
        controller1.player = (Player) controller1.input.readObject(); // player 1 har kört klart
        controller1.output.writeObject(controller2.player);
        System.out.println("Skickat spelare 2 till spelare 1 för att skriva ut resultat");
        // FIRST PLAYER DONE ------------------------------------------------------
        System.out.println("Spelare 2s tur");
        // SECOND PLAYER ----------------------------------------------------------
        controller2.player.setCurrentPlayer(true);
        controller2.output.writeObject(controller2.player); // meddelar spelare 2 att den kan börja spelet
        controller2.output.writeObject(category1); // skickar kategori 1 till spelare 2
        controller2.output.writeObject(question1); // skickar fråga 1 till spelare 2

        category1 = (String) controller2.input.readObject();
        controller2.output.writeObject(question2); // skickar fråga 2 till spelare 2
        controller2.player = (Player) controller2.input.readObject(); // player 2 har kört klart
        System.out.println("Spelare 2 har kört klart");
        controller2.output.writeObject(controller1.player);
        System.out.println("Skickar spelare 1 till spelare 2 för att skriva ut resultat");
        // RUNDA 2 ----------------------------------------------------------------

        System.out.println("Runda 2 börjar");
        System.out.println("Första spelarens tur");
        // FIRST PLAYER-----------------------------------------------------------

        controller1.player.setCurrentPlayer(true);
        System.out.println("Satt spelare 1 om currentplayer");
        controller1.output.writeObject(controller1.player); // meddelar spelare 1 att den kan börja spelet
        System.out.println("Meddela spelare 1 att den kan börja spelet och gå till kategorier");
        category1 = (String) controller1.input.readObject(); // ta emot kategori från spelare 1
        System.out.println("Tog emot kategori " + category1);
        question1 = db.getRandomQuestionFromCategory(category1); // hämta en fråga från denna kategori
        controller1.output.writeObject(question1); // Skicka fråga 1 till spelare 1
        System.out.println("Skickade fråga 1 till spelare 1");

        category1 = (String) controller1.input.readObject(); // ta emot kategori från spelare 1
        System.out.println("Tog emot nästa kategori från spelare 1");
        question2 = db.getRandomQuestionFromCategory(category1); // hämta en fråga från denna kategori
        controller1.output.writeObject(question2); // Skicka fråga 2 till spelare 1
        System.out.println("Skickade fråga 2 från kategori: " + category1);

        controller1.player = (Player) controller1.input.readObject(); // player 1 har kört klart
        System.out.println("Spelare 1 har kört klart");
        controller1.output.writeObject(controller2.player);
        System.out.println("Skickat spelare 2 till spelare 1 för att skriva ut resultat");

        // SECOND PLAYER ----------------------------------------------------------
        System.out.println("Spelare 2s tur");
        controller2.player.setCurrentPlayer(true);
        controller2.output.writeObject(controller2.player); // meddelar spelare 2 att den kan börja spelet
        controller2.output.writeObject(category1); // skickar kategori 1 till spelare 2
        controller2.output.writeObject(question1); // skickar fråga 1 till spelare 2

        category1 = (String) controller2.input.readObject();
        controller2.output.writeObject(question2); // skickar fråga 2 till spelare 2
        controller2.player = (Player) controller2.input.readObject(); // player 2 har kört klart
        System.out.println("Spelare 2 har kört klart");

        System.out.println("Sista ronden har körts");
        controller1.output.writeObject(controller2.player); // Skicka spelare 2 till spelare 1 så att den kan uppdatera poäng och avsluta
        System.out.println("Skicka spelare 2 till spelare så att den kan uppdatera poäng och avsluta");
        controller2.output.writeObject(controller1.player); // skicka spelare 1 så att poängen kan uppdateras
        System.out.println("Skickar spelare 1 till spelare 2 för att skriva ut resultat");
        System.out.println("Spelet är slut");
    }


    public void goToScoreSceneLoop() throws IOException, ClassNotFoundException
    {
        player1 = controller1.player;
        player2 = controller2.player;
        controller1.output.writeObject(player2); // Skicka spelare 2 till spelare 1 så att spelare 1 kan uppdatera labels
        controller2.output.writeObject(player1); // Skicka spelare 1 till spelare 2 så att spelare 2 kan uppdatera labels
        System.out.println("Skickar spelarna så att de kan skriva ut alla labels");
        String currentCategory;
        Question currentQuestion;
        ArrayList<Question> questionsArray= new ArrayList<>();
        for (int round = 1; round <= player1.getNumberOfRoundsPerGame(); round++){
            player1.setCurrentPlayer(true);
            System.out.println("round: " + round);
            if(round > 1){
                controller1.output.writeObject(player1);
            }
            System.out.println("Väntar på att ta emot kategori från spelare 1");
            currentCategory = (String) controller1.input.readObject(); // ta emot kategori från spelare 1
            System.out.println("Tog emot kategori från spelare 1");
            for (int turn = 1; turn <= player1.getNumberOfTurnsPerRound(); turn++){
                System.out.println("Turn: " + turn);
                currentQuestion = db.getRandomQuestionFromCategory(currentCategory); // hämta en fråga från denna kategori
                questionsArray.add(currentQuestion);
                System.out.println("Ska skicka frågan: " + currentQuestion);
                controller1.output.writeObject(currentQuestion); // Skicka fråga 1 till spelare 1
                System.out.println("Har skickat frågan: " + currentQuestion);
            }
            System.out.println("väntar på att spelare 1 ska köra klart");
            player1 = (Player) controller1.input.readObject(); // Tar emot meddelande från klienten att spelare 1 har kört klart rundan
            System.out.println("Spelare 1 har kört klart");
            controller1.output.writeObject(player2); // Skicka Spelare 2 till spelare 1 så att spelare 1 kan uppdatera labels
            System.out.println("Har skickat spelare 2 till spelare 1");

            player2.setCurrentPlayer(true);
            controller2.output.writeObject(player2); // meddelar spelare 2 att den kan börja spelet
            System.out.println("Spelare 2s tur");
            System.out.println("QuestionsArray: " + questionsArray);
            for (Question q: questionsArray){
                controller2.output.writeObject(q);
            }
            questionsArray.clear();
            player2 = (Player) controller2.input.readObject(); // Tar emot meddelande från klienten att spelare 2 har kört klart rundan
            System.out.println("Spelare 2 har kört klart ");
            System.out.println("Ett");
            System.out.println(round);
            System.out.println(player1.getNumberOfRoundsPerGame());
            if(round == player1.getNumberOfRoundsPerGame()){
                System.out.println("två");
                controller1.output.writeObject(player2); // Skicka spelare 2 till spelare 1 så att den kan uppdatera poäng och avsluta
                controller2.output.writeObject(player1); // skicka spelare 1 så att poängen kan uppdateras
            }
            System.out.println("tre");
            controller2.output.writeObject(player1); // Skicka Spelare 1 till spelare 2 så att spelare 2 kan uppdatera labels
            System.out.println("fyra");


        }
    }

}
