package com.elearning.e_learning_core.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.elearning.e_learning_core.interfaces.StorageService;

@Service("localStorage")
public class LocalStorageService implements StorageService {

    @Value("${app.upload.base-path:/var/www/files}")
    private String basePath;

    @Value("${APP_BASE_URL:http://localhost:8080}")
    private String baseUrl;

    private static final List<String> VIDEO_EXTENSIONS = Arrays.asList("mp4", "avi", "mov", "mkv", "webm", "flv", "wmv");
    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "webp", "bmp");
    private static final List<String> PDF_EXTENSIONS = Arrays.asList("pdf");

    @Override
    public String uploadFile(MultipartFile file, String path) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            // Usa File.separator para compatibilidade Windows/Linux
            Path uploadDir = Paths.get(basePath, path);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path destination = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            return baseUrl + "/e-learning/api/media/" + path + "/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Falha no upload", e);
        }
    }

    public Map<String, Object> uploadVideo(MultipartFile file) {
        validateFileType(file, VIDEO_EXTENSIONS, "video");
        String fileName = UUID.randomUUID() + "." + getExtension(file.getOriginalFilename());

        try {
            Path uploadDir = Paths.get(basePath, "video");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path destination = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            Map<String, Object> data = new HashMap<>();
            data.put("filename", fileName);
            data.put("originalName", file.getOriginalFilename());
            data.put("size", file.getSize());
            data.put("url", "/media/video/" + fileName);
            data.put("fullUrl", baseUrl + "/e-learning/api/media/video/" + fileName);

            return data;

        } catch (IOException e) {
            throw new RuntimeException("Falha no upload do vídeo", e);
        }
    }

    public Map<String, Object> uploadThumbnail(MultipartFile file) {
        validateFileType(file, IMAGE_EXTENSIONS, "imagem");
        String fileName = UUID.randomUUID() + "." + getExtension(file.getOriginalFilename());

        try {
            Path uploadDir = Paths.get(basePath, "thumbnail");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path destination = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            Map<String, Object> data = new HashMap<>();
            data.put("filename", fileName);
            data.put("originalName", file.getOriginalFilename());
            data.put("size", file.getSize());
            data.put("url", "/media/thumbnail/" + fileName);
            data.put("fullUrl", baseUrl + "/e-learning/api/media/thumbnail/" + fileName);

            return data;

        } catch (IOException e) {
            throw new RuntimeException("Falha no upload da thumbnail", e);
        }
    }

    public Map<String, Object> uploadPdf(MultipartFile file) {
        validateFileType(file, PDF_EXTENSIONS, "PDF");
        String fileName = UUID.randomUUID() + "." + getExtension(file.getOriginalFilename());

        try {
            Path uploadDir = Paths.get(basePath, "pdf");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path destination = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            Map<String, Object> data = new HashMap<>();
            data.put("filename", fileName);
            data.put("originalName", file.getOriginalFilename());
            data.put("size", file.getSize());
            data.put("url", "/media/pdf/" + fileName);
            data.put("fullUrl", baseUrl + "/e-learning/api/media/pdf/" + fileName);

            return data;

        } catch (IOException e) {
            throw new RuntimeException("Falha no upload do PDF", e);
        }
    }

    public void deleteFile(String type, String filename) {
        try {
            Path filePath = Paths.get(basePath, type, filename);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            } else {
                throw new RuntimeException("Arquivo não encontrado");
            }
        } catch (IOException e) {
            throw new RuntimeException("Falha ao deletar arquivo", e);
        }
    }

    public List<Map<String, Object>> listFiles(String type) {
        List<Map<String, Object>> files = new ArrayList<>();

        try {
            Path dir = Paths.get(basePath, type);
            if (!Files.exists(dir)) {
                return files;
            }

            Files.list(dir).forEach(path -> {
                try {
                    Map<String, Object> fileInfo = new HashMap<>();
                    fileInfo.put("filename", path.getFileName().toString());
                    fileInfo.put("size", Files.size(path));
                    fileInfo.put("created", Files.getLastModifiedTime(path).toString());
                    fileInfo.put("url", "/media/" + type + "/" + path.getFileName().toString());
                    files.add(fileInfo);
                } catch (IOException e) {
                    // Skip files that can't be read
                }
            });

        } catch (IOException e) {
            throw new RuntimeException("Falha ao listar arquivos", e);
        }

        return files;
    }

    public Path getFilePath(String type, String filename) {
        return Paths.get(basePath, type, filename);
    }

    public boolean fileExists(String type, String filename) {
        return Files.exists(getFilePath(type, filename));
    }

    private void validateFileType(MultipartFile file, List<String> allowedExtensions, String typeName) {
        String extension = getExtension(file.getOriginalFilename()).toLowerCase();
        if (!allowedExtensions.contains(extension)) {
            throw new RuntimeException("Tipo de arquivo inválido. Apenas " + typeName + " é permitido: " + String.join(", ", allowedExtensions));
        }
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
