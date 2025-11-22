package com.elearning.e_learning_core.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.elearning.e_learning_core.Dtos.ApiResponse;
import com.elearning.e_learning_core.Dtos.CertificateDto;
import com.elearning.e_learning_core.Dtos.CourseCardDto;
import com.elearning.e_learning_core.Dtos.CourseDto;
import com.elearning.e_learning_core.Dtos.CourseDtoStep1;
import com.elearning.e_learning_core.Dtos.CourseDtoStep2;
import com.elearning.e_learning_core.Dtos.CourseDtoStep5;
import com.elearning.e_learning_core.Dtos.CourseStatsDTO;
import com.elearning.e_learning_core.Dtos.CourseTitle;
import com.elearning.e_learning_core.Dtos.LevelCountDTO;
import com.elearning.e_learning_core.Dtos.ModuleDto;
import com.elearning.e_learning_core.Dtos.PriceTypeCountDTO;
import com.elearning.e_learning_core.Dtos.TopCategoryDTO;
import com.elearning.e_learning_core.Repository.CategoryRepository;
import com.elearning.e_learning_core.Repository.CertificateRepository;
import com.elearning.e_learning_core.Repository.CourseLikeRepository;
import com.elearning.e_learning_core.Repository.CourseModuleRepository;
import com.elearning.e_learning_core.Repository.CourseRepository;
import com.elearning.e_learning_core.Repository.LessonProgressRepository;
import com.elearning.e_learning_core.Repository.LessonRepository;
import com.elearning.e_learning_core.Repository.PersonRepository;
import com.elearning.e_learning_core.Repository.PurchaseRepository;
import com.elearning.e_learning_core.Repository.StudentRepository;
import com.elearning.e_learning_core.Repository.UserRepository;
import com.elearning.e_learning_core.config.security.IAuthenticationFacade;
import com.elearning.e_learning_core.mapper.CertificateMapper;
import com.elearning.e_learning_core.mapper.CourseMapper;
import com.elearning.e_learning_core.mapper.ModuleMapper;
import com.elearning.e_learning_core.model.Category;
import com.elearning.e_learning_core.model.Certificate;
import com.elearning.e_learning_core.model.Course;
import com.elearning.e_learning_core.model.Course.StatusCourse;

import jakarta.transaction.Transactional;

import com.elearning.e_learning_core.model.CourseModule;
import com.elearning.e_learning_core.model.ExpiryType;
import com.elearning.e_learning_core.model.Instructor;
import com.elearning.e_learning_core.model.Lesson;
import com.elearning.e_learning_core.model.LessonProgress;
import com.elearning.e_learning_core.model.LevelCurseType;
import com.elearning.e_learning_core.model.Person;
import com.elearning.e_learning_core.model.Purchase;
import com.elearning.e_learning_core.model.Student;
import com.elearning.e_learning_core.model.Usr;

