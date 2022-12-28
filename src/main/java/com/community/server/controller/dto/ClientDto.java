package com.community.server.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class ClientDto {
    private List<FileDto> files;
    private List<String> folders;
    private int countFiles;
    private int countFolders;
}
