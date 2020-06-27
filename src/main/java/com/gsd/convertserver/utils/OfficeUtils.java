package com.gsd.convertserver.utils;

import lombok.extern.slf4j.Slf4j;
import org.jodconverter.DocumentConverter;
import org.jodconverter.office.OfficeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class OfficeUtils {
    @Autowired
    private DocumentConverter documentConverter;

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
}
