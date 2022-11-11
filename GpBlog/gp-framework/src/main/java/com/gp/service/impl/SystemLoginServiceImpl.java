package com.gp.service.impl;

import com.gp.domain.ResponseResult;
import com.gp.domain.entity.LoginUser;
import com.gp.domain.entity.User;
import com.gp.domain.vo.BlogUserLoginVo;
import com.gp.domain.vo.UserInfoVo;
import com.gp.service.LoginService;
import com.gp.utils.BeanCopyUtils;
import com.gp.utils.JwtUtil;
import com.gp.utils.RedisCache;
import com.gp.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
@Service("loginservic")
public class SystemLoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if (Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //获取userid生成token
        LoginUser loginUser= (LoginUser) authenticate.getPrincipal();
        String userId=loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //把用户信息存入redis
        redisCache.setCacheObject("Adminlogin:"+userId,loginUser);
        //把token和userinfo封装返回
//        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
//        BlogUserLoginVo vo=new BlogUserLoginVo(jwt,userInfoVo);
        Map<String,String> map=new HashMap<>();
        map.put("token",jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject("Adminlogin:"+userId);
        return ResponseResult.okResult();
    }

//    @Override
//    public ResponseResult logout() {
//        //获取token
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        //获取userid
//        Long id = loginUser.getUser().getId();
//        //删除redis中的用户信息
//        redisCache.deleteObject("bloglogin:"+id);
//        return ResponseResult.okResult();
//    }
}
