package com.gsd.convertserver.service;

import com.gsd.convertserver.models.qo.FileInfo;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String uploadFile(FileInfo fileInfo);
}
