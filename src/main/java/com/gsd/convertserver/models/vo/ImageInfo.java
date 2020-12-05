package com.gsd.convertserver.models.vo;

import lombok.Data;

@Data
public class ImageInfo {
    private Integer attachWidth = 0;
    private Integer attachHeight = 0;
    private String attachPath = "";
    private Integer height = 0;
    private Integer id;
}
