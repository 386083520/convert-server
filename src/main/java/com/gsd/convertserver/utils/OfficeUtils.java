package com.gsd.convertserver.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.jodconverter.DocumentConverter;
import org.jodconverter.office.OfficeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.util.List;

@Slf4j
@Component
public class OfficeUtils {
    @Autowired
    private DocumentConverter documentConverter;

    @Value("${python.path}")
    private String pythonPath;

    @Value("${python.file.path.pdf2word}")
    private String pathPdf2word;

    @Value("${python.file.path.img2txt}")
    private String pathImg2txt;

    public String fileConvert(String input, String output) {
        if(input == null) {
            log.error("待转换文件为空...");
            return "转换失败";
        }
        long startTimeMillis = System.currentTimeMillis();
        File sourceFile = new File(input);
        File targetFile = new File(output);
        try {
            documentConverter.convert(sourceFile).to(targetFile).execute();
        } catch (OfficeException e) {
            log.error("转换失败：", e.getMessage());
            e.printStackTrace();
        }
        long endTimeMillis = System.currentTimeMillis();
        log.info(String.format("转换用时： %s", (endTimeMillis - startTimeMillis)/1000));
        return (endTimeMillis - startTimeMillis)/1000 + "";
    }

    public String pdf2word(String input, String output) {
        String[] gsArgs = {pythonPath, pathPdf2word,input,output};
        return ProcessUtil.runProcess(input, gsArgs);
    }
    public String img2txt(String input, String output) {
        String[] gsArgs = {pythonPath, pathImg2txt,input,output};
        return ProcessUtil.runProcess(input, gsArgs);
    }
}
