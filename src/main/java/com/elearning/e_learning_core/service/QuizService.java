package com.elearning.e_learning_core.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elearning.e_learning_core.Dtos.QuizRequest;
import com.elearning.e_learning_core.Dtos.QuizRequestDTO;
import com.elearning.e_learning_core.Repository.CourseRepository;
import com.elearning.e_learning_core.Repository.QuizRepository;
import com.elearning.e_learning_core.mapper.QuizMapper;
import com.elearning.e_learning_core.model.Course;
import com.elearning.e_learning_core.model.Quiz;

import jakarta.persistence.EntityNotFoundException;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    private final CourseRepository courseRepository;

    @Autowired
    private QuizMapper quizMapper;;

    @Autowired
    public QuizService(QuizRepository quizRepository, CourseRepository courseRepository) {
        this.quizRepository = quizRepository;
        this.courseRepository = courseRepository;
    }

    public Quiz createQuiz(QuizRequest quiz) {
        Course course = courseRepository.findById(quiz.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + quiz.getCourseId()));

        Quiz quizEntity = new Quiz();
        quizEntity.setTitle(quiz.getTitle());
        quizEntity.setCourse(course);
        quizEntity.setNumberOfQuestions(quiz.getNumberOfQuestions());
        quizEntity.setDuration(quiz.getDuration());
        return quizRepository.save(quizEntity);
    }

    public List<QuizRequestDTO> getAllQuizzes() {

        List<Quiz> quizzes = quizRepository.findAll();

        return quizzes.stream()
                .map(quizMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findById(id);
    }

    public Quiz updateQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public boolean deleteQuiz(Long id) {
        if (quizRepository.existsById(id)) {
            quizRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
