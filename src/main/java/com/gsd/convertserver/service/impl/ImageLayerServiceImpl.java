package com.gsd.convertserver.service.impl;

import com.gsd.convertserver.entity.FileUpload;
import com.gsd.convertserver.mapper.FileUploadMapper;
import com.gsd.convertserver.models.vo.ImageInfo;
import com.gsd.convertserver.service.ImageLayerService;
import com.gsd.convertserver.utils.baidu.Base64Util;
import com.gsd.convertserver.utils.baidu.FileUtil;
import com.gsd.convertserver.utils.baidu.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ImageLayerServiceImpl implements ImageLayerService{
    @Resource
    FileUploadMapper fileUploadMapper;
    @Value("${fastdfs.url}")
    private String fastdfsUrl;
    @Value("${baidu.ocr.url}")
    private String baiduOcrUrl;
    @Value("${convert.file.path}")
    private String convertFilePath;
    @Override
    public ImageInfo getImageUrl(String uuid) {
        ImageInfo imageInfo = new ImageInfo();
        FileUpload fileUpload = new FileUpload();
        fileUpload.setUuid(uuid);
        FileUpload selectOne = fileUploadMapper.selectOne(fileUpload);
        if(!ObjectUtils.isEmpty(selectOne)) {
            imageInfo.setId(selectOne.getId());
            imageInfo.setAttachPath(fastdfsUrl + selectOne.getFilePath());
        }
        return imageInfo;
    }

    @Override
    public String getOcrResult(String id) {
        FileUpload upload = fileUploadMapper.selectById(Integer.parseInt(id));
        if(!ObjectUtils.isEmpty(upload)) {
            String filePath = fastdfsUrl + upload.getFilePath();
            String fullPath = convertFilePath.concat("/").concat(upload.getUuid());
            InputStream inputStream = com.gsd.convertserver.utils.FileUtil.getFileStream(filePath);
            String writeFilePath = com.gsd.convertserver.utils.FileUtil.writeFile(inputStream, fullPath, upload.getFileName());
            try {
                byte[] imgData = FileUtil.readFileByBytes(writeFilePath);
                String imgStr = Base64Util.encode(imgData);
                String imgParam = URLEncoder.encode(imgStr, "UTF-8");
                String param = "image=" + imgParam + "&recognize_granularity=small";
                String accessToken = getAuth();
                if(!StringUtils.isBlank(accessToken)) {
                    String result = HttpUtil.post(baiduOcrUrl, accessToken, param);
                    com.gsd.convertserver.utils.FileUtil.deleteDir(fullPath);
                    return result;
                }else {
                    log.error("获取token为空");
                    return "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private String getAuth() {
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + "L03agaDXKbUFO3MrVkIGAWbW"
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + "mSLqEWVoIGr5DnVZnRHUx4GsyGthxtI0";
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            JSONObject jsonObject = new JSONObject(result);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        } catch (Exception e) {
            log.error("获取token失败！");
            e.printStackTrace();
        }
        return "";
    }
}
