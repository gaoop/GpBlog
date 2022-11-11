package com.gp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gp.domain.entity.UserRole;
import com.gp.mapper.UserRoleMapper;
import com.gp.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2022-11-11 22:04:21
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
