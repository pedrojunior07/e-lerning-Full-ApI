package com.elearning.e_learning_core.Dtos;

import java.util.List;

public class QuestionRequest {

    private Long quizId; // ID do quiz associado
    private String questionText; // Texto da pergunta
    private List<String> options; // Opções de respostas
    private int correctOption; // Índice da opção correta (0, 1, 2, 3)

    public QuestionRequest() {
    }

    

    public QuestionRequest(Long quizId, String questionText, List<String> options, int correctOption) {
        this.quizId = quizId;
        this.questionText = questionText;
        this.options = options;
        this.correctOption = correctOption;
    }



    // Getters and Setters
    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public int getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(int correctOption) {
        this.correctOption = correctOption;
    }
}