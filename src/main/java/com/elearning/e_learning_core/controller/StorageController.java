package com.elearning.e_learning_core.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.elearning.e_learning_core.service.LocalStorageService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/storage")
public class StorageController {

    private final LocalStorageService storageService;

    public StorageController(@Qualifier("localStorage") LocalStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "imagens") String path) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Nenhum arquivo enviado"
            ));
        }

        String url = storageService.uploadFile(file, path);

        Map<String, Object> response = Map.of(
                "status", "success",
                "message", "Arquivo enviado com sucesso",
                "url", url);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/upload/video")
    public ResponseEntity<?> uploadVideo(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Nenhum arquivo enviado"
            ));
        }

        try {
            Map<String, Object> data = storageService.uploadVideo(file);
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Vídeo enviado com sucesso",
                "url", data.get("fullUrl"),
                "data", data
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/upload/thumbnail")
    public ResponseEntity<?> uploadThumbnail(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Nenhum arquivo enviado"
            ));
        }

        try {
            Map<String, Object> data = storageService.uploadThumbnail(file);
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Thumbnail enviada com sucesso",
                "url", data.get("fullUrl"),
                "data", data
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/upload/pdf")
    public ResponseEntity<?> uploadPdf(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Nenhum arquivo enviado"
            ));
        }

        try {
            Map<String, Object> data = storageService.uploadPdf(file);
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "PDF enviado com sucesso",
                "url", data.get("fullUrl"),
                "data", data
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/upload/{type}/{filename}")
    public ResponseEntity<?> deleteFile(
            @PathVariable String type,
            @PathVariable String filename) {

        try {
            storageService.deleteFile(type, filename);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Arquivo deletado com sucesso"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/upload/list/{type}")
    public ResponseEntity<?> listFiles(@PathVariable String type) {
        try {
            List<Map<String, Object>> files = storageService.listFiles(type);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "data", files
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", e.getMessage()
            ));
        }
    }
}
