package org.reggie.controller;

import org.reggie.common.Res;
import org.reggie.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/common")
public class CommonController {

  @Autowired
  UploadService uploadService;
  @PostMapping("/upload")
  public Res<String> upload(MultipartFile file) {
    String url = uploadService.uploadFile(file);
    return Res.success(url);
  }
}
