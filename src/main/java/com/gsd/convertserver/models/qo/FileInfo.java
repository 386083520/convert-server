package com.gsd.convertserver.models.qo;

import lombok.Data;

@Data
public class FileInfo {
    private String filePath;
    private String convertType;
    private String fileName;
}
