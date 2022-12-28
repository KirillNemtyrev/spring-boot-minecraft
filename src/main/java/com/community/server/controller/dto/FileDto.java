package com.community.server.controller.dto;

import lombok.Data;

@Data
public class FileDto {
    private String path;
    private String md5;
    private long size;

    @Override
    public String toString(){
        return "path: " + path + " | md5: " + md5 + " | size: " + size;
    }
}
