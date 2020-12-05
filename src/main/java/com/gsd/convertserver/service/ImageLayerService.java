package com.gsd.convertserver.service;

import com.gsd.convertserver.models.vo.ImageInfo;

public interface ImageLayerService {
    ImageInfo getImageUrl(String uuid);
    String getOcrResult(String id);
}
