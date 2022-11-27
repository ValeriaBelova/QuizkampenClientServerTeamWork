package com.example.quizkampenclientserver;

import java.io.Serializable;
import java.util.Arrays;

public class Question implements Serializable
{
    String description;
    int correctAnswerindex;
    String[] answers;
    boolean taken;

    Question(String d, String[] a, int c){
        this.description = d;
        this.answers = a;
        this.correctAnswerindex = c;
        this.taken = false;
    }

    public boolean isTaken()
    {
        return taken;
    }

    public void setTaken(boolean taken)
    {
        this.taken = taken;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getCorrectAnswerindex()
    {
        return correctAnswerindex;
    }

    public void setCorrectAnswerindex(int correctAnswerindex)
    {
        this.correctAnswerindex = correctAnswerindex;
    }

    public String[] getAnswers()
    {
        return answers;
    }

    public void setAnswers(String[] answers)
    {
        this.answers = answers;
    }

    @Override
    public String toString()
    {
        return "Question{" +
                "description='" + description + '\'' +
                ", correctAnswerindex=" + correctAnswerindex +
                ", answers=" + Arrays.toString(answers) +
                '}';
    }
}
