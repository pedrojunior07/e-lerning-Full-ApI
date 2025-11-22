package com.elearning.e_learning_core.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.elearning.e_learning_core.Dtos.ApiResponse;
import com.elearning.e_learning_core.Dtos.InstructorDto;
import com.elearning.e_learning_core.Repository.InstructorRepository;
import com.elearning.e_learning_core.mapper.InstructorMapper;
import com.elearning.e_learning_core.model.Instructor;
import com.elearning.e_learning_core.model.Course;

@Service
public class InstructorService {

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private InstructorMapper instructorMapper;

    public ApiResponse<?> getInstructorById(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + id));

        InstructorDto instructorDto = instructorMapper.toDto(instructor);

        return new ApiResponse<>(
                "success",
                "Instructor retrieved successfully",
                200,
                instructorDto);
    }

    public Page<InstructorDto> listInstructors(Pageable pageable) {
        return instructorRepository.findAll(pageable)
                .map(instructorMapper::toDto);
    }

    public ApiResponse<?> updateInstructor(Long id, InstructorDto dto) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + id));

        if (dto.getFirstName() != null) instructor.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) instructor.setLastName(dto.getLastName());
        if (dto.getEmail() != null) instructor.setEmail(dto.getEmail());
        if (dto.getPhoneNumber() != null) instructor.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getGender() != null) instructor.setGender(dto.getGender());
        if (dto.getBio() != null) instructor.setBio(dto.getBio());

        instructorRepository.save(instructor);

        return new ApiResponse<>(
                "success",
                "Instructor updated successfully",
                200,
                instructorMapper.toDto(instructor));
    }

    public ApiResponse<?> deleteInstructor(Long id) {
        if (!instructorRepository.existsById(id)) {
            return new ApiResponse<>("error", "Instructor not found", 404, null);
        }

        instructorRepository.deleteById(id);

        return new ApiResponse<>(
                "success",
                "Instructor deleted successfully",
                200,
                null);
    }

    public ApiResponse<?> getInstructorCourses(Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + instructorId));

        List<Course> courses = instructor.getCourses();

        return new ApiResponse<>(
                "success",
                "Instructor courses retrieved successfully",
                200,
                courses.stream().map(course -> new Object() {
                    public Long id = course.getId();
                    public String title = course.getTitle();
                    public String thumbnail = course.getThumbnailPath();
                    public String status = course.getStatus() != null ? course.getStatus().name() : null;
                    public int studentsCount = course.getStudents() != null ? course.getStudents().size() : 0;
                }).collect(Collectors.toList()));
    }

    public ApiResponse<?> getInstructorStats(Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + instructorId));

        List<Course> courses = instructor.getCourses();
        int totalStudents = courses.stream()
                .mapToInt(c -> c.getStudents() != null ? c.getStudents().size() : 0)
                .sum();

        return new ApiResponse<>(
                "success",
                "Instructor stats retrieved successfully",
                200,
                new Object() {
                    public int totalCourses = courses.size();
                    public int totalStudentsEnrolled = totalStudents;
                    public String instructorName = instructor.getFirstName() + " " + instructor.getLastName();
                });
    }

    public long countInstructors() {
        return instructorRepository.count();
    }
}
