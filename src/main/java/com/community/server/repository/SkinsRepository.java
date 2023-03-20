package com.community.server.repository;

import com.community.server.entity.SkinEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface SkinsRepository extends JpaRepository<SkinEntity, Long> {
    Optional<SkinEntity> findByHash(String hash);
    //List<SkinEntity> findByHash(String hash);
}
