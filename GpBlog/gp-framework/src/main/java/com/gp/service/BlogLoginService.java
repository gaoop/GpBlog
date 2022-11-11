package com.gp.service;

import com.gp.domain.ResponseResult;
import com.gp.domain.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2022-10-29 22:07:13
 */
public interface BlogLoginService{

    ResponseResult login(User user);

    ResponseResult logout();
}
