package com.gp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gp.constants.SystemConstants;
import com.gp.domain.ResponseResult;
import com.gp.domain.entity.User;
import com.gp.domain.entity.UserRole;
import com.gp.domain.vo.PageVo;
import com.gp.domain.vo.UserInfoVo;
import com.gp.domain.vo.UserVo;
import com.gp.enums.AppHttpCodeEnum;
import com.gp.handler.exception.SystemException;
import com.gp.mapper.UserMapper;
import com.gp.service.UserRoleService;
import com.gp.service.UserService;
import com.gp.utils.BeanCopyUtils;
import com.gp.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2022-10-30 16:14:14
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public ResponseResult userInfo() {
        //获取当前id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User byId = getById(userId);
        //封装曾UserInfoVo
        UserInfoVo vo= BeanCopyUtils.copyBean(byId,UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())&&!StringUtils.hasText(user.getNickName())&&!StringUtils
                .hasText(user.getEmail())&&!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.NAME_NICKNAME_EMAIL_PASSWORD);
        }
        //对数据进行是否存在的判断
        if (userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }

        if (userNickExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNANE_EXIST);
        }
        if (userEmailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //存入数据库
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getInfo(Integer pageNum, Integer pageSize,User users) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(users.getUserName()),User::getUserName,users.getUserName());
        queryWrapper.eq(StringUtils.hasText(users.getPhonenumber()),User::getPhonenumber,users.getPhonenumber());
        queryWrapper.eq(StringUtils.hasText(users.getStatus()),User::getStatus,users.getStatus());
        Page page = new Page(pageNum, pageSize);
        page(page, queryWrapper);
        //List<User> records = page.getRecords();
        List<User> users1 = page.getRecords();
        List<UserVo> userVoList = users1.stream()
                .map(u -> BeanCopyUtils.copyBean(u, UserVo.class))
                .collect(Collectors.toList());
        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(userVoList);

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public boolean checkUserNameUnique(String userName) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getUserName,userName))==0;
    }

    @Override
    public boolean checkPhoneUnique(User user) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getPhonenumber,user.getPhonenumber()))==0;
    }

    @Override
    public boolean checkEmailUnique(User user) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getEmail,user.getEmail()))==0;
    }

    @Override
    @Transactional
    public ResponseResult addUser(User user) {
        //密码加密处理
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);
        if (user.getRoleIds()!=null&&user.getRoleIds().length>0){
            insertUserRole(user);
        }
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        // 删除用户与角色关联
        LambdaQueryWrapper<UserRole> userRoleUpdateWrapper = new LambdaQueryWrapper<>();
        userRoleUpdateWrapper.eq(UserRole::getUserId,user.getId());
        userRoleService.remove(userRoleUpdateWrapper);

        // 新增用户与角色管理
        insertUserRole(user);
        // 更新用户信息
        updateById(user);
    }

    @Autowired
    private UserRoleService userRoleService;
    private void insertUserRole(User user) {
        List<UserRole> sysUserRoles = Arrays.stream(user.getRoleIds())
                .map(roleId -> new UserRole(user.getId(), roleId)).collect(Collectors.toList());
        userRoleService.saveBatch(sysUserRoles);
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        return count(queryWrapper)>0;
    }

    private boolean userNickExist(String userNickExist) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,userNickExist);
        return count(queryWrapper)>0;
    }
    private boolean userEmailExist(String userEmailExist) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,userEmailExist);
        return count(queryWrapper)>0;
    }
}
