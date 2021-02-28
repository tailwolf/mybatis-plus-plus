package com.tailwolf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
;
import com.tailwolf.service.SysRoleService;

@RestController
@RequestMapping("/sysRole")
public class SysRolecontroller {
    @Autowired
    private SysRoleService sysRoleService;
}