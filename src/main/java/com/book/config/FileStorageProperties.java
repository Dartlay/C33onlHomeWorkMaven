package com.book.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;
    private String bookFilesDir = "books";
    private String coverImagesDir = "covers";
    private String maxFileSize = "10MB";

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String getBookFilesDir() {
        return bookFilesDir;
    }

    public void setBookFilesDir(String bookFilesDir) {
        this.bookFilesDir = bookFilesDir;
    }

    public String getCoverImagesDir() {
        return coverImagesDir;
    }

    public void setCoverImagesDir(String coverImagesDir) {
        this.coverImagesDir = coverImagesDir;
    }

    public String getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(String maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public String getFullBookFilesPath() {
        return uploadDir + "/" + bookFilesDir;
    }

    public String getFullCoverImagesPath() {
        return uploadDir + "/" + coverImagesDir;
    }
}