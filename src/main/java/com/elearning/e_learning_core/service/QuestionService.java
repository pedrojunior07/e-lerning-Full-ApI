package com.elearning.e_learning_core.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elearning.e_learning_core.Dtos.ApiResponse;
import com.elearning.e_learning_core.Dtos.QuestionDTO;
import com.elearning.e_learning_core.Repository.QuestionRepository;
import com.elearning.e_learning_core.Repository.QuizRepository;
import com.elearning.e_learning_core.model.Question;
import com.elearning.e_learning_core.model.Quiz;

import jakarta.persistence.EntityNotFoundException;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    public ApiResponse<?> saveQuestion(Long quizId, String questionText, List<String> options, int correctOption) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException("Quiz not found with id: " + quizId));

        Question question = new Question();
        question.setQuiz(quiz);
        question.setQuestionText(questionText);
        question.setOptions(options);
        question.setCorrectOption(correctOption);

        questionRepository.save(question);

        QuestionDTO dto = new QuestionDTO(
                question.getId(),
                question.getQuestionText(),
                question.getOptions(),
                question.getCorrectOption(),
                quiz.getId());

        return new ApiResponse<>("Success", "Question saved successfully", 200, dto);
    }

    public List<QuestionDTO> listQuestionByQuizId(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException("Quiz not found with ID: " + quizId));

        List<Question> questions = questionRepository.findByQuiz(quiz);

        return questions.stream()
                .map(q -> new QuestionDTO(
                        q.getId(),
                        q.getQuestionText(),
                        q.getOptions(),
                        q.getCorrectOption(),
                        quiz.getId()))
                .collect(Collectors.toList());
    }
}
