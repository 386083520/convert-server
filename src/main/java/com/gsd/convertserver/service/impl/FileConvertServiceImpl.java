package com.gsd.convertserver.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gsd.convertserver.entity.FileConvert;
import com.gsd.convertserver.entity.FileUpload;
import com.gsd.convertserver.mapper.FileConvertMapper;
import com.gsd.convertserver.mapper.FileUploadMapper;
import com.gsd.convertserver.models.qo.FileInfo;
import com.gsd.convertserver.service.FileConvertService;
import com.gsd.convertserver.utils.OfficeUtils;
import com.gsd.convertserver.utils.TimeString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FileConvertServiceImpl implements FileConvertService{
    @Resource
    private OfficeUtils  officeUtils;
    @Value("${convert.file.path}")
    private String convertFilePath;

    @Autowired
    FileUploadMapper fileUploadMapper;
    @Autowired
    FileConvertMapper fileConvertMapper;

    @Override
    public String convertFile(FileInfo fileInfo) {
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
            String fileConvertCost = officeUtils.fileConvert(inputPath, outputPath);
            FileConvert fileConvert = new FileConvert();
            fileConvert.setConvertCost(fileConvertCost);
            fileConvert.setUuid(uuid);
            fileConvert.setFileName(fileName.concat(".").concat("pdf"));
            fileConvert.setFilePath(uuid.concat("/").concat(fileName).concat(".").concat("pdf"));
            fileConvert.setCreateTime(new Date());
            fileConvert.setUpdateTime(new Date());
            fileConvertMapper.insert(fileConvert);
            return fileConvertCost;
        }else {
            return "";
        }
    }
}
