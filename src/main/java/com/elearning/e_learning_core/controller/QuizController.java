package com.elearning.e_learning_core.controller;

import com.elearning.e_learning_core.model.Quiz;
import com.elearning.e_learning_core.service.QuizService;
import com.elearning.e_learning_core.Dtos.QuizRequest;
import com.elearning.e_learning_core.Dtos.QuizRequestDTO;
import com.elearning.e_learning_core.mapper.QuizMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/quizzes")
public class QuizController {

    private final QuizService quizService;
    private final QuizMapper quizMapper;

    @Autowired
    public QuizController(QuizService quizService, QuizMapper quizMapper) {
        this.quizService = quizService;
        this.quizMapper = quizMapper;
    }

    @PostMapping
    public ResponseEntity<QuizRequestDTO> createQuiz(@RequestBody QuizRequest quizRequest) {
        Quiz createdQuiz = quizService.createQuiz(quizRequest);
        QuizRequestDTO quizResponse = quizMapper.toDto(createdQuiz);
        return ResponseEntity.ok(quizResponse);
    }

    @GetMapping
    public ResponseEntity<List<QuizRequestDTO>> getAllQuizzes() {
        List<QuizRequestDTO> quizzes = quizService.getAllQuizzes();
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizRequestDTO> getQuizById(@PathVariable Long id) {
        Optional<Quiz> quiz = quizService.getQuizById(id);
        if (quiz.isPresent()) {
            QuizRequestDTO quizResponse = quizMapper.toDto(quiz.get());
            return ResponseEntity.ok(quizResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuizRequestDTO> updateQuiz(@PathVariable Long id, @RequestBody QuizRequest quizRequest) {
        Optional<Quiz> existingQuiz = quizService.getQuizById(id);
        if (existingQuiz.isPresent()) {
            Quiz updatedQuiz = existingQuiz.get();
            updatedQuiz.setTitle(quizRequest.getTitle());
            updatedQuiz.setNumberOfQuestions(quizRequest.getNumberOfQuestions());
            updatedQuiz.setDuration(quizRequest.getDuration());
            updatedQuiz = quizService.updateQuiz(updatedQuiz);
            QuizRequestDTO quizResponse = quizMapper.toDto(updatedQuiz);
            return ResponseEntity.ok(quizResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        boolean isDeleted = quizService.deleteQuiz(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
