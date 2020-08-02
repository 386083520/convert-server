package com.gsd.convertserver.service;

import com.gsd.convertserver.models.qo.FileInfo;
import org.springframework.web.multipart.MultipartFile;

public interface FileChunkService {
    String chunkFile(MultipartFile file, String uuid, Integer chunk, Integer chunks);
    Object[] mergeFile(FileInfo fileInfo);
}
