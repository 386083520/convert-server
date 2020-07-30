package com.gsd.convertserver.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gsd.convertserver.entity.FileConvert;
import com.gsd.convertserver.entity.FileUpload;
import com.gsd.convertserver.entity.ProposeInfo;
import com.gsd.convertserver.mapper.FileConvertMapper;
import com.gsd.convertserver.mapper.FileUploadMapper;
import com.gsd.convertserver.mapper.ProposeInfoMapper;
import com.gsd.convertserver.models.qo.FileInfo;
import com.gsd.convertserver.models.qo.ProposeInfoQo;
import com.gsd.convertserver.service.FileConvertService;
import com.gsd.convertserver.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class FileConvertServiceImpl implements FileConvertService{
    @Resource
    private OfficeUtils  officeUtils;
    @Resource
    private GhostUtils ghostUtils;
    @Value("${convert.file.path}")
    private String convertFilePath;
    @Value("${fastdfs.url}")
    private String fastdfsUrl;


    @Resource
    FileUploadMapper fileUploadMapper;
    @Resource
    FileConvertMapper fileConvertMapper;
    @Resource
    ProposeInfoMapper proposeInfoMapper;

    @Resource
    private FileDfsUtil fileDfsUtil ;

    @Override
    public String convertFile(FileInfo fileInfo) {
        String fileConvertCost = "0";
        String uuid = fileInfo.getUuid();
        List<FileUpload> selectList = fileUploadMapper.selectList(new EntityWrapper<FileUpload>().eq("uuid", uuid));
        if(!CollectionUtils.isEmpty(selectList)) {
            FileUpload fileUpload = selectList.get(selectList.size() - 1);
            String fullPath = convertFilePath.concat("/").concat(uuid);
            String inputPath = fastdfsUrl+fileUpload.getFilePath();
            InputStream inputStream = FileUtil.getFileStream(inputPath);
            String writeFilePath = FileUtil.writeFile(inputStream, fullPath, fileUpload.getFileName());
            log.info("inputPath, {}", inputPath);
            String fileName = new TimeString().getTimeString();
            log.info("文件名：" + fileName);
            String outputPath = fullPath.concat("/").concat(fileName).concat(".").concat(fileInfo.getFileType());
            String convertType = fileUpload.getConvertType();
            if(convertType.equals("pdfCompress")) {
                fileConvertCost = ghostUtils.fileConvert(writeFilePath, outputPath);
            }
            if(convertType.equals("word2pdf")) {
                fileConvertCost = officeUtils.fileConvert(writeFilePath, outputPath);
            }
            if(convertType.equals("txt2pdf")) {
                fileConvertCost = officeUtils.fileConvert(writeFilePath, outputPath);
            }
            if(convertType.equals("excel2pdf")) {
                fileConvertCost = officeUtils.fileConvert(writeFilePath, outputPath);
            }
            if(convertType.equals("word2html")) {
                fileConvertCost = officeUtils.fileConvert(writeFilePath, outputPath);
            }
            if(convertType.equals("txt2html")) {
                fileConvertCost = officeUtils.fileConvert(writeFilePath, outputPath);
            }
            if(convertType.equals("excel2html")) {
                fileConvertCost = officeUtils.fileConvert(writeFilePath, outputPath);
            }
            if(convertType.equals("html2word")) {
                fileConvertCost = officeUtils.fileConvert(writeFilePath, outputPath);
            }
            if(convertType.equals("html2pdf")) {
                fileConvertCost = officeUtils.fileConvert(writeFilePath, outputPath);
            }
            if(convertType.equals("pdf2word")) {
                fileConvertCost = officeUtils.pdf2word(fullPath, outputPath);
            }
            if(convertType.equals("img2txt")) {
                fileConvertCost = officeUtils.img2txt(writeFilePath, outputPath);
            }
            if(convertType.equals("removePage")) {
                fileConvertCost = ghostUtils.fileConvert(writeFilePath, outputPath);
            }
            try {
                MultipartFile mulFileByPath = FileUtil.getMulFileByPath(outputPath);
                String path = fileDfsUtil.upload(mulFileByPath) ;
                convertInsert(fileConvertCost, uuid, fileName.concat(".").concat(fileInfo.getFileType()), path, mulFileByPath.getSize()+"");
                FileUtil.deleteDir(fullPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
            FileUpload fileUpload = selectList.get(selectList.size() - 1);
            String fullPath = convertFilePath.concat("/").concat(uuid);
            String inputPath = fastdfsUrl+fileUpload.getFilePath();
            String fileName = new TimeString().getTimeString();
            String outputPath = fullPath.concat("/").concat(fileName);
            File file = new File(outputPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String[] srcFiles = new String[1];
            srcFiles[0] = outputPath;
            String zipFile = outputPath.concat(".zip");
            InputStream inputStream = FileUtil.getFileStream(inputPath);
            String writeFilePath = FileUtil.writeFile(inputStream, fullPath, fileUpload.getFileName());
            log.info("inputPath, {}", inputPath);
            String fileConvertCost = ghostUtils.pdfToImg(writeFilePath, outputPath, fileInfo.getResolutionValue());
            try {
                ZipUtils.toZip2(srcFiles, zipFile, false);
                MultipartFile mulFileByPath = FileUtil.getMulFileByPath(zipFile);
                String path = fileDfsUtil.upload(mulFileByPath) ;
                convertInsert(fileConvertCost, uuid, fileName.concat(".").concat("zip"), path, mulFileByPath.getSize()+"");
                FileUtil.deleteDir(fullPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return fileConvertCost;
        }
        return null;
    }

    // 將转换数据插入数据库
    public void convertInsert(String fileConvertCost, String uuid, String fileName, String filePath, String fileSize) {
        FileConvert fileConvert = new FileConvert();
        fileConvert.setConvertCost(fileConvertCost);
        fileConvert.setUuid(uuid);
        fileConvert.setFileName(fileName);
        fileConvert.setFilePath(filePath);
        fileConvert.setFileSize(fileSize);
        fileConvert.setCreateTime(new Date());
        fileConvert.setUpdateTime(new Date());
        fileConvertMapper.insert(fileConvert);
    }

    public String proposeInsert(ProposeInfoQo proposeInfoQo) {
        ProposeInfo proposeInfo = new ProposeInfo();
        proposeInfo.setProposeContent(proposeInfoQo.getTextInfo());
        proposeInfo.setCreateTime(new Date());
        proposeInfo.setUpdateTime(new Date());
        proposeInfoMapper.insert(proposeInfo);
        return "";
    }
}
