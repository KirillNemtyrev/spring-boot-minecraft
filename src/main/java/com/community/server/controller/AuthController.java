package com.community.server.controller;

import com.community.server.body.SignInBody;
import com.community.server.body.SignUpBody;
import com.community.server.entity.SkinEntity;
import com.community.server.repository.SkinsRepository;
import com.community.server.repository.UserRepository;
import com.community.server.service.AuthService;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final SkinsRepository skinsRepository;

    public AuthController(AuthService authService, SkinsRepository skinsRepository) {
        this.authService = authService;
        this.skinsRepository = skinsRepository;
    }

    @PostMapping("/signing")
    public ResponseEntity<?> signing(@Valid @RequestBody SignInBody signInBody) {
        return authService.auth(signInBody);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpBody signUpBody) {
        return authService.create(signUpBody);
    }

    @SneakyThrows
    @GetMapping("/{uuid}")
    public ResponseEntity<?> update(@PathVariable String uuid){
        BufferedImage image = ImageIO.read(new File("resources/skins/default.png"));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] bytes = baos.toByteArray();

        SkinEntity skinEntity = new SkinEntity();
        skinEntity.setData(bytes);
        skinEntity.setHash(uuid);
        skinEntity.setUrl(null);
        skinsRepository.save(skinEntity);
        return null;
    }
}
