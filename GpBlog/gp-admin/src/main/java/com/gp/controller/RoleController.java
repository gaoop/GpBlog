package com.gp.controller;

import com.gp.domain.ResponseResult;
import com.gp.domain.dto.RoleDto;
import com.gp.domain.entity.Role;
import com.gp.enums.AppHttpCodeEnum;
import com.gp.service.RoleMenuService;
import com.gp.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    private RoleService  roleService;

    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        List<Role> roles = roleService.selectRoleAll();
        return ResponseResult.okResult(roles);
    }



    /**
     * 根据角色编号获取详细信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult getRole(@PathVariable(value = "id") Long id){
        Role byId = roleService.getById(id);
        return ResponseResult.okResult(byId);
    }

    /**
     * 新增角色
     * @param role
     * @return
     */
    @PostMapping
    public ResponseResult add(@RequestBody Role role){
        roleService.insertRole(role);
        return ResponseResult.okResult();
    }

    @GetMapping("/list")
    public ResponseResult getInfo(Integer pageNum,Integer pageSize,String roleName,String status){
        ResponseResult getinfo= roleService.getInfo(pageNum,pageSize,roleName,status);
        return getinfo;
    }
    @PutMapping("/changeStatus")
    public ResponseResult grtchangeStatus(@RequestBody RoleDto roleDto){
        Role role=new Role();
        role.setStatus(roleDto.getStatus());
        role.setId(roleDto.getRoleId());
        return roleService.grtchangeStatus(role);
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult deleRole(@PathVariable(value = "id") Long id){
        if (roleService.removeById(id)) {
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.DELETE_ERROR);
    }

    /**
     * 修改保存角色
     * @param role
     * @return
     */
    @PutMapping
    public ResponseResult edit(@RequestBody Role role)
    {
        roleService.updateRole(role);
        return ResponseResult.okResult();
    }

}
