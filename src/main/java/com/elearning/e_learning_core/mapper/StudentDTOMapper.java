package com.elearning.e_learning_core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.elearning.e_learning_core.Dtos.InstructorDto;
import com.elearning.e_learning_core.Dtos.StudentDTO;
import com.elearning.e_learning_core.model.Instructor;
import com.elearning.e_learning_core.model.Student;

@Mapper(componentModel = "spring")
public interface StudentDTOMapper {
    @Mapping(target = "dob", ignore = true)
    StudentDTO toDto(Student course);
}
