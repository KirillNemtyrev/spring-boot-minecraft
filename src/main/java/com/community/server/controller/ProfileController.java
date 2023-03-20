package com.community.server.controller;

import com.community.server.dto.ProfileDto;
import com.community.server.dto.minecraft.TextureType;
import com.community.server.service.ProfileService;
import com.community.server.service.SkinService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@RestController
@AllArgsConstructor
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final SkinService skinService;

    @GetMapping
    public ProfileDto getProfile(HttpServletRequest httpServletRequest){
        return profileService.getProfile(httpServletRequest);
    }

    @PostMapping("/texture/{type}")
    public void loadTextureImage(@RequestParam MultipartFile file, @PathVariable @NotNull TextureType type, HttpServletRequest httpServletRequest){
        skinService.loadImage(file, type, httpServletRequest);
    }
}
