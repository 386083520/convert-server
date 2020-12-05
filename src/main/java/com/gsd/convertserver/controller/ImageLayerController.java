package com.gsd.convertserver.controller;

import com.gsd.convertserver.models.ResponseData;
import com.gsd.convertserver.models.qo.FileInfo;
import com.gsd.convertserver.models.vo.ImageInfo;
import com.gsd.convertserver.service.FileUploadService;
import com.gsd.convertserver.service.ImageLayerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/imageLayer", produces = "application/json; charset=UTF-8")
public class ImageLayerController {
    @Autowired
    ImageLayerService imageLayerService;

    @GetMapping(value = "/getImageUrl/{uuid}")
    public ResponseData<ImageInfo> convertFile(@PathVariable("uuid") String uuid) {
        return ResponseData.success(imageLayerService.getImageUrl(uuid));
    }

    @GetMapping(value = "/getOcrResult/{id}")
    public ResponseData<String> getOcrResult(@PathVariable("id") String id) {
        return ResponseData.success(imageLayerService.getOcrResult(id));
    }
}
