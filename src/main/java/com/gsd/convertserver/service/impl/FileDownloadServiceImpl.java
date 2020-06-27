package com.gsd.convertserver.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gsd.convertserver.entity.FileConvert;
import com.gsd.convertserver.entity.FileUpload;
import com.gsd.convertserver.mapper.FileConvertMapper;
import com.gsd.convertserver.models.qo.FileInfo;
import com.gsd.convertserver.service.FileDownloadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@Service
@Slf4j
public class FileDownloadServiceImpl implements FileDownloadService{
    @Value("${convert.file.path}")
    private String convertFilePath;
    @Autowired
    FileConvertMapper fileConvertMapper;
    @Override
    public void downloadFile(HttpServletResponse response, String uuid) {
        if(uuid!=null) {
            FileConvert fileConvert = new FileConvert();
            fileConvert.setUuid(uuid);
            List<FileConvert> selectList = fileConvertMapper.selectList(new EntityWrapper<FileConvert>().eq("uuid", uuid));
            if(!CollectionUtils.isEmpty(selectList)) {
                String fullPath = convertFilePath.concat("/").concat(selectList.get(0).getFilePath());
                File file = new File(fullPath);
                long length = file.length();
                if(length <= Integer.MAX_VALUE) {
                    response.setContentLength((int) length);
                }else {
                    response.addHeader("Content-Length", Long.toString(length));
                }
                log.info("内容长度:{}", response.getHeader("Content-Length"));
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Disposition", "attachment;filename=\"" + selectList.get(0).getFileName() + "\"");
                byte[] buffer = new byte[1024*100];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    int sizeRead = 0;
                    while (i != -1) {
                        sizeRead += i;
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                        if((sizeRead % (1024 * 100 *1000)) == 0) {
                            log.info("已经传输了:{}", sizeRead);
                        }
                    }
                    log.info("传输完成:{}", sizeRead);
                } catch (Exception e) {
                    log.error("下载失败：", e.getMessage());
                    e.printStackTrace();
                }finally {
                    if(bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
