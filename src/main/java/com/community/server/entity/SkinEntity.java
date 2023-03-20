package com.community.server.entity;

import com.community.server.dto.minecraft.Texture;
import com.community.server.dto.minecraft.TextureType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Lazy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "skins")
public class SkinEntity {
    private static final String BASIC_URL = "localhost:8745/image/";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private TextureType textureType;
    private Long userId;
    private String hash;
    @Lazy
    private byte[] data;

    public Texture toTexture(){
        return new Texture(
                hash,
                data,
                BASIC_URL + hash
        );
    }
}
