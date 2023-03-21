package com.community.server.service;

import com.community.server.dto.ProfileDto;
import com.community.server.dto.minecraft.Texture;
import com.community.server.dto.minecraft.TextureType;
import com.community.server.entity.SkinEntity;
import com.community.server.entity.UserEntity;
import com.community.server.repository.SkinRepository;
import com.community.server.repository.UserRepository;
import com.community.server.security.JwtAuthenticationFilter;
import com.community.server.security.JwtTokenProvider;
import com.community.server.utils.MD5Files;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.awt.Image.SCALE_DEFAULT;

@Service
@AllArgsConstructor
public class ProfileService {

    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);

    private final UserRepository userRepository;
    private final SkinService skinService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public ProfileDto getProfile(HttpServletRequest httpServletRequest){
        Long user = jwtTokenProvider.getUserIdFromJWT(jwtAuthenticationFilter.getJwtFromRequest(httpServletRequest));

        UserEntity userEntity = userRepository.findById(user).orElseThrow(
                () -> new UsernameNotFoundException("User is not found!"));

        Map<TextureType, Texture> skins = skinService.getSkinByUsername(userEntity.getUsername());
        Map<TextureType, String> map = new HashMap<>();

        for (Map.Entry<TextureType, Texture> entry : skins.entrySet()){
            map.put(entry.getKey(), entry.getValue().hash);
        }
        return new ProfileDto(userEntity.getUsername(), userEntity.getName(), userEntity.getBalance(), new MD5Files().getServers(), map);
    }
}
