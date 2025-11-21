package com.elearning.e_learning_core.Dtos;

public class CourseDtoStep2 {
    private String introVideoUrl;
    private String videoProvider;
    private String thumbnailPath;

    public CourseDtoStep2() {
    }

    public CourseDtoStep2(String introVideoUrl, String videoProvider, String thumbnailPath) {
        this.introVideoUrl = introVideoUrl;
        this.videoProvider = videoProvider;
        this.thumbnailPath = thumbnailPath;
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

}
