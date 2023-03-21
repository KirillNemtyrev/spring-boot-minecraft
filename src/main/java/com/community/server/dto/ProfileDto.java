package com.community.server.dto;

import com.community.server.dto.minecraft.TextureType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ProfileDto {
    private String login;
    private String name;
    private BigDecimal balance;
    private ServerDto[] servers;
    private Map<TextureType, String> textures;
}
