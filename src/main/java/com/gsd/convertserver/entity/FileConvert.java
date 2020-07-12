package com.gsd.convertserver.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "file_convert")
public class FileConvert {
    @TableId(value = "id",type = IdType.AUTO)//指定自增策略
    private Integer id;
    @TableField(value = "uuid")
    private String uuid;
    @TableField(value = "convert_cost")
    private String convertCost;
    @TableField(value = "file_path")
    private String filePath;
    @TableField(value = "file_name")
    private String fileName;
    @TableField(value = "file_size")
    private String fileSize;
    @TableField(value = "create_time")
    private Date createTime;
    @TableField(value = "update_time")
    private Date updateTime;
}
