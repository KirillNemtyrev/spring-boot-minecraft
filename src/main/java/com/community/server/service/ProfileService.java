package com.community.server.service;

import com.community.server.controller.dto.ProfileDto;
import com.community.server.entity.UserEntity;
import com.community.server.repository.UserRepository;
import com.community.server.security.JwtAuthenticationFilter;
import com.community.server.security.JwtTokenProvider;
import com.community.server.utils.MD5Files;
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

import static java.awt.Image.SCALE_DEFAULT;

@Service
public class ProfileService {

    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    public ProfileDto getProfile(HttpServletRequest httpServletRequest){
        Long user = jwtTokenProvider.getUserIdFromJWT(jwtAuthenticationFilter.getJwtFromRequest(httpServletRequest));

        UserEntity userEntity = userRepository.findById(user).orElseThrow(
                () -> new UsernameNotFoundException("User is not found!"));

        MD5Files md5Files = new MD5Files();
        return new ProfileDto(userEntity.getUsername(), userEntity.getName(), userEntity.getBalance(), md5Files.getServers());
    }

    @SneakyThrows
    public Object updateSkin(HttpServletRequest httpServletRequest, MultipartFile multipartFile) {

        Long user = jwtTokenProvider.getUserIdFromJWT(jwtAuthenticationFilter.getJwtFromRequest(httpServletRequest));

        UserEntity userEntity = userRepository.findById(user).orElseThrow(
                () -> new UsernameNotFoundException("User is not found!"));

        if(multipartFile.isEmpty()){
            return new ResponseEntity<>("File is empty!", HttpStatus.BAD_REQUEST);
        }

        String suffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1);
        if(suffix.equalsIgnoreCase("png")) {
            return new ResponseEntity<>("The file is not a skin!", HttpStatus.BAD_REQUEST);
        }

        File skins = new File("resources/skins");
        if (skins.mkdir()) logger.info("Folder with skins created!");

        File photos = new File("resources/photo");
        if (photos.mkdir()) logger.info("Folder with photos created!");

        BufferedImage image = ImageIO.read((File) multipartFile);
        BufferedImage bufferedImage = image.getSubimage(8, 8, 8, 8);

        BufferedImage resizedImage = new BufferedImage(32, 32, SCALE_DEFAULT);
        Graphics2D graphics = resizedImage.createGraphics();
        graphics.drawImage(bufferedImage, 0, 0, 32, 32, null);

        File photo = new File("resources/photo/" + userEntity.getUsername() + ".png");
        ImageIO.write(resizedImage, "png", photo);

        File skin = new File("resources/skins/" + userEntity.getUsername() + ".png");
        if (skin.createNewFile()) logger.info("File with skin " + userEntity.getUsername() + " created!");
        ImageIO.write(image, "png", skin);

        return new ResponseEntity<>("Your skin updated!", HttpStatus.OK);
    }

}
