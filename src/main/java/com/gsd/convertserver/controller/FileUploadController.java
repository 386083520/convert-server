package com.gsd.convertserver.controller;

import com.gsd.convertserver.models.ResponseData;
import com.gsd.convertserver.models.qo.FileInfo;
import com.gsd.convertserver.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@RestController
@RequestMapping(value = "/upload", produces = "application/json; charset=UTF-8")
public class FileUploadController {
    @Autowired
    FileUploadService fileUploadService;

    @PostMapping(value = "/uploadFile")
    public ResponseData<String> uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        return ResponseData.success(fileUploadService.uploadFile(file));
    }



}
