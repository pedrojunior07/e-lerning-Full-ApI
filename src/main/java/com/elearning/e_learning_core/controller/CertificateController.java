package com.elearning.e_learning_core.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.elearning.e_learning_core.Dtos.CertificateDto;
import com.elearning.e_learning_core.service.CertificateService;

import java.util.List;

@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @PostMapping("/issue/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<CertificateDto> issueCertificate(

            @PathVariable Long courseId) {

        CertificateDto certificateDto = certificateService.issueCertificate(courseId);
        return ResponseEntity.ok(certificateDto);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<CertificateDto>> getMyCertificates() {
        List<CertificateDto> certificates = certificateService.getMyCertificates();
        return ResponseEntity.ok(certificates);
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<List<CertificateDto>> getCertificatesByStudent(

            @PathVariable Long studentId) {

        List<CertificateDto> certificates = certificateService.getCertificatesByStudent(studentId);
        return ResponseEntity.ok(certificates);
    }

    @GetMapping("/student/{studentId}/revoked")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CertificateDto>> getRevokedCertificatesByStudent(

            @PathVariable Long studentId) {

        List<CertificateDto> certificates = certificateService.listRevokedCertificatesByStudent(studentId);
        return ResponseEntity.ok(certificates);
    }

    @PutMapping("/{certificateId}/revoke")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CertificateDto> revokeCertificate(

            @PathVariable Long certificateId) {

        CertificateDto certificateDto = certificateService.revokeCertificate(certificateId);
        return ResponseEntity.ok(certificateDto);
    }

    @GetMapping("/code/{certificateCode}")
    public ResponseEntity<CertificateDto> getCertificateByCode(

            @PathVariable String certificateCode) {

        CertificateDto certificateDto = certificateService.getCertificateByCode(certificateCode);
        return ResponseEntity.ok(certificateDto);
    }

    @GetMapping("/check/{courseId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Boolean> hasCertificateForCourse(

            @PathVariable Long courseId) {

        // Obtém o ID do estudante autenticado do contexto de segurança
        // Esta lógica precisaria ser implementada no service
        // Por enquanto, retornamos um exemplo
        boolean hasCertificate = certificateService.hasCertificateForCourse(1L, courseId); // Exemplo
        return ResponseEntity.ok(hasCertificate);
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<List<CertificateDto>> getCertificatesByCourse(

            @PathVariable Long courseId) {

        // Este método precisaria ser implementado no service e repository
        // Por enquanto, retornamos uma lista vazia como exemplo
        return ResponseEntity.ok(List.of());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno do servidor: " + ex.getMessage());
    }
}