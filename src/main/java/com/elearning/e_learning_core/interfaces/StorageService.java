package com.elearning.e_learning_core.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String uploadFile(MultipartFile file, String path);

}