package com.gp.controller;

import com.gp.domain.ResponseResult;
import com.gp.domain.entity.Role;
import com.gp.domain.entity.User;
import com.gp.domain.vo.UserInfoAndRoleIdsVo;
import com.gp.enums.AppHttpCodeEnum;
import com.gp.handler.exception.SystemException;
import com.gp.service.RoleService;
import com.gp.service.UserService;
import com.gp.utils.SecurityUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/user")
public class UserConteoller {
    @Autowired
    private UserService userService;

    /**
     * 获取用户列表
     * @param pageNum
     * @param pageSize
     * @param user
     * @return
     */
    @GetMapping("/list")
    public ResponseResult getInfo(Integer pageNum, Integer pageSize,User user){
        return userService.getInfo(pageNum,pageSize,user);
    }

    /**
     * 新增用户
     * @param user
     * @return
     */
    @PostMapping
    public ResponseResult add(@RequestBody User user) {
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        if (!userService.checkUserNameUnique(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (!userService.checkPhoneUnique(user)){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        if (!userService.checkEmailUnique(user)){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        return userService.addUser(user);
    }

    @Autowired
    private RoleService roleService;

    /**
     * 根据用户编号获取详细信息
     * @param userId
     * @return
     */
    @GetMapping(value = { "/{id}" })
    public ResponseResult getUserInfoAndRoleIds(@PathVariable(value = "id") Long userId)
    {
        List<Role> roles = roleService.selectRoleAll();
        User user = userService.getById(userId);
        //当前用户所具有的角色id列表
        List<Long> roleIds = roleService.selectRoleIdByUserId(userId);

        UserInfoAndRoleIdsVo vo = new UserInfoAndRoleIdsVo(user,roles,roleIds);
        return ResponseResult.okResult(vo);
    }

    /**
     * 修改用户
     * @param user
     * @return
     */

    @PutMapping
    public ResponseResult edit(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseResult.okResult();
    }

    /**
     * 删除用户
     * @param userIds
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult remove(@PathVariable(value = "id") List<Long> userIds) {
        if(userIds.contains(SecurityUtils.getUserId())){
            return ResponseResult.errorResult(500,"不能删除当前你正在使用的用户");
        }
        userService.removeByIds(userIds);
        return ResponseResult.okResult();
    }

}
