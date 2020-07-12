package com.gsd.convertserver.controller;

import com.gsd.convertserver.models.ResponseData;
import com.gsd.convertserver.models.qo.FileInfo;
import com.gsd.convertserver.service.FileUploadService;
import com.gsd.convertserver.utils.FileDfsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RestController
@RequestMapping(value = "/upload", produces = "application/json; charset=UTF-8")
public class FileUploadController {
    @Autowired
    FileUploadService fileUploadService;

    @Resource
    private FileDfsUtil fileDfsUtil ;

    @PostMapping(value = "/uploadFile")
    public ResponseData<String> uploadFile(FileInfo fileInfo, HttpServletRequest request) {
        return fileUploadService.uploadFile(fileInfo);
    }
    /**
     * 文件删除
     */
    @RequestMapping(value = "/deleteByPath", method = RequestMethod.GET)
    public ResponseEntity<String> deleteByPath (@RequestParam String filePath){
        fileDfsUtil.deleteFile(filePath);
        return ResponseEntity.ok("SUCCESS") ;
    }



}
