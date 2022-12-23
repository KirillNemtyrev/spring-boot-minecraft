package com.community.server.controller;

import com.community.server.dto.ClientDto;
import com.community.server.service.LauncherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/launcher")
public class LauncherController {

    @Autowired
    private LauncherService launcherService;

    @GetMapping("/info/{name}")
    public ClientDto getLauncher(@PathVariable String name) {
        return launcherService.getLauncher(name);
    }

    @GetMapping
    public void getFile(@RequestParam String path, HttpServletResponse httpServletResponse){
        launcherService.getFile(path, httpServletResponse);
    }

}
