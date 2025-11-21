package com.elearning.e_learning_core.Repository;

import com.elearning.e_learning_core.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    /**
     * Encontra certificados por ID do estudante
     */
    List<Certificate> findByStudentId(Long studentId);

    /**
     * Encontra certificados revogados por ID do estudante
     */
    List<Certificate> findByStudentIdAndRevokedTrue(Long studentId);

    /**
     * Verifica se existe certificado para estudante e curso
     */
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    /**
     * Encontra certificado por código
     */
    Optional<Certificate> findByCertificateCode(String certificateCode);

    /**
     * Encontra certificados por ID do curso
     */
    List<Certificate> findByCourseId(Long courseId);

    /**
     * Conta certificados emitidos para um curso
     */
    Long countByCourseId(Long courseId);

    /**
     * Encontra certificados não revogados por estudante
     */
    @Query("SELECT c FROM Certificate c WHERE c.student.id = :studentId AND c.revoked = false")
    List<Certificate> findActiveCertificatesByStudentId(@Param("studentId") Long studentId);

    /**
     * Encontra certificados por estudante e curso
     */
    Optional<Certificate> findByStudentIdAndCourseId(Long studentId, Long courseId);
}