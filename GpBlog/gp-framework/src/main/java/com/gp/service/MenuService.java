package com.gp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gp.domain.ResponseResult;
import com.gp.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2022-10-31 19:40:12
 */

public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterTreeByUserId(Long userId);

    ResponseResult getList();

    ResponseResult addMenu(Menu menu);

    ResponseResult deleteMenu(Long menuId);

    ResponseResult getMenu(Long menuId);


//    ResponseResult getTreeselect();

}
