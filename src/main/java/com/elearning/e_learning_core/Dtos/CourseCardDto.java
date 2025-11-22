package com.elearning.e_learning_core.Dtos;

import java.math.BigDecimal;

import com.elearning.e_learning_core.model.Category;
import com.elearning.e_learning_core.model.Course.StatusCourse;

public class CourseCardDto {
    private String thumbnailPath;
    private String discountText;
    private String instructorName;
    private Category category;
    private String title;
    private String ratingText;
    private BigDecimal price;
    private long id;
    private Long lessonCount;
    private StatusCourse status;
    private Long studentCount;
    private boolean hasLiked;
    private Long likeCount;
    private Long instructorId;

    public CourseCardDto(String thumbnailPath, String discountText, String instructorName, Category category,
            String title, String ratingText, BigDecimal price, long id, Long lessonCount) {
        this.thumbnailPath = thumbnailPath;
        this.discountText = discountText;
        this.instructorName = instructorName;
        this.category = category;
        this.title = title;
        this.ratingText = ratingText;
        this.price = price;
        this.id = id;
        this.lessonCount = lessonCount;
    }

    public CourseCardDto(
            String thumbnailPath,
            String instructorFullName,
            Category category,
            String title,
            BigDecimal price,
            long id,
            Long lessonsCount, Long studentCount, StatusCourse status) {

        this.thumbnailPath = thumbnailPath;
        this.status = status;
        this.instructorName = instructorFullName;
        this.category = category;
        this.title = title;
        this.studentCount = studentCount;
        this.price = price;
        this.id = id;
        this.lessonCount = lessonsCount;

    }

    public CourseCardDto() {
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getDiscountText() {
        return discountText;
    }

    public void setDiscountText(String discountText) {
        this.discountText = discountText;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRatingText() {
        return ratingText;
    }

    public void setRatingText(String ratingText) {
        this.ratingText = ratingText;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public StatusCourse getStatus() {
        return status;
    }

    public void setStatus(StatusCourse status) {
        this.status = status;
    }

    public Long getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(Long studentCount) {
        this.studentCount = studentCount;
    }

    public Long getLessonCount() {
        return lessonCount;
    }

    public void setLessonCount(Long lessonCount) {
        this.lessonCount = lessonCount;
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

    public Long getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Long instructorId) {
        this.instructorId = instructorId;
    }

}