@Service
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    private final CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private final CourseModuleRepository moduleRepository;

    @Autowired
    private CourseLikeRepository courseLikeRepository;

    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    CertificateRepository certificateRepository;

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    private final CourseMapper courseMapper;

    private final ModuleMapper moduleMapper;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LessonProgressRepository lessonProgressRepository;

    public CourseService(CourseRepository courseRepository,
            CourseModuleRepository moduleRepository,
            CourseMapper courseMapper, ModuleMapper moduleMapper) {
        this.courseRepository = courseRepository;
        this.moduleRepository = moduleRepository;
        this.courseMapper = courseMapper;
        this.moduleMapper = moduleMapper;
    }

    /**
     * Obtém o ID do estudante autenticado
     */
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
     * Adiciona informações de like a um CourseDto
     */
    private void populateLikeInfo(CourseDto courseDto, Long courseId) {
        Long studentId = getAuthenticatedStudentId();
        if (studentId != null) {
            boolean hasLiked = courseLikeRepository.existsByStudentIdAndCourseId(studentId, courseId);
            Long likeCount = courseLikeRepository.countByCourseId(courseId);

            courseDto.setHasLiked(hasLiked);
            courseDto.setLikeCount(likeCount);
        }
    }

    /**
     * Adiciona informações de like a uma lista de CourseCardDto
     */
    private void populateLikeInfoo(List<CourseCardDto> courseCards) {
        Long studentId = getAuthenticatedStudentId();
        if (studentId != null && !courseCards.isEmpty()) {
            List<Long> courseIds = courseCards.stream()
                    .map(CourseCardDto::getId)
                    .collect(Collectors.toList());

            // Busca todos os likes do estudante para estes cursos
            List<Long> likedCourseIds = courseLikeRepository.findLikedCourseIdsByStudentAndCourses(studentId,
                    courseIds);

            // Define hasLiked para cada curso
            courseCards.forEach(card -> card.setHasLiked(likedCourseIds.contains(card.getId())));
        }
    }

    public ApiResponse<?> getMyLikedCourses() {
        Long studentId = getAuthenticatedStudentId();

        if (studentId == null) {
            return new ApiResponse<>("error", "Estudante não autenticado", 401, null);
        }

        // Agora usa o método corrigido com JOIN FETCH
        List<Course> likedCourses = courseLikeRepository.findLikedCoursesByStudent(studentId);

        List<CourseCardDto> courseCards = likedCourses.stream()
                .map(courseMapper::toCardDto)
                .peek(card -> card.setHasLiked(true))
                .collect(Collectors.toList());

        return new ApiResponse<>("success", "Cursos curtidos recuperados", 200, courseCards);
    }

    /**
     * Adiciona informações de like a uma lista de CourseDto
     */
    private void populateLikeInfo(List<CourseDto> courseDtos) {
        Long studentId = getAuthenticatedStudentId();
        if (studentId != null && !courseDtos.isEmpty()) {
            List<Long> courseIds = courseDtos.stream()
                    .map(CourseDto::getId)
                    .collect(Collectors.toList());

            // Busca contagem de likes para estes cursos
            Map<Long, Long> likeCountsMap = courseLikeRepository.countLikesByCourseIds(courseIds)
                    .stream()
                    .collect(Collectors.toMap(
                            result -> (Long) result[0],
                            result -> (Long) result[1]));

            // Busca likes do estudante
            List<Long> likedCourseIds = courseLikeRepository.findLikedCourseIdsByStudentAndCourses(studentId,
                    courseIds);

            // Define hasLiked e likeCount para cada curso
            courseDtos.forEach(dto -> {
                dto.setHasLiked(likedCourseIds.contains(dto.getId()));
                dto.setLikeCount(likeCountsMap.getOrDefault(dto.getId(), 0L));
            });
        }
    }

    public CourseStatsDTO getCourseStatistics() {
        long total = courseRepository.count();
        long published = courseRepository.countByStatus(StatusCourse.PUBLISHED);
        long draft = courseRepository.countByStatus(StatusCourse.DRAFT);
        long inactive = courseRepository.countByStatus(StatusCourse.INACTIVE);
        long archived = courseRepository.countByStatus(StatusCourse.ARCHIVED);
        long paid = courseRepository.countPaidCourses();
        long free = courseRepository.countFreeCourses();

        return new CourseStatsDTO(total, published, draft, inactive, archived, paid, free);
    }

    public double getModuleProgress(Student student, CourseModule module) {
        long totalLessons = module.getLessons().size();
        long completed = lessonProgressRepository.countByStudentAndLessonModuleAndCompletedTrue(student, module);

        return totalLessons == 0 ? 0 : (completed * 100.0 / totalLessons);
    }

    public List<CertificateDto> listRevokedCertificatesByStudent(Long studentId) {
        return certificateRepository.findByStudentIdAndRevokedTrue(studentId)
                .stream()
                .map(CertificateMapper::toDto)
                .toList();
    }

    public double getCourseProgress(String courseId) {
        Course course = courseRepository.findById(Long.parseLong(courseId))
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        Usr usr = authenticationFacade.getPrincipal();
        Person studentt = personRepository.findById(usr.getPerson().getId())
                .orElseThrow(() -> new UsernameNotFoundException(""));
        Person student = personRepository.findById(studentt.getId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        long totalLessons = course.getModules().stream()
                .mapToLong(m -> m.getLessons().size())
                .sum();

        long completed = lessonProgressRepository.countByStudentAndLessonModuleCourseAndCompletedTrue((Student) student,
                course);

        return totalLessons == 0 ? 0 : (completed * 100.0 / totalLessons);
    }

    @Transactional
    public void completeLesson(Long lessonId) {
        Usr usr = authenticationFacade.getPrincipal();
        Person studentt = personRepository.findById(usr.getPerson().getId())
                .orElseThrow(() -> new UsernameNotFoundException(""));
        Person student = personRepository.findById(studentt.getId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Aula não encontrada"));

        LessonProgress progress = lessonProgressRepository
                .findByStudentAndLesson((Student) student, lesson)
                .orElse(null);

        if (progress == null) {
            progress = new LessonProgress();
            progress.setStudent((Student) student);
            progress.setLesson(lesson);
        }

        progress.setCompleted(true);
        progress.setProgressPercentage(100);
        progress.setCompletedAt(LocalDateTime.now());
        lessonProgressRepository.save(progress);
    }

    public Certificate issueCertificate(Long courseId) {
        Usr usr = authenticationFacade.getPrincipal();
        Person student = personRepository.findById(usr.getPerson().getId())
                .orElseThrow(() -> new UsernameNotFoundException(""));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        double progress = getCourseProgress(courseId.toString());

        if (progress < 100.0) {
            throw new RuntimeException("Curso ainda não concluído.");
        }

        Certificate cert = new Certificate();
        cert.setStudent((Student) student);
        cert.setCourse(course);
        cert.setIssuedAt(LocalDateTime.now());
        cert.setCertificateCode(UUID.randomUUID().toString());

        return certificateRepository.save(cert);
    }

    public ApiResponse<?> saveStep1(CourseDtoStep1 dto) {
        Long courseId = dto.getCourseId();

        Usr principal = authenticationFacade.getPrincipal();

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        if (!principal.getRole().getRoleName().equals("ROLE_INSTRUCTOR")) {
            return new ApiResponse<>("error", "You do not have permission to create a course", 403, principal);
        }

        Course course;
        if (courseId == null) {
            course = new Course();
            course.setInstructor((Instructor) principal.getPerson());
            course.setStatus(Course.StatusCourse.PUBLISHED);
            course.setCurrentStep(1);
        } else {
            course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
            if (!course.getInstructor().getId().equals(principal.getPerson().getId())) {
                return new ApiResponse<>("error", "Você não tem permissão para atualizar este curso", 403, null);
            }
        }

        course.setTitle(dto.getTitle());
        course.setCategory(category);

        course.setLevel(dto.getLevel());
        course.setLanguage(dto.getLanguage());
        course.setMaxStudents(dto.getMaxStudents());
        course.setPublicOrPrivate(dto.getPublicOrPrivate());
        course.setShortDescription(dto.getShortDescription());
        course.setLongDescription(dto.getLongDescription());
        course.setWhatStudentsWillLearn(dto.getWhatStudentsWillLearn());
        course.setRequirements(dto.getRequirements());

        Course savedCourse = courseRepository.save(course);
        return new ApiResponse<>("success", "Course saved successfully", 201, Map.of(
                "courseId", savedCourse.getId(),
                "currentStep", savedCourse.getCurrentStep()));
    }

    public ApiResponse<?> saveStep2Media(Long courseId, CourseDtoStep2 dto) {
        Usr principal = authenticationFacade.getPrincipal();
        if (!principal.getRole().getRoleName().equals("ROLE_INSTRUCTOR")
                || !courseRepository.existsByIdAndInstructorId(courseId, principal.getPerson().getId())) {
            throw new RuntimeException("Você não tem permissão para atualizar o curso");
        }
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        course.setIntroVideoUrl(dto.getIntroVideoUrl());
        course.setVideoProvider(dto.getVideoProvider());
        course.setThumbnailPath(dto.getThumbnailPath());
        course.setCurrentStep(2);

        Course savedCourse = courseRepository.save(course);
        return new ApiResponse<>("success", "Course updated successfully", 201, Map.of(
                "courseId", savedCourse.getId(),
                "currentStep", savedCourse.getCurrentStep()));
    }

    public ApiResponse<?> saveStep5Pricing(Long courseId, CourseDtoStep5 dto) {
        Usr principal = authenticationFacade.getPrincipal();

        if (!principal.getRole().getRoleName().equals("ROLE_INSTRUCTOR")
                || !courseRepository.existsByIdAndInstructorId(courseId, principal.getPerson().getId())) {
            throw new RuntimeException("Você não tem permissão para atualizar este curso.");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado."));

        course.setPrice(dto.isFree() ? BigDecimal.ZERO : dto.getPrice());
        course.setHasDiscount(dto.isFree() ? false : dto.isHasDiscount());
        course.setDiscountPrice(dto.isFree() || !dto.isHasDiscount() ? BigDecimal.ZERO : dto.getDiscountPrice());
        course.setExpiryType(dto.getExpiryType());
        course.setCurrentStep(4);

        if (dto.getExpiryType() == ExpiryType.LIMITED_TIME) {
            course.setExpiryMonths(dto.getExpiryMonths());
        } else {
            course.setExpiryMonths(null);
        }

        Course savedCourse = courseRepository.save(course);
        return new ApiResponse<>("success", "Course updated successfully", 201, Map.of(
                "courseId", savedCourse.getId(),
                "currentStep", savedCourse.getCurrentStep()));
    }

    public ApiResponse<?> saveCurriculum(Long courseId, List<ModuleDto> moduleDtos) {
        Usr principal = authenticationFacade.getPrincipal();
        if (!principal.getRole().getRoleName().equals("ROLE_INSTRUCTOR")
                || !courseRepository.existsByIdAndInstructorId(courseId, principal.getPerson().getId())) {
            throw new RuntimeException("Você não tem permissão para atualizar o curso");
        }
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        course.setCurrentStep(3);

        logger.info("Saving curriculum for course {} with {} modules", courseId, moduleDtos.size());

        for (ModuleDto moduleDto : moduleDtos) {
            CourseModule module = new CourseModule();
            module.setTitle(moduleDto.getTitle());
            module.setDescription(moduleDto.getDescription());
            module.setCourse(course);

            List<Lesson> lessons = moduleDto.getLessons().stream().map(dto -> {
                Lesson lesson = new Lesson();
                lesson.setTitle(dto.getTitle());
                lesson.setContent(dto.getContent());
                lesson.setModule(module);
                logger.info("Lesson '{}' content: {}", dto.getTitle(),
                    dto.getContent() != null ? dto.getContent().substring(0, Math.min(100, dto.getContent().length())) : "null");
                return lesson;
            }).toList();

            module.setLessons(lessons);
            CourseModule savedModule = moduleRepository.save(module);
            logger.info("Saved module '{}' with {} lessons", savedModule.getTitle(), lessons.size());
        }
        Course savedCourse = courseRepository.save(course);
        logger.info("Curriculum saved successfully for course {}", courseId);
        return new ApiResponse<>("success", "Course updated successfully", 201, Map.of(
                "courseId", savedCourse.getId(),
                "currentStep", savedCourse.getCurrentStep()));
    }

    public ApiResponse<?> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        List<CourseTitle> dtos = courses.stream()
                .map(courseMapper::toTitleDto)
                .collect(Collectors.toList());

        return new ApiResponse<>("success", "Courses retrieved", 200, dtos);
    }

    public ApiResponse<?> getMyCourses(String title, StatusCourse status, Pageable pageable) {
        Usr principal = authenticationFacade.getPrincipal();

        if (!principal.getRole().getRoleName().equals("ROLE_INSTRUCTOR")) {
            throw new RuntimeException("Você não tem permissão para acessar os cursos");
        }

        if (title == null)
            title = "";
        if (status == null)
            status = StatusCourse.DRAFT;

        Page<Course> coursesPage = courseRepository
                .findByTitleCategoryAndOptionalStatus(title, status, pageable);

        List<CourseDto> dtos = coursesPage.stream()
                .map(courseMapper::toDtoWithoutModules)
                .collect(Collectors.toList());

        // Adiciona informações de like
        populateLikeInfo(dtos);

        Map<String, Object> response = Map.of(
                "content", dtos,
                "pageNumber", coursesPage.getNumber(),
                "pageSize", coursesPage.getSize(),
                "totalElements", coursesPage.getTotalElements(),
                "totalPages", coursesPage.getTotalPages(),
                "last", coursesPage.isLast());

        return new ApiResponse<>("success", "Courses retrieved for instructor", 200, response);
    }

    public ApiResponse<?> listCourseCards(String category, String title, Double minPrice, Double maxPrice,
            Pageable pageable) {
        Page<CourseCardDto> cards = courseRepository.findAllCourseCardsWithLessonCount(
                title, category, minPrice, maxPrice, pageable);

        // Adiciona informações de like
        populateLikeInfoo(cards.getContent());

        return new ApiResponse<>("success",
                "Course cards retrieved",
                200,
                cards);
    }

    public ApiResponse<?> listInstructorCourseCards(
            String title,
            String category,
            Double minPrice,
            Double maxPrice,
            StatusCourse status,
            Long instructorId,
            Pageable pageable) {

        Usr principal = authenticationFacade.getPrincipal();

        if (!principal.getRole().getRoleName().equals("ROLE_INSTRUCTOR")) {
            throw new RuntimeException("Você não tem permissão para acessar os cursos");
        }

        Page<CourseCardDto> cards = courseRepository.findCourseCardsByInstructorAndStatus(
                title, category, minPrice, maxPrice, status, principal.getId(), pageable);

        // Adiciona informações de like
        populateLikeInfoo(cards.getContent());

        return new ApiResponse<>("success", "Courses filtered", 200, cards);
    }

    public ApiResponse<?> searchCourseCards(
            Long instructorId,
            Long categoryId,
            Double minPrice,
            Double maxPrice,
            StatusCourse status,
            Pageable pageable) {

        Usr principal = authenticationFacade.getPrincipal();

        Page<CourseCardDto> cards = courseRepository.searchCourseCards(
                categoryId, instructorId, minPrice, maxPrice, pageable);

        // Adiciona informações de like
        populateLikeInfoo(cards.getContent());

        return new ApiResponse<>("success", "Courses filtered", 200, cards);
    }

    public ApiResponse<?> getCourseById(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElse(null);

        if (course == null) {
            return new ApiResponse<>("error", "Course not found", 404, null);
        }

        CourseDto dto = courseMapper.toDtoWithoutModules(course);

        // Adiciona informações de like
        populateLikeInfo(dto, courseId);

        return new ApiResponse<>("success", "Course found", 200, dto);
    }

    public ApiResponse<?> getModelesByCourseId(Long id) {
        List<ModuleDto> modules = moduleRepository.findByCourseId(id).stream()
                .map(moduleMapper::toModuleDto)
                .collect(Collectors.toList());

        return new ApiResponse("success", "", 200, modules);
    }

    public ApiResponse<?> getTopCategories() {
        List<TopCategoryDTO> categoryDTOs = courseRepository.findTopCategories();
        return new ApiResponse<>("success", "", 200, categoryDTOs);
    }

    public ApiResponse<?> getTop10Courses() {
        Pageable top10 = PageRequest.of(0, 8);
        List<CourseCardDto> topCourses = courseRepository.findTopCoursesWithMostStudents(top10);

        // Adiciona informações de like
        populateLikeInfoo(topCourses);

        return new ApiResponse<>("success", "", 200, topCourses);
    }

    public ApiResponse<?> getCourseLevelsWithCount() {
        List<Object[]> results = courseRepository.countCoursesByLevel();

        List<LevelCountDTO> levelsCount = results.stream()
                .map(obj -> new LevelCountDTO((LevelCurseType) obj[0], (Long) obj[1]))
                .collect(Collectors.toList());

        return new ApiResponse("success", "Levels count retrieved successfully", 200, levelsCount);
    }

    public List<PriceTypeCountDTO> getCoursePriceTypesCount() {
        List<Object[]> results = courseRepository.countCoursesByPriceType();
        return results.stream()
                .map(obj -> new PriceTypeCountDTO((Boolean) obj[0], (Long) obj[1]))
                .collect(Collectors.toList());
    }

    public ApiResponse<?> getStudentCourses(Long id, Pageable pageable) {
        // Verificar se o estudante existe
        if (!studentRepository.existsById(id)) {
            return new ApiResponse<>("error", "Student not found", 404, null);
        }

        Page<Course> coursesPage = studentRepository.findCoursesByStudentId(id, pageable);

        List<CourseDto> dtos = coursesPage.getContent().stream()
                .map(courseMapper::toDtoWithoutModules)
                .collect(Collectors.toList());

        // Adiciona informações de like
        populateLikeInfo(dtos);

        Map<String, Object> response = Map.of(
                "content", dtos,
                "pageNumber", coursesPage.getNumber(),
                "pageSize", coursesPage.getSize(),
                "totalElements", coursesPage.getTotalElements(),
                "totalPages", coursesPage.getTotalPages(),
                "last", coursesPage.isLast());

        return new ApiResponse<>("success", "Student courses retrieved", 200, response);
    }

    public ApiResponse<?> updateCourseStatus(Long courseId, StatusCourse status) {
        Course course = courseRepository.findById(courseId)
                .orElse(null);

        if (course == null) {
            return new ApiResponse<>("error", "Course not found", 404, null);
        }

        course.setStatus(status);
        courseRepository.save(course);

        return new ApiResponse<>("success", "Course status updated to " + status, 200, courseMapper.toDtoWithoutModules(course));
    }

    /**
     * Método auxiliar para popular informações de like em CourseCardDto paginados
     */
    private void populateLikeInfo(Page<CourseCardDto> cardsPage) {
        if (cardsPage != null && !cardsPage.getContent().isEmpty()) {
            populateLikeInfoo(cardsPage.getContent());
        }
    }
}