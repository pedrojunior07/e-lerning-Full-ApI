package com.elearning.e_learning_core.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.elearning.e_learning_core.Dtos.AnnouncementDto;
import com.elearning.e_learning_core.model.Announcement;

@Mapper(componentModel = "spring")
public interface AnnouncementMapper {

    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.title", target = "courseTitle")
    AnnouncementDto toDto(Announcement announcement);
}
