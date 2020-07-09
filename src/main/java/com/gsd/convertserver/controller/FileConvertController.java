package com.gsd.convertserver.controller;

import com.gsd.convertserver.models.ResponseData;
import com.gsd.convertserver.models.qo.FileInfo;
import com.gsd.convertserver.service.FileConvertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/convert", produces = "application/json; charset=UTF-8")
public class FileConvertController {
    @Autowired
    FileConvertService fileConvertService;

    @PostMapping(value = "/convertFile")
    public ResponseData<String> convertFile(@RequestBody FileInfo fileInfo) {
        return ResponseData.success(fileConvertService.convertFile(fileInfo));
    }

    @PostMapping(value = "/pdfToImg")
    public ResponseData<String> pdfToImg(@RequestBody FileInfo fileInfo) {
        return ResponseData.success(fileConvertService.pdfToImg(fileInfo));
    }

    @PostMapping(value = "/imgToPdf")
    public ResponseData<String> imgToPdf(@RequestBody FileInfo fileInfo) {
        return ResponseData.success();
    }
}
