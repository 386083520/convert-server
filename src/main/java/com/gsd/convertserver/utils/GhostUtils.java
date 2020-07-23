package com.gsd.convertserver.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class GhostUtils {
    @Value("${gs.path}")
    private String gsPath;
    public String fileConvert(String input, String output) {
        String[] gsArgs = {gsPath, "-sDEVICE=pdfwrite", "-dPDFSETTINGS=/ebook", "-dNOPAUSE",
                "-dBATCH", "-dQUIET", "-sOutputFile="+output, input};
        return convert(input, output, gsArgs);
    }

    public String pdfToImg(String input, String output, String resolutionValue) {
        String[] gsArgs = {gsPath, "-sDEVICE=jpeg", "-r" + resolutionValue,"-dNOPAUSE", "-dBATCH", "-dQUIET", "-sOutputFile=" + output + "/%d.jpg" , input};
        return convert(input, output, gsArgs);
    }

    public String convert(String input, String output, String[] gsArgs) {
        return ProcessUtil.runProcess(input, gsArgs);
    }
}
