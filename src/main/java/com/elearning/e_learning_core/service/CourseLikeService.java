package com.elearning.e_learning_core.service;

import com.elearning.e_learning_core.model.Course;
import com.elearning.e_learning_core.model.CourseLike;
import com.elearning.e_learning_core.model.Student;
import com.elearning.e_learning_core.model.Usr;
import com.elearning.e_learning_core.Repository.CourseLikeRepository;
import com.elearning.e_learning_core.Repository.CourseRepository;
import com.elearning.e_learning_core.Repository.StudentRepository;
import com.elearning.e_learning_core.config.security.IAuthenticationFacade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class CourseLikeService {

    private final CourseLikeRepository courseLikeRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public CourseLikeService(CourseLikeRepository courseLikeRepository,
            CourseRepository courseRepository,
            StudentRepository studentRepository) {
        this.courseLikeRepository = courseLikeRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    private Long getAuthenticatedStudentId() {
        try {
            Usr usr = authenticationFacade.getPrincipal();
            if (usr.getPerson() instanceof Student) {
                return usr.getPerson().getId();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Adiciona like a um curso
     */
    public Map<String, Object> likeCourse(Long courseId) {
        Long studentId = getAuthenticatedStudentId();
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        // Verifica se o estudante existe
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Estudante não encontrado"));

        // Verifica se já existe like
        boolean alreadyLiked = courseLikeRepository.existsByStudentIdAndCourseId(studentId, courseId);
        if (alreadyLiked) {
            throw new RuntimeException("Você já curtiu este curso");
        }

        // Cria o like
        CourseLike courseLike = new CourseLike(student, course);
        courseLikeRepository.save(courseLike);

        // Obtém a nova contagem de likes
        Long likeCount = courseLikeRepository.countByCourseId(courseId);

        Map<String, Object> response = new HashMap<>();
        response.put("liked", true);
        response.put("likeCount", likeCount);
        response.put("message", "Curso curtido com sucesso");

        return response;
    }

    /**
     * Remove like de um curso
     */
    public Map<String, Object> unlikeCourse(Long courseId) {
        Long studentId = getAuthenticatedStudentId();
        CourseLike courseLike = courseLikeRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new RuntimeException("Like não encontrado"));

        // Remove o like
        courseLikeRepository.delete(courseLike);

        // Obtém a nova contagem de likes
        Long likeCount = courseLikeRepository.countByCourseId(courseId);

        Map<String, Object> response = new HashMap<>();
        response.put("liked", false);
        response.put("likeCount", likeCount);
        response.put("message", "Like removido com sucesso");

        return response;
    }

    /**
     * Alterna like/unlike
     */
    public Map<String, Object> toggleLike(Long courseId, Long studentId) {
        boolean alreadyLiked = courseLikeRepository.existsByStudentIdAndCourseId(studentId, courseId);

        if (alreadyLiked) {
            return unlikeCourse(courseId);
        } else {
            return likeCourse(courseId);
        }
    }

    /**
     * Verifica se o estudante curtiu o curso
     */
    public boolean hasStudentLikedCourse(Long courseId, Long studentId) {
        return courseLikeRepository.existsByStudentIdAndCourseId(studentId, courseId);
    }

    /**
     * Obtém a contagem de likes de um curso
     */
    public Long getLikeCount(Long courseId) {
        return courseLikeRepository.countByCourseId(courseId);
    }

    /**
     * Obtém informações completas sobre likes
     */
    public Map<String, Object> getLikeInfo(Long courseId, Long studentId) {
        Long likeCount = getLikeCount(courseId);
        boolean hasLiked = hasStudentLikedCourse(courseId, studentId);

        Map<String, Object> response = new HashMap<>();
        response.put("likeCount", likeCount);
        response.put("hasLiked", hasLiked);
        response.put("courseId", courseId);

        return response;
    }
}