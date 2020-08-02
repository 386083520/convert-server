package com.gsd.convertserver.controller;

import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.gsd.convertserver.models.ResponseData;
import com.gsd.convertserver.models.qo.FileInfo;
import com.gsd.convertserver.service.FileChunkService;
import com.gsd.convertserver.service.FileUploadService;
import com.gsd.convertserver.utils.CacheUtils;
import com.gsd.convertserver.utils.MergeFileUtil;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;


@Slf4j
@RestController
@RequestMapping(value = "/chunkfile", produces = "application/json; charset=UTF-8")
public class ChunkFileController {
    @Value("${convert.file.path}")
    private String convertFilePath;
    @Resource
    private FastFileStorageClient storageClient;
    @Resource
    private FileUploadService fileUploadService;
    @Resource
    private FileChunkService fileChunkService;

    /**
     * 文件分片上传
     */
    @PostMapping(value = "/chunkupload", headers = "content-type=multipart/form-data")
    public ResponseData<String> chunk (@ApiParam(value = "大文件", required = true)MultipartFile file, String uuid, Integer chunk, Integer chunks){
        String result = fileChunkService.chunkFile(file, uuid, chunk, chunks);
        return ResponseData.success(result);
    }

    @PostMapping(value = "/mergeFile")
    public ResponseData<Object[]> mergeFile(@RequestBody FileInfo fileInfo) throws Exception {
        Object[] objects = fileChunkService.mergeFile(fileInfo);
        return ResponseData.success(objects);
    }



}
