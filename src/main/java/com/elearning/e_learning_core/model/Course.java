package com.elearning.e_learning_core.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "course")
public class Course extends BaseEntity {

	private String title;

	@Enumerated(EnumType.STRING)
	private LevelCurseType level = LevelCurseType.BEGINNER;

	private String language;

	@Column(name = "price")
	private BigDecimal price = BigDecimal.ZERO;

	@Column(name = "is_free", columnDefinition = "BOOLEAN DEFAULT false")
	private boolean isFree = false;

	@Column(name = "has_discount", columnDefinition = "BOOLEAN DEFAULT false")
	private boolean hasDiscount = false;

	private BigDecimal discountPrice;
	private ExpiryType expiryType;
	private Integer expiryMonths;

	@Column(name = "max_students")
	private Integer maxStudents;

	@Column(name = "public_or_private")
	private String publicOrPrivate;

	@Column(name = "short_description", length = 500)
	private String shortDescription;

	@Column(name = "long_description", columnDefinition = "TEXT")
	private String longDescription;

	private String introVideoUrl;

	private String videoProvider;

	private String thumbnailPath;

	@Column(name = "current_step", nullable = false)
	private Integer currentStep = 1;

	@ElementCollection
	@CollectionTable(name = "course_what_students_will_learn", joinColumns = @JoinColumn(name = "course_id"))
	@Column(name = "item")
	private List<String> whatStudentsWillLearn = new ArrayList<>();

	// NOVO: Relacionamento com CourseLike
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<CourseLike> likes = new ArrayList<>();
	@ElementCollection
	@CollectionTable(name = "course_requirements", joinColumns = @JoinColumn(name = "course_id"))
	@Column(name = "requirement")
	private List<String> requirements = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	private StatusCourse status = StatusCourse.PUBLISHED;

	public enum StatusCourse {
		PUBLISHED,
		DRAFT,
		INACTIVE,
		ARCHIVED,
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "instructor_id", nullable = false)
	private Instructor instructor;
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	private List<Announcement> announcements = new ArrayList<>();
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	private List<CourseModule> modules;

	@ManyToMany(mappedBy = "courses")
	private List<Student> students = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	public Course() {
	}

	public Course(String title, Category category, LevelCurseType level, String language, Integer maxStudents,
			String publicOrPrivate, String shortDescription, String longDescription, List<String> whatStudentsWillLearn,
			List<String> requirements, StatusCourse status, Instructor instructor, List<CourseModule> modules,
			List<Student> students, List<Category> categories) {
		this.title = title;
		this.category = category;
		this.level = level;
		this.language = language;
		this.maxStudents = maxStudents;
		this.publicOrPrivate = publicOrPrivate;
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
		this.whatStudentsWillLearn = whatStudentsWillLearn;
		this.requirements = requirements;
		this.status = status;
		this.instructor = instructor;
		this.modules = modules;
		this.students = students;

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	// Método utilitário para obter contagem de likes
	public Long getLikeCount() {
		return (long) likes.size();
	}

	public boolean isLikedByStudent(Student student) {
		return likes.stream().anyMatch(like -> like.getStudent().equals(student));
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

	public Course(String title, LevelCurseType level, String language, BigDecimal price, boolean isFree,
			boolean hasDiscount, BigDecimal discountPrice, ExpiryType expiryType, Integer expiryMonths,
			Integer maxStudents, String publicOrPrivate, String shortDescription, String longDescription,
			String introVideoUrl, String videoProvider, String thumbnailPath, Integer currentStep,
			List<String> whatStudentsWillLearn, List<String> requirements, StatusCourse status, Instructor instructor,
			List<Announcement> announcements, List<CourseModule> modules, List<Student> students, Category category) {
		this.title = title;
		this.level = level;
		this.language = language;
		this.price = price;
		this.isFree = isFree;
		this.hasDiscount = hasDiscount;
		this.discountPrice = discountPrice;
		this.expiryType = expiryType;
		this.expiryMonths = expiryMonths;
		this.maxStudents = maxStudents;
		this.publicOrPrivate = publicOrPrivate;
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
		this.introVideoUrl = introVideoUrl;
		this.videoProvider = videoProvider;
		this.thumbnailPath = thumbnailPath;
		this.currentStep = currentStep;
		this.whatStudentsWillLearn = whatStudentsWillLearn;
		this.requirements = requirements;
		this.status = status;
		this.instructor = instructor;
		this.announcements = announcements;
		this.modules = modules;
		this.students = students;
		this.category = category;
	}

	public StatusCourse getStatus() {
		return status;
	}

	public void setStatus(StatusCourse status) {
		this.status = status;
	}

	public Instructor getInstructor() {
		return instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}

	public List<CourseModule> getModules() {
		return modules;
	}

	public void setModules(List<CourseModule> modules) {
		this.modules = modules;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public String getIntroVideoUrl() {
		return introVideoUrl;
	}

	public Course(String title, Category category, LevelCurseType level, String language, boolean isFree,
			BigDecimal price,
			boolean hasDiscount, BigDecimal discountPrice, ExpiryType expiryType, Integer expiryMonths,
			Integer maxStudents, String publicOrPrivate, String shortDescription, String longDescription,
			String introVideoUrl, String videoProvider, String thumbnailPath, List<String> whatStudentsWillLearn,
			List<String> requirements, StatusCourse status, Instructor instructor, List<CourseModule> modules,
			List<Student> students) {
		this.title = title;

		this.level = level;
		this.language = language;
		this.isFree = isFree;
		this.price = price;
		this.hasDiscount = hasDiscount;
		this.discountPrice = discountPrice;
		this.expiryType = expiryType;
		this.expiryMonths = expiryMonths;
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
		this.students = students;
		this.category = category;
	}

	public boolean isFree() {
		return isFree;
	}

	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public boolean isHasDiscount() {
		return hasDiscount;
	}

	public void setHasDiscount(boolean hasDiscount) {
		this.hasDiscount = hasDiscount;
	}

	public BigDecimal getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(BigDecimal discountPrice) {
		this.discountPrice = discountPrice;
	}

	public ExpiryType getExpiryType() {
		return expiryType;
	}

	public void setExpiryType(ExpiryType expiryType) {
		this.expiryType = expiryType;
	}

	public Integer getExpiryMonths() {
		return expiryMonths;
	}

	public void setExpiryMonths(Integer expiryMonths) {
		this.expiryMonths = expiryMonths;
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

	public Integer getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(Integer currentStep) {
		this.currentStep = currentStep;
	}

	public List<Announcement> getAnnouncements() {
		return announcements;
	}

	public void setAnnouncements(List<Announcement> announcements) {
		this.announcements = announcements;
	}

}
