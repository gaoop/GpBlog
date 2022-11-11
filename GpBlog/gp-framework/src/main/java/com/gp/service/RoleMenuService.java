package com.gp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gp.domain.entity.RoleMenu;


/**
 * 角色和菜单关联表(RoleMenu)表服务接口
 *
 * @author makejava
 * @since 2022-11-11 20:01:06
 */
public interface RoleMenuService extends IService<RoleMenu> {

    void deleteRoleMenuByRoleId(Long id);
}
