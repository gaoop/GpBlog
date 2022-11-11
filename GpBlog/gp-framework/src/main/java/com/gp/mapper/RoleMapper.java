package com.gp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gp.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-31 19:46:47
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyUserId(Long userId);

    List<Long> selectRoleIdByUserId(Long userId);
}

