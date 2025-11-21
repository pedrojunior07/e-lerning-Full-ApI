package com.elearning.e_learning_core.service;

import com.elearning.e_learning_core.Dtos.*;
import com.elearning.e_learning_core.model.*;
import com.elearning.e_learning_core.Repository.CourseModuleRepository;
import com.elearning.e_learning_core.Repository.LessonProgressRepository;
import com.elearning.e_learning_core.Repository.StudentRepository;
import com.elearning.e_learning_core.config.security.IAuthenticationFacade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseProgressService {

        private final CourseModuleRepository moduleRepository;
        private final LessonProgressRepository lessonProgressRepository;
        private final StudentRepository studentRepository;

        public CourseProgressService(CourseModuleRepository moduleRepository,
                        LessonProgressRepository lessonProgressRepository,
                        StudentRepository studentRepository) {
                this.moduleRepository = moduleRepository;
                this.lessonProgressRepository = lessonProgressRepository;
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
         * Obtém todos os módulos de um curso com as aulas e progresso do estudante
         */
        public List<ModuleWithProgressDTO> getCourseModulesWithStudentProgress(Long courseId, Long studentId) {
                // Verifica se o estudante existe
                Long student = getAuthenticatedStudentId();

                Student stud = studentRepository.findById(studentId)
                                .orElseThrow(() -> new RuntimeException("Estudante não encontrado"));

                // Busca todos os módulos do curso
                List<CourseModule> modules = moduleRepository.findByCourseId(courseId);

                // Busca todos os progressos do estudante para este curso
                List<LessonProgress> studentProgresses = lessonProgressRepository
                                .findProgressByStudentAndCourse(studentId, courseId);

                // Cria um mapa de lessonId -> LessonProgress para acesso rápido
                Map<Long, LessonProgress> progressMap = studentProgresses.stream()
                                .collect(Collectors.toMap(
                                                progress -> progress.getLesson().getId(),
                                                progress -> progress));

                // Converte os módulos para DTOs com progresso
                return modules.stream()
                                .map(module -> convertModuleToDtoWithProgress(module, progressMap))
                                .collect(Collectors.toList());
        }

        /**
         * Converte um CourseModule para ModuleWithProgressDTO
         */
        private ModuleWithProgressDTO convertModuleToDtoWithProgress(CourseModule module,
                        Map<Long, LessonProgress> progressMap) {
                List<LessonWithProgressDTO> lessonDtos = module.getLessons().stream()
                                .map(lesson -> convertLessonToDtoWithProgress(lesson, progressMap))
                                .collect(Collectors.toList());

                return new ModuleWithProgressDTO(
                                module.getId(),
                                module.getTitle(),
                                module.getDescription(),
                                lessonDtos);
        }

        /**
         * Converte uma Lesson para LessonWithProgressDTO
         */
        private LessonWithProgressDTO convertLessonToDtoWithProgress(Lesson lesson,
                        Map<Long, LessonProgress> progressMap) {
                LessonProgressDTO progressDto = null;

                // Verifica se existe progresso para esta aula
                LessonProgress progress = progressMap.get(lesson.getId());
                if (progress != null) {
                        progressDto = new LessonProgressDTO(
                                        progress.isCompleted(),
                                        progress.getProgressPercentage(),
                                        progress.getCompletedAt());
                }

                return new LessonWithProgressDTO(
                                lesson.getId(),
                                lesson.getTitle(),
                                lesson.getContent(),
                                progressDto);
        }

        /**
         * Calcula o progresso geral do estudante no curso
         */
        public double getCourseProgressPercentage(Long courseId, Long studentId) {
                List<CourseModule> modules = moduleRepository.findByCourseId(courseId);

                long totalLessons = modules.stream()
                                .mapToLong(module -> module.getLessons().size())
                                .sum();

                if (totalLessons == 0) {
                        return 0.0;
                }

                List<LessonProgress> completedProgresses = lessonProgressRepository
                                .findByStudentIdAndLessonModuleCourseId(studentId, courseId)
                                .stream()
                                .filter(LessonProgress::isCompleted)
                                .collect(Collectors.toList());

                long completedLessons = completedProgresses.size();

                return (completedLessons * 100.0) / totalLessons;
        }
}