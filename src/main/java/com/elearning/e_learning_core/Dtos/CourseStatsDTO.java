package com.elearning.e_learning_core.Dtos;

public class CourseStatsDTO {
    private long total;
    private long published;
    private long draft;
    private long inactive;
    private long archived;
    private long paid;
    private long free;

    public CourseStatsDTO(long total, long published, long draft, long inactive, long archived, long paid, long free) {
        this.total = total;
        this.published = published;
        this.draft = draft;
        this.inactive = inactive;
        this.archived = archived;
        this.paid = paid;
        this.free = free;
    }

    // Getters e Setters
    public long getTotal() {
        return total;
    }

    public long getPublished() {
        return published;
    }

    public long getDraft() {
        return draft;
    }

    public long getInactive() {
        return inactive;
    }

    public long getArchived() {
        return archived;
    }

    public long getPaid() {
        return paid;
    }

    public long getFree() {
        return free;
    }
}
