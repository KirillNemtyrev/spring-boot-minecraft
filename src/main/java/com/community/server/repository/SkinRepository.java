package com.community.server.repository;

import com.community.server.dto.minecraft.TextureType;
import com.community.server.entity.SkinEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkinRepository extends JpaRepository<SkinEntity, Long> {
    List<SkinEntity> findByUserId(Long userId);
    Optional<SkinEntity> findByHash(String hash);

    Optional<SkinEntity> findByUserIdAndTextureType(Long userId, TextureType textureType);

    @Query("select s from SkinEntity s where s.id = 1")
    SkinEntity findDefaultSkin();
}
