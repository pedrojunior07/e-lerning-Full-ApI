package com.elearning.e_learning_core.Dtos;

import java.time.LocalDate;

public class ExperienceDTO {

    private String jobTitle;
    private String company;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean current;

    public ExperienceDTO() {
    }

    public ExperienceDTO(String jobTitle, String company, LocalDate startDate, LocalDate endDate, Boolean current) {
        this.jobTitle = jobTitle;
        this.company = company;
        this.startDate = startDate;
        this.endDate = endDate;
        this.current = current;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getCurrent() {
        return current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }
}