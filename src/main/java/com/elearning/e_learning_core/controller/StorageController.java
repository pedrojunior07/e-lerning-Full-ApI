package com.elearning.e_learning_core.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.elearning.e_learning_core.interfaces.StorageService;

@RestController
@RequestMapping("/storage")
public class StorageController {

    private final StorageService storageService;

    public StorageController(@Qualifier("bunny") StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "imagens") String path) {
        String url = storageService.uploadFile(file, path);

        Map<String, Object> response = Map.of(
                "status", "success",
                "message", "Arquivo enviado com sucesso",
                "url", url);

        return ResponseEntity.ok(response);
    }

}