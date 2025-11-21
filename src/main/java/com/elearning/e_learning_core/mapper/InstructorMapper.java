package com.elearning.e_learning_core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.elearning.e_learning_core.Dtos.InstructorDto;
import com.elearning.e_learning_core.model.Instructor;

@Mapper(componentModel = "spring")
public interface InstructorMapper {

    @Mapping(target = "dob", ignore = true)

    InstructorDto toDto(Instructor course);

}
