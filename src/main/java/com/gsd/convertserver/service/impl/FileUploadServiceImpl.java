package com.gsd.convertserver.service.impl;

import com.gsd.convertserver.entity.FileUpload;
import com.gsd.convertserver.mapper.FileUploadMapper;
import com.gsd.convertserver.models.ResponseData;
import com.gsd.convertserver.models.qo.FileInfo;
import com.gsd.convertserver.service.FileUploadService;
import com.gsd.convertserver.utils.FileDfsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService{
    @Value("${convert.file.path}")
    private String convertFilePath;

    @Resource
    FileUploadMapper fileUploadMapper;

    @Resource
    private FileDfsUtil fileDfsUtil ;

    @Override
    public ResponseData<String> uploadFile(FileInfo fileInfo) {
        String result ;
        try{
            String path = fileDfsUtil.upload(fileInfo.getFile()) ;
            if (!StringUtils.isEmpty(path)){
                result = path ;
                FileUpload fileUpload = new FileUpload();
                fileUpload.setUuid(fileInfo.getUuid());
                fileUpload.setConvertType(fileInfo.getConvertType());
                fileUpload.setFileName(fileInfo.getFile().getOriginalFilename());
                fileUpload.setFilePath(result);
                fileUpload.setFileSize(fileInfo.getFile().getSize()+"");
                fileUpload.setCreateTime(new Date());
                fileUpload.setUpdateTime(new Date());
                fileUploadMapper.insert(fileUpload);
                return ResponseData.success(result);
            } else {
                result = "上传失败" ;
                return ResponseData.fail(result);
            }
        } catch (Exception e){
            e.printStackTrace() ;
            result = "服务异常" ;
            return ResponseData.fail(result);
        }
    }
}
