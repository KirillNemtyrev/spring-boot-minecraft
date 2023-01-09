package com.community.server.controller;

import com.community.server.dto.ClientDto;
import com.community.server.service.LauncherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/launcher")
public class LauncherController {

    @Autowired
    private LauncherService launcherService;

    @GetMapping("/info/{name}")
    public ClientDto getClient(@PathVariable String name) {
        return launcherService.getLauncher(name);
    }

    @GetMapping("/info/settings/{name}")
    public ArrayList<String> getClientSettings(@PathVariable String name, @RequestParam boolean connect, @RequestParam boolean fullscreen, HttpServletRequest httpServletRequest) {
        return launcherService.getLauncherSettings(name, connect, fullscreen, httpServletRequest);
    }

    @GetMapping
    public void getFile(@RequestParam String path, HttpServletResponse httpServletResponse){
        launcherService.getFile(path, httpServletResponse);
    }

}
