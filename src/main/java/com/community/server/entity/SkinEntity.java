package com.community.server.entity;

import com.community.server.dto.minecraft.TextureType;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "skins")
public class SkinEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private TextureType textureType;
    private String hash;
    private byte[] data;
    private String url;
}
