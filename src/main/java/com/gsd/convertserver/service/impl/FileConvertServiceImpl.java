package com.gsd.convertserver.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gsd.convertserver.entity.FileConvert;
import com.gsd.convertserver.entity.FileUpload;
import com.gsd.convertserver.mapper.FileConvertMapper;
import com.gsd.convertserver.mapper.FileUploadMapper;
import com.gsd.convertserver.models.qo.FileInfo;
import com.gsd.convertserver.service.FileConvertService;
import com.gsd.convertserver.utils.GhostUtils;
import com.gsd.convertserver.utils.OfficeUtils;
import com.gsd.convertserver.utils.TimeString;
import com.gsd.convertserver.utils.ZipUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FileConvertServiceImpl implements FileConvertService{
    @Resource
    private OfficeUtils  officeUtils;
    @Resource
    private GhostUtils ghostUtils;
    @Value("${convert.file.path}")
    private String convertFilePath;

    @Autowired
    FileUploadMapper fileUploadMapper;
    @Autowired
    FileConvertMapper fileConvertMapper;

    @Override
    public String convertFile(FileInfo fileInfo) {
        String fileType = "";
        String fileConvertCost = "0";
        String uuid = fileInfo.getUuid();
        FileUpload fileUpload = new FileUpload();
        fileUpload.setUuid(uuid);
        List<FileUpload> selectList = fileUploadMapper.selectList(new EntityWrapper<FileUpload>().eq("uuid", uuid));
        if(!CollectionUtils.isEmpty(selectList)) {
            FileUpload fileUpload1 = selectList.get(0);
            String fullPath = convertFilePath.concat("/").concat(uuid);
            String inputPath = fullPath.concat("/").concat(fileUpload1.getFileName());
            String fileName = new TimeString().getTimeString();
            log.info("文件名：" + fileName);
            String outputPath = fullPath.concat("/").concat(fileName).concat(".").concat("pdf");
            String convertType = fileUpload1.getConvertType();
            if(convertType.equals("pdfCompress")) {
                fileConvertCost = ghostUtils.fileConvert(inputPath, outputPath);
                fileType = "pdf";
            }
            if(convertType.equals("word2pdf")) {
                fileConvertCost = officeUtils.fileConvert(inputPath, outputPath);
                fileType = "pdf";
            }
            if(convertType.equals("txt2pdf")) {
                fileConvertCost = officeUtils.fileConvert(inputPath, outputPath);
                fileType = "pdf";
            }
            FileConvert fileConvert = new FileConvert();
            fileConvert.setConvertCost(fileConvertCost);
            fileConvert.setUuid(uuid);
            fileConvert.setFileName(fileName.concat(".").concat("pdf"));
            fileConvert.setFilePath(uuid.concat("/").concat(fileName).concat(".").concat(fileType));
            fileConvert.setCreateTime(new Date());
            fileConvert.setUpdateTime(new Date());
            fileConvertMapper.insert(fileConvert);
            return fileConvertCost;
        }else {
            return "";
        }
    }

    @Override
    public String pdfToImg(FileInfo fileInfo) {
        String uuid = fileInfo.getUuid();
        List<FileUpload> selectList = fileUploadMapper.selectList(new EntityWrapper<FileUpload>().eq("uuid", uuid));
        if(!CollectionUtils.isEmpty(selectList)) {
            FileUpload fileUpload1 = selectList.get(0);
            String fullPath = convertFilePath.concat("/").concat(uuid);
            String inputPath = fullPath.concat("/").concat(fileUpload1.getFileName());
            String fileName = new TimeString().getTimeString();
            String outputPath = fullPath.concat("/").concat(fileName);
            File file = new File(outputPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String[] srcFiles = new String[1];
            srcFiles[0] = outputPath;
            String zipFile = outputPath.concat(".zip");
            String fileConvertCost = ghostUtils.pdfToImg(inputPath, outputPath, fileInfo.getResolutionValue());
            try {
                ZipUtils.toZip2(srcFiles, zipFile, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            FileConvert fileConvert = new FileConvert();
            fileConvert.setConvertCost(fileConvertCost);
            fileConvert.setUuid(uuid);
            fileConvert.setFileName(fileName.concat(".").concat("zip"));
            fileConvert.setFilePath(uuid.concat("/").concat(fileName).concat(".").concat("zip"));
            fileConvert.setCreateTime(new Date());
            fileConvert.setUpdateTime(new Date());
            fileConvertMapper.insert(fileConvert);
            return fileConvertCost;
        }
        return null;
    }
}
