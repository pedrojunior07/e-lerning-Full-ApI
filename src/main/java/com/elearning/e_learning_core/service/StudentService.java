package com.elearning.e_learning_core.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.elearning.e_learning_core.Dtos.ApiResponse;
import com.elearning.e_learning_core.Dtos.StudentDTO;
import com.elearning.e_learning_core.Dtos.StudentSummaryDTO;
import com.elearning.e_learning_core.Repository.StudentRepository;
import com.elearning.e_learning_core.Repository.CourseRepository;
import com.elearning.e_learning_core.mapper.StudentDTOMapper;
import com.elearning.e_learning_core.model.Student;
import com.elearning.e_learning_core.model.Course;

@Service
public class StudentService {

        @Autowired
        private StudentRepository studentRepository;

        @Autowired
        private CourseRepository courseRepository;

        @Autowired
        private StudentDTOMapper studentDTOMapper;

        public Page<StudentSummaryDTO> listStudentsSummary(Pageable pageable) {
                return studentRepository.findAll(pageable)
                                .map(student -> new StudentSummaryDTO(
                                                student.getId(),
                                                student.getFirstName() + " " + student.getLastName(),
                                                student.getCreatedAt(),
                                                student.getCourses() != null ? student.getCourses().size() : 0));
        }

        public ApiResponse<?> getStudentById(Long id) {
                Student student = studentRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

                StudentDTO studentDto = studentDTOMapper.toDto(student);

                return new ApiResponse<>(
                                "success",
                                "Student retrieved successfully",
                                200,
                                studentDto);
        }

        public ApiResponse<?> updateStudent(Long id, StudentDTO dto) {
                Student student = studentRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

                if (dto.getFirstName() != null) student.setFirstName(dto.getFirstName());
                if (dto.getLastName() != null) student.setLastName(dto.getLastName());
                if (dto.getEmail() != null) student.setEmail(dto.getEmail());
                if (dto.getPhoneNumber() != null) student.setPhoneNumber(dto.getPhoneNumber());
                if (dto.getGender() != null) student.setGender(dto.getGender());
                if (dto.getDob() != null) student.setDob(dto.getDob());

                studentRepository.save(student);

                return new ApiResponse<>(
                                "success",
                                "Student updated successfully",
                                200,
                                studentDTOMapper.toDto(student));
        }

        public ApiResponse<?> deleteStudent(Long id) {
                if (!studentRepository.existsById(id)) {
                        return new ApiResponse<>("error", "Student not found", 404, null);
                }

                studentRepository.deleteById(id);

                return new ApiResponse<>(
                                "success",
                                "Student deleted successfully",
                                200,
                                null);
        }

        public ApiResponse<?> getStudentCourses(Long studentId) {
                Student student = studentRepository.findById(studentId)
                                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

                List<Course> courses = student.getCourses();

                return new ApiResponse<>(
                                "success",
                                "Student courses retrieved successfully",
                                200,
                                courses.stream().map(course -> new Object() {
                                        public Long id = course.getId();
                                        public String title = course.getTitle();
                                        public String thumbnail = course.getThumbnailPath();
                                        public String level = course.getLevel() != null ? course.getLevel().name() : null;
                                }).collect(Collectors.toList()));
        }

        public ApiResponse<?> enrollStudentInCourse(Long studentId, Long courseId) {
                Student student = studentRepository.findById(studentId)
                                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

                Course course = courseRepository.findById(courseId)
                                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

                if (student.getCourses().contains(course)) {
                        return new ApiResponse<>("error", "Student already enrolled in this course", 400, null);
                }

                student.enrollInCourse(course);
                studentRepository.save(student);

                return new ApiResponse<>(
                                "success",
                                "Student enrolled successfully",
                                200,
                                null);
        }

        public ApiResponse<?> unenrollStudentFromCourse(Long studentId, Long courseId) {
                Student student = studentRepository.findById(studentId)
                                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));

                Course course = courseRepository.findById(courseId)
                                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

                if (!student.getCourses().contains(course)) {
                        return new ApiResponse<>("error", "Student is not enrolled in this course", 400, null);
                }

                student.unenrollFromCourse(course);
                studentRepository.save(student);

                return new ApiResponse<>(
                                "success",
                                "Student unenrolled successfully",
                                200,
                                null);
        }

        public long countStudents() {
                return studentRepository.count();
        }
}
