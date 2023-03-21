package com.community.server.service;

import com.community.server.dto.TextureDto;
import com.community.server.dto.minecraft.Texture;
import com.community.server.dto.minecraft.TextureType;
import com.community.server.entity.SkinEntity;
import com.community.server.entity.UserEntity;
import com.community.server.exception.ResourceNotFoundException;
import com.community.server.repository.SkinRepository;
import com.community.server.repository.UserRepository;
import com.community.server.security.JwtAuthenticationFilter;
import com.community.server.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SkinService {
    private final SkinRepository skinRepository;
    private final UserRepository userRepository;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private JwtTokenProvider jwtTokenProvider;

    public Map<TextureType, Texture> getSkinByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found!"));
        Map<TextureType, SkinEntity> texturesMap = skinRepository.findByUserId(user.getId()).stream().collect(Collectors.toMap(SkinEntity::getTextureType, it -> it));
        if (!texturesMap.containsKey(TextureType.SKIN)) {
            texturesMap.put(TextureType.SKIN, skinRepository.findDefaultSkin());
        }

        Map<TextureType, Texture> map = new HashMap<>();
        for (Map.Entry<TextureType, SkinEntity> entry : texturesMap.entrySet()){
            map.put(entry.getKey(), entry.getValue().toTexture());
        }
        return map;
    }

    public Optional<byte[]> getDataByHash(String hash) {
        return skinRepository.findByHash(hash).map(SkinEntity::getData);
    }

    @SneakyThrows
    public TextureDto loadImage(MultipartFile file, TextureType type, HttpServletRequest httpServletRequest) {
        Long userId = jwtTokenProvider.getUserIdFromJWT(jwtAuthenticationFilter.getJwtFromRequest(httpServletRequest));

        SkinEntity texture = skinRepository.findByUserIdAndTextureType(userId, type).orElse(
                SkinEntity.builder()
                        .userId(userId)
                        .textureType(type)
                        .hash(UUID.randomUUID().toString())
                        .build()
        );

        texture.setData(file.getBytes());
        skinRepository.save(texture);
        return new TextureDto(texture.getHash(), texture.getTextureType());
    }

    public ResponseEntity<?> deleteCape(HttpServletRequest httpServletRequest){
        Long userId = jwtTokenProvider.getUserIdFromJWT(jwtAuthenticationFilter.getJwtFromRequest(httpServletRequest));
        SkinEntity texture = skinRepository.findByUserIdAndTextureType(userId, TextureType.CAPE).orElseThrow(
                () -> new IllegalArgumentException("Cape is not found!")
        );

        skinRepository.delete(texture);
        return ResponseEntity.ok().build();
    }

}
