package com.gsd.convertserver.controller;


import com.gsd.convertserver.models.ResponseData;
import com.gsd.convertserver.models.qo.FileInfo;
import com.gsd.convertserver.service.FileDownloadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping(value = "/download", produces = "application/json; charset=UTF-8")
public class FileDownloadController {
    @Autowired
    FileDownloadService fileDownloadService;

    @GetMapping(value = "/downloadFile")
    public void downloadFile(HttpServletResponse response, @RequestParam String filePath) {
        fileDownloadService.downloadFile(response, filePath);
    }

}
