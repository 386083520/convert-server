package com.gsd.convertserver.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "propose_info")
public class ProposeInfo {
    @TableId(value = "id",type = IdType.AUTO)//指定自增策略
    private Integer id;
    @TableField(value = "propose_content")
    private String proposeContent;
    @TableField(value = "create_time")
    private Date createTime;
    @TableField(value = "update_time")
    private Date updateTime;
}
