package com.example.android.expandablelistviewexample;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

public class QuestionAnswerModel implements Serializable {

    @PropertyName("question")
    private String question;
    @PropertyName("answer")
    private String answer;


    public QuestionAnswerModel() {
    }

    public QuestionAnswerModel(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
