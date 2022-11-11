package com.gp.controller;

import com.gp.domain.ResponseResult;
import com.gp.domain.entity.LoginUser;
import com.gp.domain.entity.Menu;
import com.gp.domain.entity.User;
import com.gp.domain.vo.AdminUserInfoVo;
import com.gp.domain.vo.RoutersVo;
import com.gp.domain.vo.UserInfoVo;
import com.gp.enums.AppHttpCodeEnum;
import com.gp.handler.exception.SystemException;
import com.gp.service.BlogLoginService;
import com.gp.service.LoginService;
import com.gp.service.MenuService;
import com.gp.service.RoleService;
import com.gp.service.impl.SystemLoginServiceImpl;
import com.gp.utils.BeanCopyUtils;
import com.gp.utils.RedisCache;
import com.gp.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if (!StringUtils.hasText(user.getUserName())){
            //提示必须输入用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo(){
        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String> perms=menuService.selectPermsByUserId(loginUser.getUser().getId());
        //根据用户id查询角色信息
        List<String> roleKeyUserId=roleService.selectRoleKeyUserId(loginUser.getUser().getId());
        //获取用户信息
        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        //封装数据返回

        AdminUserInfoVo adminUserInfoVo=new AdminUserInfoVo(perms,roleKeyUserId,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    /**
     * 查询左边列表
     * @return
     */
    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        Long userId = SecurityUtils.getUserId();
        //查询menu结果是tree的形式
        List<Menu> menus= menuService.selectRouterTreeByUserId(userId);
        //封装数据

        return ResponseResult.okResult(new RoutersVo(menus));
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        //获取当前登录的用户id
        //Long userId = SecurityUtils.getUserId();
        //删除redis中对应的值
        //redisCache.deleteObject("bloglogin:"+userId);
        return loginService.logout();
    }

}
