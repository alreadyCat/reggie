package org.reggie.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    public String uploadFile(MultipartFile file);
}
