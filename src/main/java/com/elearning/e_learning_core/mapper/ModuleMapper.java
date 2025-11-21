package com.elearning.e_learning_core.mapper;

import org.mapstruct.Mapper;

import com.elearning.e_learning_core.Dtos.ModuleDto;
import com.elearning.e_learning_core.model.CourseModule;

@Mapper(componentModel = "spring")
public interface ModuleMapper {

    ModuleDto toModuleDto(CourseModule courseModule);

}
