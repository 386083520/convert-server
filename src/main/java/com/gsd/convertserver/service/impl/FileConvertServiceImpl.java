package com.gsd.convertserver.service.impl;

import com.gsd.convertserver.models.qo.FileInfo;
import com.gsd.convertserver.service.FileConvertService;
import com.gsd.convertserver.utils.OfficeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Service
@Slf4j
public class FileConvertServiceImpl implements FileConvertService{
    @Resource
    private OfficeUtils  officeUtils;
    @Value("${convert.file.path}")
    private String convertFilePath;

    @Override
    public String convertFile(FileInfo fileInfo) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String fullPath = convertFilePath.concat("/").concat(uuid);
        String inputPath = convertFilePath.concat("/").concat(fileInfo.getFilePath());
        String[] fileParams = fileInfo.getFilePath().split("/");
        String fileName = fileParams[1].split("\\.")[0];
        log.info("文件名：" + fileParams[1].split("\\."));
        String outputPath = fullPath.concat("/").concat(fileName).concat(".").concat("pdf");
        officeUtils.fileConvert(inputPath, outputPath);
        return uuid.concat("/").concat(fileName).concat(".").concat("pdf");
    }
}
