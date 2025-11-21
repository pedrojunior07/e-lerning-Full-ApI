package com.elearning.e_learning_core.Dtos;

public class StatsDTO {

    private long onlineCourses;
    private long expertTutors;
    private long certifiedCourses;
    private long onlineStudents;

    public StatsDTO() {
    }

    public StatsDTO(long onlineCourses, long expertTutors, long certifiedCourses, long onlineStudents) {
        this.onlineCourses = onlineCourses;
        this.expertTutors = expertTutors;
        this.certifiedCourses = certifiedCourses;
        this.onlineStudents = onlineStudents;
    }

    public long getOnlineCourses() {
        return onlineCourses;
    }

    public void setOnlineCourses(long onlineCourses) {
        this.onlineCourses = onlineCourses;
    }

    public long getExpertTutors() {
        return expertTutors;
    }

    public void setExpertTutors(long expertTutors) {
        this.expertTutors = expertTutors;
    }

    public long getCertifiedCourses() {
        return certifiedCourses;
    }

    public void setCertifiedCourses(long certifiedCourses) {
        this.certifiedCourses = certifiedCourses;
    }

    public long getOnlineStudents() {
        return onlineStudents;
    }

    public void setOnlineStudents(long onlineStudents) {
        this.onlineStudents = onlineStudents;
    }
}