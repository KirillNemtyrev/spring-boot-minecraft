package com.community.server.service;

import com.community.server.dto.ClientDto;
import com.community.server.utils.ReadFile;
import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class LauncherService {

    private static final Logger logger = LoggerFactory.getLogger(LauncherService.class);

    public ClientDto getLauncher(String launcher) {

        File file = new File("indexes/" + launcher + ".json");
        ReadFile readFile = new ReadFile(file);
        return new Gson().fromJson(readFile.get(), ClientDto.class);

    }

    @SneakyThrows
    public void getFile(String path, HttpServletResponse httpServletResponse){

        path = URLDecoder.decode(path, StandardCharsets.UTF_8);
        File file = new File(path);

        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        httpServletResponse.setContentType(mimetypesFileTypeMap.getContentType(file.getAbsoluteFile()));

        httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());
        httpServletResponse.setContentLength((int) file.length());

        BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream outStream = new BufferedOutputStream(httpServletResponse.getOutputStream());

        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        outStream.flush();
        inStream.close();
    }

}
