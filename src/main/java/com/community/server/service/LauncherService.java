package com.community.server.service;

import com.community.server.dto.ClientDto;
import com.community.server.dto.ServerDto;
import com.community.server.entity.UserEntity;
import com.community.server.repository.UserRepository;
import com.community.server.security.JwtAuthenticationFilter;
import com.community.server.security.JwtTokenProvider;
import com.community.server.utils.ReadFile;
import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.ArrayList;

@Service
public class LauncherService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public LauncherService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    public ClientDto getLauncher(String launcher) {

        File file = new File("launcher/indexes/" + launcher + ".json");
        ReadFile readFile = new ReadFile(file);
        return new Gson().fromJson(readFile.get(), ClientDto.class);

    }

    public ArrayList<String> getLauncherSettings(String launcher, boolean connect, boolean fullscreen, HttpServletRequest httpServletRequest) {

        String token = jwtAuthenticationFilter.getJwtFromRequest(httpServletRequest);
        Long user = jwtTokenProvider.getUserIdFromJWT(token);

        UserEntity userEntity = userRepository.findById(user).orElseThrow(
                () -> new UsernameNotFoundException("User is not found!"));

        File file = new File("servers.json");
        ReadFile readFile = new ReadFile(file);
        ServerDto[] servers = new Gson().fromJson(readFile.get(), new TypeToken<ServerDto[]>() {}.getType());

        for (ServerDto serverDto : servers){

            if (serverDto.getClient().equals(launcher)){

                ArrayList<String> params = serverDto.getParams();
                params.add("--username");
                params.add(userEntity.getUsername());
                params.add("--uuid");
                params.add(userEntity.getUuid().replace("-", ""));
                params.add("--accessToken");
                params.add(token);

                if (connect) {
                    params.add("--server");
                    params.add(serverDto.getIp());
                    params.add("--port");
                    params.add(String.valueOf(serverDto.getPort()));
                }

                if (fullscreen) {
                    params.add("--fullscreen");
                    params.add("true");
                }

                return params;

            }

        }
        return null;
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
