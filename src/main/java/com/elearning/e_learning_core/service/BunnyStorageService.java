package com.elearning.e_learning_core.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.elearning.e_learning_core.interfaces.StorageService;

@Service("bunny")
public class BunnyStorageService implements StorageService {

    @Value("${bunny.storage.zone}")
    private String storageZone;

    @Value("${bunny.storage.apiKey}")
    private String apiKey;

    @Value("${bunny.storage.region:}")
    private String storageZoneRegion;

    @Override
    public String uploadFile(MultipartFile file, String path) {

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            Path uploadDir = Paths.get("/var/www/files/" + path);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path destination = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            return "192.250.224.214:8585/e-learning/api/files/" + path + "/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Falha no upload", e);
        }
    }

}