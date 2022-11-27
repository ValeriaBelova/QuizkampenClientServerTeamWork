package com.example.quizkampenclientserver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class QuestionsDataBase
{
    HashMap<String, ArrayList<Question>> db = new HashMap<>();
    int numberOfQuestions;
    public QuestionsDataBase(Path p)
    {
        this.db = loadDataBase(p);
        this.numberOfQuestions = 0;
    }

    private HashMap<String, ArrayList<Question>> loadDataBase(Path p)
    {
        String tempLine;
        String questionDescription;
        String[] alternatives = new String[4];
        String categoryString;
        int correctAnswerIndex = 0;
        try (FileReader fr = new FileReader(String.valueOf(p));
             BufferedReader br = new BufferedReader(fr))
        {
            while ((tempLine = br.readLine()) != null)
            {
                // Hämtar den första raden som är självaste frågan
                questionDescription = tempLine.strip();
                // går till nästa rad och hämtar svarsalternativen
                if ((tempLine = br.readLine()) != null)
                {
                    alternatives = tempLine.split(",");
                }
                // går till nästa rad och hämtar index till rätt svar
                if ((tempLine = br.readLine()) != null)
                {
                    correctAnswerIndex = Integer.parseInt(tempLine);

                }
                // går till nästa rad och hämtar kategorin till frågan
                if ((tempLine = br.readLine()) != null)
                {
                    categoryString = tempLine.strip();
                }
                else
                {
                    break;
                }
                // skapa frågan
                Question q = new Question(questionDescription, alternatives, correctAnswerIndex);
                // lägg till frågan i databasen
                addQuestionToDatabase(q, categoryString);
                numberOfQuestions++;
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return db;
    }
    public void addQuestionToDatabase(Question question, String category){
        ArrayList<Question> arrayOfQuestions;
        if(!db.containsKey(category)){
            arrayOfQuestions = new ArrayList<>();
            arrayOfQuestions.add(question);
            db.put(category, arrayOfQuestions);
        }
        else{
            arrayOfQuestions = db.get(category);
            arrayOfQuestions.add(question);
        }
    }

    public ArrayList<Question> getQuestionsFromCategory(String category){
        return db.get(category);
    }

    public Question getRandomQuestionFromCategory(String category){
        Random rand = new Random();
        int randomIndex;
        ArrayList<Question> arrayOfQuestions = getQuestionsFromCategory(category);
        while (true){
            randomIndex = rand.nextInt(getNumberOfQuestionsFromCategory(category));
            Question question = arrayOfQuestions.get(randomIndex);
            if (!question.isTaken()){
                question.taken = true;
                return question;
            }
        }
    }

    public int getNumberOfQuestionsFromCategory(String category){
        return db.get(category).size();
    }

    public int getNumberOfQuestions(){
        return this.numberOfQuestions;
    }

    public void showAllCategories(){
        System.out.println("Categories: \n" );
        for(String s : db.keySet()){
            System.out.println(s);
        }
    }

    public void showDatabase(){
        for(String s : db.keySet()){
            System.out.println("Category " + s + " has questions: ");
            System.out.println(db.get(s));
        }
    }

    public static void main(String[] args) throws FileNotFoundException
    {
    }
}



/*

 */