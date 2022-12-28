package com.community.server.controller;

import com.community.server.controller.dto.ClientDto;
import com.community.server.controller.dto.SettingsDto;
import com.community.server.service.LauncherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    public SettingsDto getClientSettings(@PathVariable String name, HttpServletRequest httpServletRequest) {
        return launcherService.getLauncherSettings(name, httpServletRequest);
    }

    @GetMapping
    public void getFile(@RequestParam String path, HttpServletResponse httpServletResponse){
        launcherService.getFile(path, httpServletResponse);
    }

}
