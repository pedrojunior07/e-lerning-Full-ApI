package com.elearning.e_learning_core.mapper;

import com.elearning.e_learning_core.Dtos.CertificateDto;
import com.elearning.e_learning_core.Dtos.PersonDto;
import com.elearning.e_learning_core.Dtos.CourseDto;
import com.elearning.e_learning_core.model.Certificate;
import com.elearning.e_learning_core.model.Student;
import com.elearning.e_learning_core.model.Course;

public class CertificateMapper {

    public static CertificateDto toDto(Certificate certificate) {
        if (certificate == null)
            return null;

        // Converte student → PersonDto
        Student student = certificate.getStudent();
        PersonDto studentDto = new PersonDto(
                student.getFirstName(),
                student.getLastName(),
                student.getUserName() != null ? student.getUserName() : null,
                student.getPhoneNumber(),
                student.getEmail(),
                student.getGender(),
                student.getDob());

        // Converte course → CourseDto (aqui simplificado, tu podes usar um CourseMapper
        // existente)
        Course course = certificate.getCourse();
        CourseDto courseDto = new CourseDto();
        courseDto.setId(course.getId());
        courseDto.setTitle(course.getTitle());

        courseDto.setShortDescription(course.getShortDescription());
        courseDto.setLongDescription(course.getLongDescription());

        return new CertificateDto(
                certificate.getCertificateCode(),
                certificate.getIssuedAt(),
                certificate.isRevoked(),
                studentDto,
                courseDto);
    }
}