package com.gsd.convertserver.utils;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.gsd.convertserver.entity.FileUpload;
import com.gsd.convertserver.mapper.FileUploadMapper;
import com.gsd.convertserver.models.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Logger;

@Slf4j
@Component
public class MergeFileUtil {
    @Resource
    private FastFileStorageClient storageClient;
    @Resource
    FileUploadMapper fileUploadMapper;

    public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public Object[] mergeFile(String srcFile, String name, int chunks, String uuid, String convertType){
        SequenceInputStream sis= null;
        FileOutputStream fos = null;
        InputStream is = null;
        Object[] sizeLengthTime = new Object[0];
        sizeLengthTime = new Object[4];
        File file = null;
        try {
            ArrayList<FileInputStream> al = new ArrayList<>();
            for (int i = 0; i < chunks; i++) {
                al.add(new FileInputStream(new File(srcFile, i+"")));
            }
            Enumeration<FileInputStream> en = Collections.enumeration(al);
            String[] splitName = name.split("\\.");
            String type = splitName[splitName.length-1];
            String tempFileName = System.currentTimeMillis() + "." + type;
            file = new File(srcFile + tempFileName);
            sis = new SequenceInputStream(en);
            fos = new FileOutputStream(file);
            int len = 0;
            byte buf[] = new byte[1024];
            while ((len = sis.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            is = new FileInputStream(file);
            log.info("fileName:",file.getName());
            dealFile(is, file.length(), tempFileName, uuid, convertType);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(sis != null) {
                try {
                    sis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if(file!=null) {
            sizeLengthTime[0] = "";
            sizeLengthTime[1] = "";
            sizeLengthTime[2] = sdf.format(new Date());
            sizeLengthTime[3] = "文件合并成功！";
        }
        return sizeLengthTime;
    }

    public void dealFile(InputStream is, long length, String name, String uuid, String convertType) {
        log.info("length: {}, name: {}", length, name);
        StorePath storePath = storageClient.uploadFile(is, length, name, null);
        log.info("storePath.getFullPath:", storePath.getFullPath());
        if (!StringUtils.isEmpty(storePath.getFullPath())){
            FileUpload fileUpload = new FileUpload();
            fileUpload.setUuid(uuid);
            fileUpload.setConvertType(convertType);
            fileUpload.setFileName(name);
            fileUpload.setFilePath(storePath.getFullPath());
            fileUpload.setFileSize(length+"");
            fileUpload.setCreateTime(new Date());
            fileUpload.setUpdateTime(new Date());
            fileUploadMapper.insert(fileUpload);
        }
    }
}
