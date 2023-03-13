package com.community.server.service;

import com.community.server.dto.ClientDto;
import com.community.server.utils.ReadFile;
import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Files;

@Service
public class LoaderService {

    public ClientDto getLoader() {
        File file = new File("loader.json");
        ReadFile readFile = new ReadFile(file);
        return new Gson().fromJson(readFile.get(), ClientDto.class);
    }


    @SneakyThrows
    public void getFile(String path, HttpServletResponse httpServletResponse){

        path = URLDecoder.decode(path);
        File file = new File("./" + path);

        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        httpServletResponse.setContentType(mimetypesFileTypeMap.getContentType(file.getAbsoluteFile()));

        httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());
        httpServletResponse.setContentLength((int) file.length());

        BufferedInputStream inStream = new BufferedInputStream(Files.newInputStream(file.toPath()));
        BufferedOutputStream outStream = new BufferedOutputStream(httpServletResponse.getOutputStream());

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        outStream.flush();
        inStream.close();
    }

}
