package com.elearning.e_learning_core.service;

import com.elearning.e_learning_core.Dtos.CertificateDto;
import com.elearning.e_learning_core.mapper.CertificateMapper;
import com.elearning.e_learning_core.model.Certificate;
import com.elearning.e_learning_core.model.Course;
import com.elearning.e_learning_core.model.Student;
import com.elearning.e_learning_core.model.Usr;
import com.elearning.e_learning_core.Repository.CertificateRepository;
import com.elearning.e_learning_core.Repository.CourseRepository;
import com.elearning.e_learning_core.Repository.PersonRepository;
import com.elearning.e_learning_core.config.security.IAuthenticationFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final CourseRepository courseRepository;
    private final PersonRepository personRepository;
    private final IAuthenticationFacade authenticationFacade;
    private final CourseService courseService;

    public CertificateService(CertificateRepository certificateRepository,
            CourseRepository courseRepository,
            PersonRepository personRepository,
            IAuthenticationFacade authenticationFacade,
            CourseService courseService) {
        this.certificateRepository = certificateRepository;
        this.courseRepository = courseRepository;
        this.personRepository = personRepository;
        this.authenticationFacade = authenticationFacade;
        this.courseService = courseService;
    }

    /**
     * Emite um certificado para o curso (se o estudante tiver concluído)
     */
    public CertificateDto issueCertificate(Long courseId) {
        Usr usr = authenticationFacade.getPrincipal();
        Student student = (Student) personRepository.findById(usr.getPerson().getId())
                .orElseThrow(() -> new RuntimeException("Estudante não encontrado"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        // Verifica se o curso foi concluído
        double progress = courseService.getCourseProgress(courseId.toString());
        if (progress < 100.0) {
            throw new RuntimeException("Curso ainda não concluído. Progresso atual: " + progress + "%");
        }

        // Verifica se já existe um certificado para este curso e estudante
        boolean certificateExists = certificateRepository.existsByStudentIdAndCourseId(student.getId(), courseId);
        if (certificateExists) {
            throw new RuntimeException("Certificado já emitido para este curso");
        }

        Certificate certificate = new Certificate();
        certificate.setStudent(student);
        certificate.setCourse(course);
        certificate.setIssuedAt(LocalDateTime.now());
        certificate.setCertificateCode(generateCertificateCode());
        certificate.setRevoked(false);

        Certificate savedCertificate = certificateRepository.save(certificate);
        return CertificateMapper.toDto(savedCertificate);
    }

    /**
     * Lista todos os certificados do estudante autenticado
     */
    public List<CertificateDto> getMyCertificates() {
        Usr usr = authenticationFacade.getPrincipal();
        Student student = (Student) personRepository.findById(usr.getPerson().getId())
                .orElseThrow(() -> new RuntimeException("Estudante não encontrado"));

        return certificateRepository.findByStudentId(student.getId())
                .stream()
                .map(CertificateMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Lista certificados de um estudante específico
     */
    public List<CertificateDto> getCertificatesByStudent(Long studentId) {
        return certificateRepository.findByStudentId(studentId)
                .stream()
                .map(CertificateMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Lista certificados revogados de um estudante
     */
    public List<CertificateDto> listRevokedCertificatesByStudent(Long studentId) {
        return certificateRepository.findByStudentIdAndRevokedTrue(studentId)
                .stream()
                .map(CertificateMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Revoga um certificado específico
     */
    public CertificateDto revokeCertificate(Long certificateId) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new RuntimeException("Certificado não encontrado"));

        if (certificate.isRevoked()) {
            throw new RuntimeException("Certificado já está revogado");
        }

        certificate.setRevoked(true);
        Certificate revokedCertificate = certificateRepository.save(certificate);
        return CertificateMapper.toDto(revokedCertificate);
    }

    /**
     * Verifica se um certificado existe para o estudante e curso
     */
    public boolean hasCertificateForCourse(Long studentId, Long courseId) {
        return certificateRepository.existsByStudentIdAndCourseId(studentId, courseId);
    }

    /**
     * Busca certificado por código
     */
    public CertificateDto getCertificateByCode(String certificateCode) {
        Certificate certificate = certificateRepository.findByCertificateCode(certificateCode)
                .orElseThrow(() -> new RuntimeException("Certificado não encontrado"));
        return CertificateMapper.toDto(certificate);
    }

    /**
     * Gera um código único para o certificado
     */
    private String generateCertificateCode() {
        return "CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}