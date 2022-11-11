package com.gp.controller;

import com.gp.domain.ResponseResult;
import com.gp.domain.dto.RoleDto;
import com.gp.domain.entity.Role;
import com.gp.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class UserController {
    @Autowired
    private RoleService  roleService;

    @GetMapping("/list")
    public ResponseResult getInfo(Integer pageNum,Integer pageSize,String roleName){
        ResponseResult getinfo= roleService.getInfo(pageNum,pageSize,roleName);
        return getinfo;
    }

    @PutMapping("/changeStatus")
    public ResponseResult grtchangeStatus(@RequestBody RoleDto roleDto){
        Role role=new Role();
        role.setStatus(roleDto.getStatus());
        role.setId(roleDto.getRoleId());
        return roleService.grtchangeStatus(role);
    }
}
