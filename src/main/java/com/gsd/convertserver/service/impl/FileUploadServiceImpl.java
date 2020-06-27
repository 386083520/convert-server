package com.gsd.convertserver.service.impl;

import com.gsd.convertserver.entity.FileUpload;
import com.gsd.convertserver.mapper.FileUploadMapper;
import com.gsd.convertserver.models.qo.FileInfo;
import com.gsd.convertserver.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService{
    @Value("${convert.file.path}")
    private String convertFilePath;

    @Autowired
    FileUploadMapper fileUploadMapper;

    @Override
    public String uploadFile(FileInfo fileInfo) {
        FileUpload fileUpload = new FileUpload();
        fileUpload.setUuid(fileInfo.getUuid());
        fileUpload.setConvertType(fileInfo.getConvertType());
        fileUpload.setFileName(fileInfo.getFile().getOriginalFilename());
        fileUpload.setCreateTime(new Date());
        fileUpload.setUpdateTime(new Date());
        fileUploadMapper.insert(fileUpload);
        String filename = fileInfo.getFile().getOriginalFilename();
        String fullPath = convertFilePath.concat("/").concat(fileInfo.getUuid());
        try {
            InputStream inputStream = fileInfo.getFile().getInputStream();
            writeFile(inputStream, fullPath, filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void writeFile(InputStream inputStream, String fullPath, String fileName) {
        File folder = new File(fullPath);
        if(!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(fullPath.concat("/").concat(fileName));
        FileOutputStream fileOutputStream = null;
        InputStream in = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            in = inputStream;
            byte[] temp = new byte[1024];
            int len = 0;
            while ((len = in.read(temp)) != -1) {
                fileOutputStream.write(temp, 0, len);
            }
        } catch (Exception e) {
            log.error("保存文件失败：", e.toString());
            e.printStackTrace();
        }finally {
            if(fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
