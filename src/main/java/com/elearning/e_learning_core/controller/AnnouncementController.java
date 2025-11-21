package com.elearning.e_learning_core.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.e_learning_core.Dtos.AnnouncementDto;
import com.elearning.e_learning_core.service.AnnouncementService;

@RestController
@RequestMapping("/announcements")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @PostMapping
    public ResponseEntity<AnnouncementDto> createAnnouncement(@RequestBody AnnouncementDto dto) {
        AnnouncementDto created = announcementService.createAnnouncement(dto);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public List<AnnouncementDto> listAllAnnouncements() {
        return announcementService.listAnnouncement();
    }
}