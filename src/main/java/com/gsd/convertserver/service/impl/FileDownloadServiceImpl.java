package com.gsd.convertserver.service.impl;

import com.gsd.convertserver.models.qo.FileInfo;
import com.gsd.convertserver.service.FileDownloadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Service
@Slf4j
public class FileDownloadServiceImpl implements FileDownloadService{
    @Value("${convert.file.path}")
    private String convertFilePath;
    @Override
    public void downloadFile(HttpServletResponse response, String filePath) {
        if(filePath!=null) {
            String fileName = filePath.split("/")[1];
            String fullPath = convertFilePath.concat("/").concat(filePath);
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
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
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
