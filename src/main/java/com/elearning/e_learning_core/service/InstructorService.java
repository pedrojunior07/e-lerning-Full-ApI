package com.elearning.e_learning_core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elearning.e_learning_core.Dtos.ApiResponse;
import com.elearning.e_learning_core.Dtos.InstructorDto;
import com.elearning.e_learning_core.Repository.InstructorRepository;
import com.elearning.e_learning_core.mapper.InstructorMapper;
import com.elearning.e_learning_core.model.Instructor;

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

}
