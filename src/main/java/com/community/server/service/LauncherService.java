package com.community.server.service;

import com.community.server.dto.ClientDto;
import com.community.server.dto.SettingsDto;
import com.community.server.entity.UserEntity;
import com.community.server.repository.UserRepository;
import com.community.server.security.JwtAuthenticationFilter;
import com.community.server.security.JwtTokenProvider;
import com.community.server.utils.ReadFile;
import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;

@Service
public class LauncherService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    public ClientDto getLauncher(String launcher) {

        File file = new File("launcher/indexes/" + launcher + ".json");
        ReadFile readFile = new ReadFile(file);
        return new Gson().fromJson(readFile.get(), ClientDto.class);

    }

    public SettingsDto getLauncherSettings(String launcher, HttpServletRequest httpServletRequest) {

        Long user = jwtTokenProvider.getUserIdFromJWT(jwtAuthenticationFilter.getJwtFromRequest(httpServletRequest));

        UserEntity userEntity = userRepository.findById(user).orElseThrow(
                () -> new UsernameNotFoundException("User is not found!"));

        File file = new File("launcher/settings/" + launcher + ".json");
        ReadFile readFile = new ReadFile(file);

        SettingsDto settings = new Gson().fromJson(readFile.get(), SettingsDto.class);
        settings.setUsername(userEntity.getUsername());
        settings.setUuid(userEntity.getUuid().replace("-", ""));

        return settings;

    }

    @SneakyThrows
    public void getFile(String path, HttpServletResponse httpServletResponse){

        path = URLDecoder.decode(path);
        File file = new File("./launcher/" + path);

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
