package com.tailwolf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
;
import com.tailwolf.service.ProjectService;

@RestController
@RequestMapping("/project")
public class Projectcontroller {
    @Autowired
    private ProjectService projectService;
}