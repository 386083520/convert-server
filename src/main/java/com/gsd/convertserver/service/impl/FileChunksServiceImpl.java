package com.gsd.convertserver.service.impl;

import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.gsd.convertserver.models.qo.FileInfo;
import com.gsd.convertserver.service.FileChunkService;
import com.gsd.convertserver.utils.CacheUtils;
import com.gsd.convertserver.utils.MergeFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class FileChunksServiceImpl implements FileChunkService{
    @Value("${convert.file.path}")
    private String convertFilePath;

    @Resource
    private FastFileStorageClient storageClient;

    @Resource
    private MergeFileUtil mergeFileUtil;
    @Override
    public String chunkFile(MultipartFile file, String uuid, Integer chunk, Integer chunks) {
        if(chunk == null) {
            chunk = 0;
        }
        if(chunks == null) {
            chunks = 1;
        }
        String fileName = file.getOriginalFilename();
        String fileDir = "";
        String filePath = "";
        int index = fileName.lastIndexOf(".");
        if(index > 0) {
            fileDir = fileName.substring(0,index);
        } else {
            fileDir = fileName;
        }
        filePath = convertFilePath.concat("/").concat(uuid).concat("/").concat(fileDir).concat("/")+chunk;
        File fileTemp = new File(filePath);
        File fileParent = fileTemp.getParentFile();
        if(!fileParent.exists()) {
            fileParent.mkdirs();
        }
        try {
            file.transferTo(fileTemp);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(CacheUtils.getMap().get("name_" +uuid + fileDir) == null) {
            CacheUtils.setValues("chunks_"+ uuid + fileDir, chunks+"");
            CacheUtils.setValues("name_"+uuid+fileDir, fileName);
            log.info("chunks_"+ uuid + fileDir+ ":" + CacheUtils.getMap().get("chunks_"+ uuid + fileDir));
            log.info("name_"+ uuid + fileDir+ ":" + CacheUtils.getMap().get("name_"+ uuid + fileDir));
        }
        log.info("文件[{}]分片[{}]保存成功！",fileName,chunk);
        return "SUCCESS";
    }

    @Override
    public Object[] mergeFile(FileInfo fileInfo) {
        Object[] sizeLengthTimeName = new Object[4];
        String fileDir = "";
        String fileName = fileInfo.getFileName();
        if(fileName != null) {
            int index = fileName.lastIndexOf(".");
            if(index > 0) {
                fileDir = fileName.substring(0,index);
            } else {
                fileDir = fileName;
            }
        }
        String filePath = "";
        Object[] sizeLengthTime = null;
        String uuid = fileInfo.getUuid();
        String convertType = fileInfo.getConvertType();
        log.info("merge:chunks_"+ uuid + fileDir+ ":" + CacheUtils.getMap().get("chunks_"+ uuid + fileDir));
        log.info("merge:name_"+ uuid + fileDir+ ":" + CacheUtils.getMap().get("name_"+ uuid + fileDir));
        String name = CacheUtils.getMap().get("name_" + uuid + fileDir);
        int chunks = Integer.parseInt(CacheUtils.getMap().get("chunks_"+uuid+fileDir));
        filePath = convertFilePath.concat("/").concat(uuid).concat("/").concat(fileDir).concat("/");
        sizeLengthTime = mergeFileUtil.mergeFile(filePath, name, chunks, uuid, convertType);
        sizeLengthTimeName[0] = sizeLengthTime[0];
        sizeLengthTimeName[1] = sizeLengthTime[1];
        sizeLengthTimeName[2] = sizeLengthTime[2];
        sizeLengthTimeName[3] = name;
        return sizeLengthTimeName;
    }
}
