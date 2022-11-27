package com.example.quizkampenclientserver;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class gameController implements Initializable
{
    @FXML
    private Label playerLabel;
    @FXML
    private Label roundLabel;
    @FXML
    private Label turnLabel;
    @FXML
    private Button answer1Button;
    @FXML
    private Label questionLabel;
    @FXML
    private GridPane gameGridPane;

    Player player1;
    Player player2;
    Path p = Paths.get("src/main/java/Server/questions.txt");
    QuestionsDataBase db = new QuestionsDataBase(p);
    String category;
    Question currentQuestion;
    ArrayList<Question> questionsGenerated = new ArrayList<>();
    ArrayList<String> categoriesArray = new ArrayList<>();
    int questionsAnswered = 0;
    int currentTurn = 1;
    int numberOfTurnsPerRound = 2;
    int currentRound = 1;
    int numberOfRoundsPerGame = 2;
    int points = 0;
    boolean firstPlayerTurn = true;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
    }

    public void startQuiz(String cat, int round,Player p1, Player p2)
    {
        this.player1 = p1;
        this.player2 = p2;
        this.currentTurn = 1;
        this.category = cat;
        this.currentRound = round;
        setQuestion();
    }
    public void getQuestion(){
        if (firstPlayerTurn)
        {
            currentQuestion = db.getRandomQuestionFromCategory(category);
            questionsGenerated.add(currentQuestion);
        }
        // om det är den andra personen som kör så tar man samma frågor som första personen körde
        else
        {
            currentQuestion = questionsGenerated.get(questionsAnswered);
        }
    }
    public void setQuestion()
    {
        getQuestion();
        questionLabel.setText(currentQuestion.getDescription());
        categoriesArray.add(category);
        int index = 0;
        for (Node n : gameGridPane.getChildren())
        {
            try
            {
                Button b = (Button) n;
                b.setBackground(Background.fill(Color.LIGHTSKYBLUE));
                b.setText(currentQuestion.getAnswers()[index]);
                index++;
            } catch (ClassCastException e)
            {
                System.out.println("Fix");
            }
        }
        if(firstPlayerTurn){
            playerLabel.setText("Player: " + player1.getName());
        }
        else{
            playerLabel.setText("Player: " + player2.getName());
        }
        roundLabel.setText("Round: " + currentRound + "/" + numberOfRoundsPerGame);
        turnLabel.setText("Question: " + currentTurn + "/" + numberOfTurnsPerRound);
    }

    public void checkAnswer(ActionEvent actionEvent)
    {
        Button button = (Button) actionEvent.getSource();
        // Hämta rätta svaret på frågan
        String correctAnswer = currentQuestion.getAnswers()[currentQuestion.getCorrectAnswerindex()];
        if (button.getText().equals(correctAnswer))
        {
            if(firstPlayerTurn){
                player1.setPoints(player1.getPoints() + 1);
            }
            else{
                player2.setPoints(player2.getPoints() + 1);
            }
        }
        questionsAnswered++;
        showCorrectAnswer();
        PauseTransition wait = new PauseTransition(Duration.seconds(2));
        wait.setOnFinished(event ->
        {
            if (currentTurn < numberOfTurnsPerRound)
            {
                nextTurn();
            }
            else if(firstPlayerTurn)
            {
                //nextRound();
                switchPlayer();
            }
            else {
                try
                {
                    nextRound();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        wait.play();
    }

    public void showCorrectAnswer()
    {
        String correctAnswer = currentQuestion.getAnswers()[currentQuestion.getCorrectAnswerindex()];
        for (Node n : gameGridPane.getChildren())
        {
            try
            {
                Button b = (Button) n;
                if (b.getText().equals(correctAnswer))
                {
                    b.setBackground(Background.fill(Color.LIMEGREEN));
                } else
                {
                    b.setBackground(Background.fill(Color.RED));
                }

            } catch (ClassCastException e)
            {
                System.out.println("This is not a button");
            }
        }
    }
    public void nextTurn()
    {
        currentTurn++;
        setQuestion();
    }

    public void nextRound() throws IOException
    {
        if (currentRound < numberOfRoundsPerGame)
        {
            currentRound++;
            firstPlayerTurn = true;
            questionsGenerated.clear();
            switchToChoiceOfCategoryScene();
        }
        else{
            endGame();
        }
    }

    public void reset()
    {
        // Se till så att man kan välja dessa frågor igen
        for (Question q : questionsGenerated)
        {
            q.setTaken(false);
        }
    }

    private void switchPlayer()
    {
        firstPlayerTurn = false;
        //currentRound = 1;
        category = categoriesArray.get(0);
        questionsAnswered = 0;
        startQuiz(category, currentRound,player1, player2);
    }

    private void endGame() throws IOException
    {
        reset();
        switchToScoreScene();
    }

    public void switchToChoiceOfCategoryScene() throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Client.class.getResource("choiceOfCategoryScreen.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        HelloController controller = loader.getController();
        controller.setPlayer1(player1);
        controller.setPlayer2(player2);
        controller.setRound(currentRound);
        Stage stage = (Stage) answer1Button.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToStartScene() throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Client.class.getResource("startScene.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage = (Stage) answer1Button.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void switchToScoreScene() throws IOException
    {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Client.class.getResource("scoreScene.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage = (Stage) answer1Button.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


}