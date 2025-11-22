package com.elearning.e_learning_core.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.elearning.e_learning_core.Repository.StudentRepository;
import com.elearning.e_learning_core.service.LocalStorageService;

@RestController
@RequestMapping("/media")
public class MediaController {

    private final LocalStorageService storageService;
    private final StudentRepository studentRepository;

    public MediaController(LocalStorageService storageService, StudentRepository studentRepository) {
        this.storageService = storageService;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/video/{filename}")
    public ResponseEntity<StreamingResponseBody> streamVideo(
            @PathVariable String filename,
            @RequestHeader(value = "Range", required = false) String rangeHeader) {

        Path videoPath = storageService.getFilePath("video", filename);

        if (!Files.exists(videoPath)) {
            return ResponseEntity.notFound().build();
        }

        try {
            long fileSize = Files.size(videoPath);

            if (rangeHeader == null) {
                // Full file request
                StreamingResponseBody responseBody = outputStream -> {
                    try (InputStream inputStream = Files.newInputStream(videoPath)) {
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                };

                return ResponseEntity.ok()
                        .contentType(MediaType.valueOf("video/mp4"))
                        .contentLength(fileSize)
                        .body(responseBody);
            }

            // Range request for video streaming
            String[] ranges = rangeHeader.replace("bytes=", "").split("-");
            long start = Long.parseLong(ranges[0]);
            long end = ranges.length > 1 && !ranges[1].isEmpty()
                    ? Long.parseLong(ranges[1])
                    : fileSize - 1;

            if (end >= fileSize) {
                end = fileSize - 1;
            }

            long contentLength = end - start + 1;
            final long finalStart = start;
            final long finalEnd = end;

            StreamingResponseBody responseBody = outputStream -> {
                try (InputStream inputStream = Files.newInputStream(videoPath)) {
                    inputStream.skip(finalStart);
                    byte[] buffer = new byte[8192];
                    long remaining = finalEnd - finalStart + 1;
                    int bytesRead;
                    while (remaining > 0 && (bytesRead = inputStream.read(buffer, 0, (int) Math.min(buffer.length, remaining))) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                        remaining -= bytesRead;
                    }
                }
            };

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Range", "bytes " + start + "-" + end + "/" + fileSize);
            headers.set("Accept-Ranges", "bytes");
            headers.setContentLength(contentLength);
            headers.setContentType(MediaType.valueOf("video/mp4"));

            return new ResponseEntity<>(responseBody, headers, HttpStatus.PARTIAL_CONTENT);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/protected/video/{studentId}/{courseId}/{filename}")
    public ResponseEntity<StreamingResponseBody> streamProtectedVideo(
            @PathVariable Long studentId,
            @PathVariable Long courseId,
            @PathVariable String filename,
            @RequestHeader(value = "Range", required = false) String rangeHeader) {

        // Check if student has access to course
        if (!hasAccessToCourse(studentId, courseId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return streamVideo(filename, rangeHeader);
    }

    @GetMapping("/pdf/{filename}")
    public ResponseEntity<byte[]> servePdf(@PathVariable String filename) {
        Path pdfPath = storageService.getFilePath("pdf", filename);

        if (!Files.exists(pdfPath)) {
            return ResponseEntity.notFound().build();
        }

        try {
            byte[] content = Files.readAllBytes(pdfPath);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", filename);
            headers.setContentLength(content.length);

            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/protected/pdf/{studentId}/{courseId}/{filename}")
    public ResponseEntity<byte[]> serveProtectedPdf(
            @PathVariable Long studentId,
            @PathVariable Long courseId,
            @PathVariable String filename) {

        // Check if student has access to course
        if (!hasAccessToCourse(studentId, courseId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return servePdf(filename);
    }

    @GetMapping("/thumbnail/{filename}")
    public ResponseEntity<byte[]> serveThumbnail(@PathVariable String filename) {
        Path thumbnailPath = storageService.getFilePath("thumbnail", filename);

        if (!Files.exists(thumbnailPath)) {
            return ResponseEntity.notFound().build();
        }

        try {
            byte[] content = Files.readAllBytes(thumbnailPath);
            String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
            MediaType mediaType = getImageMediaType(extension);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
            headers.setContentLength(content.length);

            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{type}/{filename}")
    public ResponseEntity<byte[]> serveFile(
            @PathVariable String type,
            @PathVariable String filename) {

        Path filePath = storageService.getFilePath(type, filename);

        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        try {
            byte[] content = Files.readAllBytes(filePath);
            String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
            MediaType mediaType = getMediaType(extension);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
            headers.setContentLength(content.length);

            return new ResponseEntity<>(content, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private boolean hasAccessToCourse(Long studentId, Long courseId) {
        // Check if student is enrolled in the course
        return studentRepository.isEnrolledInCourse(studentId, courseId);
    }

    private MediaType getImageMediaType(String extension) {
        switch (extension) {
            case "png":
                return MediaType.IMAGE_PNG;
            case "gif":
                return MediaType.IMAGE_GIF;
            case "webp":
                return MediaType.valueOf("image/webp");
            case "bmp":
                return MediaType.valueOf("image/bmp");
            default:
                return MediaType.IMAGE_JPEG;
        }
    }

    private MediaType getMediaType(String extension) {
        switch (extension) {
            case "mp4":
            case "webm":
                return MediaType.valueOf("video/" + extension);
            case "pdf":
                return MediaType.APPLICATION_PDF;
            case "png":
                return MediaType.IMAGE_PNG;
            case "gif":
                return MediaType.IMAGE_GIF;
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
