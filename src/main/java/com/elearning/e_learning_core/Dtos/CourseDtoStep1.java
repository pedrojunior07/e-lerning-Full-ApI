package com.elearning.e_learning_core.Dtos;

import java.util.ArrayList;
import java.util.List;

import com.elearning.e_learning_core.model.LevelCurseType;

public class CourseDtoStep1 {

    private String title;

    private Long courseId;

    private String category;

    private LevelCurseType level;

    private String language;

    private Integer maxStudents;

    private String publicOrPrivate;
    private long categoryId;

    private String shortDescription;

    private String longDescription;

    private List<String> whatStudentsWillLearn = new ArrayList<>();

    private List<String> requirements = new ArrayList<>();

    public CourseDtoStep1() {
    }

    public CourseDtoStep1(String title, String category, LevelCurseType level, String language, Integer maxStudents,
            String publicOrPrivate, long categoryId, String shortDescription, String longDescription,
            List<String> whatStudentsWillLearn, List<String> requirements) {
        this.title = title;
        this.category = category;
        this.level = level;
        this.language = language;
        this.maxStudents = maxStudents;
        this.publicOrPrivate = publicOrPrivate;
        this.categoryId = categoryId;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.whatStudentsWillLearn = whatStudentsWillLearn;
        this.requirements = requirements;
    }

    public CourseDtoStep1(String title, Long courseId, String category, LevelCurseType level, String language,
            Integer maxStudents, String publicOrPrivate, long categoryId, String shortDescription,
            String longDescription, List<String> whatStudentsWillLearn, List<String> requirements) {
        this.title = title;
        this.courseId = courseId;
        this.category = category;
        this.level = level;
        this.language = language;
        this.maxStudents = maxStudents;
        this.publicOrPrivate = publicOrPrivate;
        this.categoryId = categoryId;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.whatStudentsWillLearn = whatStudentsWillLearn;
        this.requirements = requirements;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LevelCurseType getLevel() {
        return level;
    }

    public void setLevel(LevelCurseType level) {
        this.level = level;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public String getPublicOrPrivate() {
        return publicOrPrivate;
    }

    public void setPublicOrPrivate(String publicOrPrivate) {
        this.publicOrPrivate = publicOrPrivate;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public List<String> getWhatStudentsWillLearn() {
        return whatStudentsWillLearn;
    }

    public void setWhatStudentsWillLearn(List<String> whatStudentsWillLearn) {
        this.whatStudentsWillLearn = whatStudentsWillLearn;
    }

    public List<String> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<String> requirements) {
        this.requirements = requirements;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCourseId() {
        return courseId;

    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

}