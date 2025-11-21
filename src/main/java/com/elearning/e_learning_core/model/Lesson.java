package com.elearning.e_learning_core.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "lesson")
public class Lesson extends BaseEntity {

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private CourseModule module;

    public Lesson() {
        super();
    }

    public Lesson(CourseModule module) {
        this.module = module;
    }

    public CourseModule getModule() {
        return module;
    }

    public void setModule(CourseModule module) {
        this.module = module;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Lesson(String title, String content, CourseModule module) {
        this.title = title;
        this.content = content;
        this.module = module;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

}
