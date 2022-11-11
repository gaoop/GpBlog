package com.gp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gp.domain.ResponseResult;
import com.gp.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2022-10-31 19:46:48
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyUserId(Long id);

    ResponseResult getInfo(Integer pageNum, Integer pageSize, String roleName);

    ResponseResult grtchangeStatus(Role role);
}
