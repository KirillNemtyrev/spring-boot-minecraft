package com.community.server.controller;

import com.community.server.dto.ClientDto;
import com.community.server.service.LoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/loader")
public class LoaderController {

    private final LoaderService loaderService;

    public LoaderController(LoaderService loaderService) {
        this.loaderService = loaderService;
    }

    @GetMapping("/info")
    public ClientDto getInfo(){
        return loaderService.getLoader();
    }

    @GetMapping
    public void getFile(@RequestParam String path, HttpServletResponse httpServletResponse){
        loaderService.getFile(path, httpServletResponse);
    }
}
