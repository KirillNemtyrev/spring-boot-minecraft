package com.community.server.dto.minecraft;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class JoinServerRequest {
    public @NotBlank String accessToken;
    public @NotBlank String selectedProfile;
    public @NotBlank String serverId;
}
