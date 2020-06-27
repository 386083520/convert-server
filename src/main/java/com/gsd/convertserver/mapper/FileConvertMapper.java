package com.gsd.convertserver.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gsd.convertserver.entity.FileConvert;
import com.gsd.convertserver.entity.FileUpload;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileConvertMapper extends BaseMapper<FileConvert> {
}
