package com.gsd.convertserver.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class ProcessUtil {
    public static String runProcess(String input, String[] gsArgs) {
        long startTimeMillis = System.currentTimeMillis();
        if(input == null) {
            log.error("待转换文件为空...");
            return "转换失败";
        }
        try {
            Process process = new ProcessBuilder(gsArgs).redirectErrorStream(true).start();
            while(process.isAlive()) {
                List<String> strings = IOUtils.readLines(process.getInputStream());
                strings.forEach(line -> log.info(line));
            }
            int exitValue;
            if((exitValue = process.waitFor()) != 0) {
                log.error("转换失败，错误码为：{}", exitValue);
                return "";
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        long endTimeMillis = System.currentTimeMillis();
        log.info(String.format("转换用时： %s", (endTimeMillis - startTimeMillis)/1000));
        return (endTimeMillis - startTimeMillis)/1000 + "";
    }
}
