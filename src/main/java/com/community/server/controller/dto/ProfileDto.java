package com.community.server.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProfileDto {
    private String login;
    private String name;
    private BigDecimal balance;
    private ServerDto[] servers;
}
