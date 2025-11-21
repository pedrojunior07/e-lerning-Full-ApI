package com.elearning.e_learning_core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.elearning.e_learning_core.Dtos.ApiResponse;
import com.elearning.e_learning_core.Dtos.StudentDTO;
import com.elearning.e_learning_core.Dtos.StudentSummaryDTO;
import com.elearning.e_learning_core.Repository.StudentRepository;
import com.elearning.e_learning_core.mapper.StudentDTOMapper;
import com.elearning.e_learning_core.model.Student;

@Service
public class StudentService {

        @Autowired
        private StudentRepository repositoryRepository;
        @Autowired
        private StudentDTOMapper studentDTOMapper;

        public Page<StudentSummaryDTO> listStudentsSummary(Pageable pageable) {
                return repositoryRepository.findAll(pageable)
                                .map(student -> new StudentSummaryDTO(
                                                student.getId(),
                                                student.getFirstName() + " " + student.getLastName(),
                                                student.getCreatedAt(),
                                                0));
        }

        public ApiResponse<?> getStudentById(Long id) {

                Student student = repositoryRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Instructor not found with id: " + id));

                StudentDTO instructorDto = studentDTOMapper.toDto(student);

                return new ApiResponse<>(
                                "success",
                                "Instructor retrieved successfully",
                                200,
                                instructorDto);
        }

}
