package com.elearning.e_learning_core.mapper;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.elearning.e_learning_core.Dtos.CourseCardDto;
import com.elearning.e_learning_core.Dtos.CourseDto;
import com.elearning.e_learning_core.Dtos.CourseModuleDto;
import com.elearning.e_learning_core.Dtos.CourseTitle;
import com.elearning.e_learning_core.Dtos.InstructorDto;
import com.elearning.e_learning_core.Dtos.LessonDTO;
import com.elearning.e_learning_core.model.Course;
import com.elearning.e_learning_core.model.CourseModule;
import com.elearning.e_learning_core.model.Instructor;
import com.elearning.e_learning_core.model.Lesson;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    @Mapping(target = "modules", ignore = true)
    CourseDto toDtoWithoutModules(Course course);

    CourseTitle toTitleDto(Course course);

    CourseModuleDto toModuleDto(CourseModule module);

    LessonDTO toLessonDto(Lesson lesson);

    InstructorDto toInstructorDto(Instructor instructor);

    @Mapping(target = "instructorName", expression = "java(course.getInstructor().getFirstName() + \" \" + course.getInstructor().getLastName())")
   
    CourseCardDto toCardDto(Course course);

    @Named("buildDiscountText")
    default BigDecimal buildDiscountText(Course c) {
        if (Boolean.TRUE.equals(c.isHasDiscount())
                && c.getDiscountPrice() != null
                && c.getPrice() != null
                && c.getPrice().compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal discount = BigDecimal.ONE
                    .subtract(c.getDiscountPrice().divide(c.getPrice(), 2, RoundingMode.HALF_UP))
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(0, RoundingMode.HALF_UP);

            return discount;
        }
        return null;
    }

    @Named("buildPrice")
    default String buildPrice(Course c) {
        if (Boolean.TRUE.equals(c.isFree()) || c.getPrice() == null) {
            return "Free";
        }
        BigDecimal finalPrice = Boolean.TRUE.equals(c.isHasDiscount()) && c.getDiscountPrice() != null
                ? c.getDiscountPrice()
                : c.getPrice();

        return "$" + finalPrice.stripTrailingZeros().toPlainString();
    }
}
