package com.gsd.convertserver.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@Slf4j
public class FileUtil {
    public static String writeFile(InputStream inputStream, String fullPath, String fileName) {
        File folder = new File(fullPath);
        if(!folder.exists()) {
            folder.mkdirs();
        }
        String filePosition = fullPath.concat("/").concat(fileName);
        File file = new File(filePosition);
        FileOutputStream fileOutputStream = null;
        InputStream in = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            in = inputStream;
            byte[] temp = new byte[1024];
            int len = 0;
            while ((len = in.read(temp)) != -1) {
                fileOutputStream.write(temp, 0, len);
            }
        } catch (Exception e) {
            log.error("保存文件失败：", e.toString());
            e.printStackTrace();
        }finally {
            if(fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return filePosition;
    }

    public static InputStream getFileStream(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                return inputStream;
            }
        } catch (IOException e) {
            System.out.println("获取网络文件出现异常，文件路径为：" + url);
            e.printStackTrace();
        }
        return null;
    }

    public static MultipartFile getMulFileByPath(String picPath) {
        try {
            File file = new File(picPath);
            FileInputStream input = null;
            input = new FileInputStream(file);
            MultipartFile multipartFile =new MockMultipartFile("file", file.getName(), "text/plain", IOUtils.toByteArray(input));
            return multipartFile;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("file转MultipartFile失败");
            return null;
        }
    }

    public static void deleteDir(String dirPath)
    {
        File file = new File(dirPath);
        if(file.isFile())
        {
            file.delete();
        }else
        {
            File[] files = file.listFiles();
            if(files == null)
            {
                file.delete();
            }else
            {
                for (int i = 0; i < files.length; i++)
                {
                    deleteDir(files[i].getAbsolutePath());
                }
                file.delete();
            }
        }
    }
}
