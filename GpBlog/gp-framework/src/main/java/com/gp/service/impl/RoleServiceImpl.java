package com.gp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gp.domain.ResponseResult;
import com.gp.domain.entity.Role;
import com.gp.domain.vo.PageVo;
import com.gp.domain.vo.RoleVo;
import com.gp.enums.AppHttpCodeEnum;
import com.gp.mapper.RoleMapper;
import com.gp.service.RoleService;
import com.gp.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2022-10-31 19:46:48
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleService roleService;

    @Override
    public List<String> selectRoleKeyUserId(Long id) {
        //判断是否为管理员
        if (id==1L){
            List<String> list=new ArrayList<>();
            list.add("admin");
            return list;
        }
        return getBaseMapper().selectRoleKeyUserId(id);
    }

    @Override
    public ResponseResult getInfo(Integer pageNum, Integer pageSize, String roleName) {
        LambdaQueryWrapper<Role> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(roleName),Role::getRoleName,roleName);
        Page<Role> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        List<Role> records = page.getRecords();
        List<RoleVo> list = BeanCopyUtils.copyBeanList(records,RoleVo.class);
        PageVo pageVo = new PageVo(list, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult grtchangeStatus(Role role) {
        boolean b = updateById(role);
        if (b){
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }
}
