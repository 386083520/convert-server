package com.gsd.convertserver.service;

import com.gsd.convertserver.models.qo.FileInfo;

import javax.servlet.http.HttpServletResponse;

public interface FileDownloadService {
    void downloadFile(HttpServletResponse response, String uuid);
}
