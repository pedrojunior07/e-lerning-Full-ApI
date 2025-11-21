package com.elearning.e_learning_core.service;

import org.springframework.stereotype.Service;

import com.elearning.e_learning_core.Dtos.StatsDTO;
import com.elearning.e_learning_core.Repository.CourseRepository;
import com.elearning.e_learning_core.Repository.InstructorRepository;
import com.elearning.e_learning_core.Repository.StudentRepository;
import com.elearning.e_learning_core.model.Course.StatusCourse;

@Service
public class StatsService {

    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;

    public StatsService(CourseRepository courseRepository,
            InstructorRepository instructorRepository,
            StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
        this.studentRepository = studentRepository;
    }

    public StatsDTO getDashboardStats() {
        long onlineCourses = courseRepository.countByStatus(StatusCourse.PUBLISHED);
        long expertTutors = instructorRepository.count();
        long certifiedCourses = courseRepository.countByStatus(StatusCourse.PUBLISHED);
        long onlineStudents = studentRepository.count();

        return new StatsDTO(onlineCourses, expertTutors, certifiedCourses, onlineStudents);
    }
}
