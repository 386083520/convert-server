package com.gsd.convertserver.service;

import com.gsd.convertserver.models.ResponseData;
import com.gsd.convertserver.models.qo.FileInfo;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    ResponseData<String> uploadFile(FileInfo fileInfo);
}
