package com.gsd.convertserver.service;

import com.gsd.convertserver.models.qo.FileInfo;
import com.gsd.convertserver.models.qo.ProposeInfoQo;

public interface FileConvertService {
    String convertFile(FileInfo fileInfo);
    String pdfToImg(FileInfo fileInfo);
    String proposeInsert(ProposeInfoQo proposeInfoQo);
}
