package com.gp.utils.serurity;

import com.alibaba.fastjson.JSON;
import com.gp.domain.ResponseResult;
import com.gp.enums.AppHttpCodeEnum;
import com.gp.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        e.printStackTrace();
        ResponseResult responseResult=ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        ResponseResult responseResult1=null;
        if (e instanceof BadCredentialsException){
            responseResult1=ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(),e.getMessage());
        }else if (e instanceof InsufficientAuthenticationException){
            responseResult1=ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }else {
            responseResult1=ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"登录或授权失败");
        }
        WebUtils.renderString(httpServletResponse, JSON.toJSONString(responseResult));
    }
}
