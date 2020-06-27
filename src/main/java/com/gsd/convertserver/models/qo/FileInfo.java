package com.gsd.convertserver.models.qo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileInfo {
    private String uuid;
    private String filePath;
    private String convertType;
    private String fileName;
    private MultipartFile file;
}
