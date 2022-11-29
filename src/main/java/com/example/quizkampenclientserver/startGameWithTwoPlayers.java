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
    public void goToScoreSceneLoop() throws IOException, ClassNotFoundException
    {
        player1 = controller1.player;
        player2 = controller2.player;
        controller1.output.writeObject(player2); // Skicka spelare 2 till spelare 1 så att spelare 1 kan uppdatera labels
        controller2.output.writeObject(player1); // Skicka spelare 1 till spelare 2 så att spelare 2 kan uppdatera labels
        String currentCategory;
        Question currentQuestion;
        ArrayList<Question> questionsArray= new ArrayList<>();
        for (int round = 1; round <= player1.getNumberOfRoundsPerGame(); round++){
            player1.setCurrentPlayer(true);
            if(round > 1){
                controller1.output.writeObject(player1);
            }
            currentCategory = (String) controller1.input.readObject(); // ta emot kategori från spelare 1
            for (int turn = 1; turn <= player1.getNumberOfTurnsPerRound(); turn++){
                currentQuestion = db.getRandomQuestionFromCategory(currentCategory); // hämta en fråga från denna kategori
                questionsArray.add(currentQuestion);
                controller1.output.writeObject(currentQuestion); // Skicka fråga 1 till spelare 1
            }
            player1 = (Player) controller1.input.readObject(); // Tar emot meddelande från klienten att spelare 1 har kört klart rundan
            controller1.output.writeObject(player2); // Skicka Spelare 2 till spelare 1 så att spelare 1 kan uppdatera labels

            player2.setCurrentPlayer(true);
            controller2.output.writeObject(player2); // meddelar spelare 2 att den kan börja spelet
            for (Question q: questionsArray){
                controller2.output.writeObject(q);
            }
            questionsArray.clear();
            player2 = (Player) controller2.input.readObject(); // Tar emot meddelande från klienten att spelare 2 har kört klart rundan
            if(round == player1.getNumberOfRoundsPerGame()){
                controller1.output.writeObject(player2); // Skicka spelare 2 till spelare 1 så att den kan uppdatera poäng och avsluta
                controller2.output.writeObject(player1); // skicka spelare 1 så att poängen kan uppdateras
            }
            controller2.output.writeObject(player1); // Skicka Spelare 1 till spelare 2 så att spelare 2 kan uppdatera labels


        }
    }

}
