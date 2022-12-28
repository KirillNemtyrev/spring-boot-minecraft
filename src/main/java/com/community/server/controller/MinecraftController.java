package com.community.server.controller;

import com.community.server.dto.minecraft.JoinServerRequest;
import com.community.server.dto.minecraft.MinecraftServerMeta;
import com.community.server.service.AuthService;
import com.community.server.utils.KeyUtils;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

import static com.community.server.utils.KeyUtils.getSignaturePublicKey;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
public class MinecraftController {

    private final AuthService authService;

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
        authService.joinServer(req);
    }

    @GetMapping("/sessionserver/session/minecraft/hasJoined")
    public ResponseEntity<?> hasJoinedServer(@RequestParam String serverId, @RequestParam String username) {
        return ok(authService.hasJoinedServer(username).toCompleteResponse(true));
    }

}
