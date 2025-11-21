package com.elearning.e_learning_core.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "question")
public class Question extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(nullable = false)
    private String questionText;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "option")
    private List<String> options;

    @Column(nullable = false)
    private int correctOption; // Índice da opção correta (0, 1, 2, 3)

    public Question() {
    }

    public Question(Quiz quiz, String questionText, List<String> options, int correctOption) {
        this.quiz = quiz;
        this.questionText = questionText;
        this.options = options;
        this.correctOption = correctOption;
    }

    // Getters and setters

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
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
