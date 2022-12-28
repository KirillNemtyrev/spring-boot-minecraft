package com.community.server.controller;

import com.community.server.controller.dto.minecraft.JoinServerRequest;
import com.community.server.controller.dto.minecraft.MinecraftServerMeta;
import com.community.server.controller.dto.minecraft.MinecraftUser;
import com.community.server.utils.KeyUtils;
import com.google.common.collect.Lists;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.community.server.utils.KeyUtils.getSignaturePublicKey;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.ok;

@RestController
public class MinecraftController {

    @GetMapping("/")
    public MinecraftServerMeta root() {
        MinecraftServerMeta meta = new MinecraftServerMeta();

        Map<String, Object> map = new HashMap<>();
        map.put("serverName", "test");
        map.put("implementationName", "test-v0.1");
        map.put("implementationVersion", "test-v0.1");
        map.put("feature.non_email_login", true);

        meta.setSignaturePublickey(KeyUtils.toPEMPublicKey(getSignaturePublicKey()));
        meta.setSkinDomains(Lists.newArrayList("localhost"));
        meta.setMeta(map);

        return meta;
    }

    @GetMapping("/status")
    public Map<?, ?> status() {
        Map<String, Object> map = new HashMap<>();
        map.put("user.count", 0);
        map.put("token.count", 0);
        map.put("pendingAuthentication.count", 0);
        return map;
    }

    @PostMapping("/api/minecraft/session/join")
    @ResponseStatus(NO_CONTENT)
    public void joinServer(@RequestBody @Valid JoinServerRequest req) {

    }

    @GetMapping("/sessionserver/session/minecraft/hasJoined")
    public ResponseEntity<?> hasJoinedServer(@RequestParam String serverId, @RequestParam String username) {
        MinecraftUser yggdrasilCharacter = new MinecraftUser();
        yggdrasilCharacter.setName("Name");
        yggdrasilCharacter.setUuid(UUID.randomUUID());
        return ok(yggdrasilCharacter.toCompleteResponse(true));
    }

}
