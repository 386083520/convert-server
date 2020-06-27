package com.gsd.convertserver.service.impl;

import com.gsd.convertserver.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService{
    @Value("${convert.file.path}")
    private String convertFilePath;

    @Override
    public String uploadFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String fullPath = convertFilePath.concat("/").concat(uuid);
        try {
            InputStream inputStream = file.getInputStream();
            writeFile(inputStream, fullPath, filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uuid + "/" + filename;
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
