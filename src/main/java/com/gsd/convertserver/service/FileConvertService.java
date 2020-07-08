package com.gsd.convertserver.service;

import com.gsd.convertserver.models.qo.FileInfo;

public interface FileConvertService {
    String convertFile(FileInfo fileInfo);
    String pdfToImg(FileInfo fileInfo);
}
