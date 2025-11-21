package com.elearning.e_learning_core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.elearning.e_learning_core.Dtos.QuizRequestDTO;
import com.elearning.e_learning_core.model.Course;
import com.elearning.e_learning_core.model.Quiz;

@Mapper(componentModel = "spring")
public interface QuizMapper {

    @Mapping(source = "quiz.title", target = "title")
    @Mapping(source = "quiz.numberOfQuestions", target = "numberOfQuestions")
    @Mapping(source = "quiz.duration", target = "duration")
    @Mapping(source = "quiz.id", target = "courseId")
    @Mapping(source = "quiz.course.title", target = "courseName")
    QuizRequestDTO toDto(Quiz quiz);
}