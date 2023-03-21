package com.community.server.controller;

import com.community.server.dto.ProfileDto;
import com.community.server.dto.TextureDto;
import com.community.server.dto.minecraft.TextureType;
import com.community.server.entity.SkinEntity;
import com.community.server.service.ProfileService;
import com.community.server.service.SkinService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public TextureDto loadTextureImage(@RequestParam MultipartFile file, @PathVariable @NotNull TextureType type, HttpServletRequest httpServletRequest){
        return skinService.loadImage(file, type, httpServletRequest);
    }

    @DeleteMapping("/cape")
    public ResponseEntity<?> deleteCape(HttpServletRequest httpServletRequest){
        return skinService.deleteCape(httpServletRequest);
    }
}
