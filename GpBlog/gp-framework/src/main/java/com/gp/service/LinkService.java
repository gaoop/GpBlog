package com.gp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gp.domain.ResponseResult;
import com.gp.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2022-10-29 21:39:42
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();
}
