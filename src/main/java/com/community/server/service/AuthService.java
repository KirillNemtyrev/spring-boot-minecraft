package com.community.server.service;

import com.community.server.body.SignInBody;
import com.community.server.body.SignUpBody;
import com.community.server.dto.minecraft.JoinServerRequest;
import com.community.server.dto.minecraft.MinecraftUser;
import com.community.server.entity.RoleEntity;
import com.community.server.entity.RoleNameEntity;
import com.community.server.entity.UserEntity;
import com.community.server.exception.AppException;
import com.community.server.repository.RoleRepository;
import com.community.server.repository.UserRepository;
import com.community.server.security.JwtAuthenticationResponse;
import com.community.server.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final Map<String, Long> joinRequests = Collections.synchronizedMap(new HashMap<>());

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider tokenProvider;

    @SneakyThrows
    public ResponseEntity<?> create(SignUpBody signUpBody) {
        if (signUpBody.getUsername() == null || signUpBody.getEmail() == null || signUpBody.getName() == null || signUpBody.getPassword() == null) {
            return new ResponseEntity<>("Auth options are not specified.", HttpStatus.BAD_GATEWAY);
        }

        if (!signUpBody.getUsername().matches("^[a-zA-Z\\d]+$")) {
            return new ResponseEntity<>("Invalid username!", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByUsername(signUpBody.getUsername())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpBody.getEmail())) {
            return new ResponseEntity<>("Email Address already in use!", HttpStatus.BAD_REQUEST);
        }

        if (!signUpBody.getPassword().matches("(?=^.{6,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$")) {
            return new ResponseEntity<>("Wrong password format!", HttpStatus.BAD_REQUEST);
        }

        UserEntity userEntity = new UserEntity(
                signUpBody.getName(), signUpBody.getUsername(), signUpBody.getEmail(), passwordEncoder.encode(signUpBody.getPassword()));

        RoleEntity roleEntity = roleRepository.findByName(RoleNameEntity.ROLE_USER).orElseThrow(
                () -> new AppException("User Role not set."));

        userEntity.setRoles(Collections.singleton(roleEntity));

        File defaultSkin = new File("resources/skins/default.png");
        File defaultPhoto = new File("resources/photo/default.png");

        BufferedImage imagePhoto = ImageIO.read(defaultPhoto);
        File photo = new File("resources/photo/" + signUpBody.getUsername() + ".png");
        if (photo.createNewFile()) {
            ImageIO.write(imagePhoto, "png", photo);
        }

        BufferedImage imageSkin = ImageIO.read(defaultSkin);
        File skin = new File("resources/skins/" + signUpBody.getUsername() + ".png");
        if (skin.createNewFile()) {
            ImageIO.write(imageSkin, "png", skin);
        }

        userRepository.save(userEntity);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }

    public ResponseEntity<?> auth(SignInBody signInBody) {

        if (signInBody.getUsername() == null || signInBody.getPassword() == null) {
            return new ResponseEntity<>("Auth options are not specified.", HttpStatus.BAD_GATEWAY);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInBody.getUsername(), signInBody.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    public void joinServer(JoinServerRequest req) {
        if (!tokenProvider.validateToken(req.accessToken)) {
            throw new IllegalArgumentException("Access token not valid!");
        }

        UserEntity user = userRepository.findById(
                tokenProvider.getUserIdFromJWT(req.accessToken)
        ).orElseThrow(() -> new IllegalArgumentException("User not found!"));

        joinRequests.put(user.getUsername(), user.getId());
    }

    public MinecraftUser hasJoinedServer(String username) {
        Long userId = joinRequests.remove(username);

        if (userId == null) {
            throw new IllegalArgumentException("Not found joined user: " + username);
        }

        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found!"));

        return MinecraftUser.builder()
                .uuid(user.getUuid())
                .name(user.getUsername())
                .build();
    }
}
