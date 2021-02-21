package com.tailwolf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
;
import com.tailwolf.service.SysUserService;

@RestController
@RequestMapping("/sysUser")
public class SysUsercontroller {
    @Autowired
    private SysUserService sysUserService;
}