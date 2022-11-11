package com.gp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gp.constants.SystemConstants;
import com.gp.domain.ResponseResult;
import com.gp.domain.entity.Role;
import com.gp.domain.vo.PageVo;
import com.gp.domain.vo.RoleVo;
import com.gp.enums.AppHttpCodeEnum;
import com.gp.mapper.RoleMapper;
import com.gp.service.RoleMenuService;
import com.gp.service.RoleService;
import com.gp.utils.BeanCopyUtils;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.gp.domain.entity.RoleMenu;
import com.gp.domain.dto.getRole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2022-10-31 19:46:48
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;

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
    public ResponseResult getInfo(Integer pageNum, Integer pageSize, String roleName,String status) {
        LambdaQueryWrapper<Role> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(roleName),Role::getRoleName,roleName);
        queryWrapper.eq(StringUtils.hasText(status),Role::getStatus,status);
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
    @Override
    public void insertRole(Role role){
        save(role);
        if (role.getMenuIds()!=null&&role.getMenuIds().length>0){
            insertRoleMeny(role);
        }
    }

    @Override
    public void updateRole(Role role) {
        updateById(role);
        roleMenuService.deleteRoleMenuByRoleId(role.getId());
        insertRoleMeny(role);
    }

    @Override
    public List<Role> selectRoleAll() {
        return list(Wrappers.<Role>lambdaQuery().eq(Role::getStatus, SystemConstants.NORMAL));
    }

    @Override
    public List<Long> selectRoleIdByUserId(Long userId) {
        return getBaseMapper().selectRoleIdByUserId(userId);
    }

//    @Override
//    public ResponseResult getRole(Long id) {
//        LambdaQueryWrapper<Role> queryWrapper=new LambdaQueryWrapper<>();
//        queryWrapper.eq(Role::getId,id);
//        List<Role> list = list(queryWrapper);
//        getRole getRoles = BeanCopyUtils.copyBean(list, getRole.class);
//        return ResponseResult.okResult(getRoles);
//    }

    private void insertRoleMeny(Role role) {
        List<RoleMenu> collect = Arrays.stream(role.getMenuIds())
                .map(memuId -> new RoleMenu(role.getId(), memuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(collect);
    }
}
