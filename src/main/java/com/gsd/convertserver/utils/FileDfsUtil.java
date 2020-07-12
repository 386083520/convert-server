package com.gsd.convertserver.utils;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Component
@Slf4j
public class FileDfsUtil {
    @Resource
    private FastFileStorageClient storageClient;

    public String upload(MultipartFile multipartFile) throws Exception{
        String originalFilename = multipartFile.getOriginalFilename().
                substring(multipartFile.getOriginalFilename().
                        lastIndexOf(".") + 1);
        StorePath storePath = storageClient.uploadFile(multipartFile.getInputStream(), multipartFile.getSize(), originalFilename, null);
        return storePath.getFullPath();
    }

    public void deleteFile(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            log.error("文件路径为空");
        }
        StorePath storePath = StorePath.parseFromUrl(fileUrl);
        storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
    }
}
