package com.elearning.e_learning_core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.elearning.e_learning_core.Dtos.AnnouncementDto;
import com.elearning.e_learning_core.Repository.AnnouncementRepository;
import com.elearning.e_learning_core.Repository.CourseRepository;
import com.elearning.e_learning_core.mapper.AnnouncementMapper;
import com.elearning.e_learning_core.model.Announcement;
import com.elearning.e_learning_core.model.Course;
import com.elearning.e_learning_core.model.Student;

import jakarta.transaction.Transactional;

@Service
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final CourseRepository courseRepository;
    private final AnnouncementMapper announcementMapper;

    public AnnouncementService(
            AnnouncementRepository announcementRepository,
            CourseRepository courseRepository,
            AnnouncementMapper announcementMapper
    // EmailService emailService
    ) {
        this.announcementRepository = announcementRepository;
        this.courseRepository = courseRepository;
        this.announcementMapper = announcementMapper;
        // this.emailService = emailService;
    }

    @Transactional
    public AnnouncementDto createAnnouncement(AnnouncementDto dto) {
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        Announcement announcement = new Announcement();
        announcement.setTitle(dto.getTitle());
        announcement.setMessage(dto.getMessage());
        announcement.setCourse(course);

        Announcement saved = announcementRepository.save(announcement);

        List<Student> students = course.getStudents();
        for (Student student : students) {
            String subject = "Novo anúncio para o curso: " + course.getTitle();
            String body = "Olá " + student.getFirstName() + ",\n\n" +
                    "Foi publicado um novo anúncio no curso \"" + course.getTitle() + "\":\n\n" +
                    "**" + announcement.getTitle() + "**\n" +
                    announcement.getMessage() + "\n\n" +
                    "Acesse sua conta para mais detalhes.";

            // emailService.sendEmail(student.getEmail(), subject, body);
        }

        return announcementMapper.toDto(saved);
    }

    public List<AnnouncementDto> listAnnouncement() {

        List<Announcement> announcements = announcementRepository.findAll();
        return announcements.stream().map(announcementMapper::toDto).collect(java.util.stream.Collectors.toList());

    }
}