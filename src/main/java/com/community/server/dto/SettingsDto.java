package com.community.server.dto;

import lombok.Data;

@Data
public class SettingsDto {
    private String uuid;
    private String username;
    private String javaagent;
    private String natives;
    private String libraries;
    private String launchwrapper;
    private String version;
    private String index;
    private String user;
    private String tweak;
    private String fml;
    private String type;
}
