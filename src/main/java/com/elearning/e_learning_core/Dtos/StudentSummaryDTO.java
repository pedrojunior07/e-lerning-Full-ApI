package com.elearning.e_learning_core.Dtos;

import java.time.LocalDateTime;

public record StudentSummaryDTO(
        Long id,
        String name,
        LocalDateTime createdAt,
        int totalCourses) {
}