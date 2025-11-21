package com.elearning.e_learning_core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.e_learning_core.Dtos.ApiResponse;
import com.elearning.e_learning_core.Dtos.QuestionDTO;
import com.elearning.e_learning_core.service.QuestionService;

@RestController
@RequestMapping("/questions")

public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createQuestion(
            @RequestBody QuestionDTO questionDTO) {

        ApiResponse<?> response = questionService.saveQuestion(questionDTO.getQuizId(), questionDTO.getQuestionText(),
                questionDTO.getOptions(), questionDTO.getCorrectOption());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByQuiz(@PathVariable Long quizId) {
        List<QuestionDTO> questions = questionService.listQuestionByQuizId(quizId);
        return ResponseEntity.ok(questions);
    }
}
