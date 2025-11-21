package com.elearning.e_learning_core.Dtos;

import java.math.BigInteger;
import java.util.List;

public class CourseDto {
    private Long id;
    private BigInteger price;
    private String title;
    private CategoryDTO category;
    private String level;
    private String language;
    private Integer maxStudents;
    private String publicOrPrivate;
    private String shortDescription;
    private String longDescription;
    private String introVideoUrl;
    private String videoProvider;
    private String thumbnailPath;
    private List<String> whatStudentsWillLearn;
    private List<String> requirements;
    private String status;
    private InstructorDto instructor;
    private List<CourseModuleDto> modules;
    private boolean hasLiked;
    private Long likeCount;

    public CourseDto() {
    }

    public CourseDto(Long id, String title, CategoryDTO category, String level, String language, Integer maxStudents,
            String publicOrPrivate, String shortDescription, String longDescription, String introVideoUrl,
            String videoProvider, String thumbnailPath, List<String> whatStudentsWillLearn, List<String> requirements,
            String status, InstructorDto instructor, List<CourseModuleDto> modules, BigInteger price) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.level = level;
        this.language = language;
        this.maxStudents = maxStudents;
        this.publicOrPrivate = publicOrPrivate;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
        this.introVideoUrl = introVideoUrl;
        this.videoProvider = videoProvider;
        this.thumbnailPath = thumbnailPath;
        this.whatStudentsWillLearn = whatStudentsWillLearn;
        this.requirements = requirements;
        this.status = status;
        this.instructor = instructor;
        this.modules = modules;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public boolean isHasLiked() {
        return hasLiked;
    }

    public void setHasLiked(boolean hasLiked) {
        this.hasLiked = hasLiked;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
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

    public String getIntroVideoUrl() {
        return introVideoUrl;
    }

    public void setIntroVideoUrl(String introVideoUrl) {
        this.introVideoUrl = introVideoUrl;
    }

    public String getVideoProvider() {
        return videoProvider;
    }

    public void setVideoProvider(String videoProvider) {
        this.videoProvider = videoProvider;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public InstructorDto getInstructor() {
        return instructor;
    }

    public void setInstructor(InstructorDto instructor) {
        this.instructor = instructor;
    }

    public List<CourseModuleDto> getModules() {
        return modules;
    }

    public void setModules(List<CourseModuleDto> modules) {
        this.modules = modules;
    }

    public BigInteger getPrice() {
        return price;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
    }

}