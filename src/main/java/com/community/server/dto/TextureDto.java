package com.community.server.dto;

import com.community.server.dto.minecraft.TextureType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TextureDto {
    private String hash;
    private TextureType texture;
}
