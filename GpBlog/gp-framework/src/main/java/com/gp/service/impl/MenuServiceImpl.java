package com.gp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gp.constants.SystemConstants;
import com.gp.domain.ResponseResult;
import com.gp.domain.entity.Menu;
import com.gp.domain.vo.MenuVo;
import com.gp.enums.AppHttpCodeEnum;
import com.gp.mapper.MenuMapper;
import com.gp.service.MenuService;
import com.gp.utils.BeanCopyUtils;
import com.gp.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2022-10-31 19:40:12
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuService menuService;

    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员，返回所有的权限
        if (SecurityUtils.isAdmin()) {
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTEN);
            queryWrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(queryWrapper);
            List<String> collect = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return collect;
        }
        //否则返回当前用户所具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterTreeByUserId(Long userId) {
        MenuMapper baseMapper = getBaseMapper();
        //判断是否为管理员
        List<Menu> menus = null;
        if (SecurityUtils.isAdmin()) {
            //若果是获取所有符合要求的menu
            menus = baseMapper.selectAllRouterMenu();

        } else {
            //否则获取当前用户所具有的menu
            menus=baseMapper.selectRouterMenuTreeByUserId(userId);
        }
        //构建tree
        //找出第一层菜单，再找子菜单设置到children
        List<Menu> menusTree = buildMenuTree(menus,0L);
        return menusTree;
    }

    @Override
    public ResponseResult getList(String menuName, String status){
        LambdaQueryWrapper<Menu> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(menuName),Menu::getMenuName,menuName);
        queryWrapper.eq(StringUtils.hasText(status),Menu::getStatus,status);
        List<Menu> list = list(queryWrapper);
        return ResponseResult.okResult(list);
    }

    @Override
    public ResponseResult addMenu(Menu menu) {
        boolean save = save(menu);
        if (save){
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    @Override
    public ResponseResult deleteMenu(Long menuId) {
        boolean b = removeById(menuId);
        if (b){
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.DELETE_ERROR);
    }

    @Override
    public ResponseResult getMenu(Long menuId) {
        LambdaQueryWrapper<Menu> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getId,menuId);
        List<Menu> list = list(queryWrapper);
        MenuVo vs = null;
        //List<MenuVo> menu = BeanCopyUtils.copyBeanList(list, MenuVo.class);
        for (Menu menu : list) {
            vs = BeanCopyUtils.copyBean(menu, MenuVo.class);
        }
        return ResponseResult.okResult(vs);
    }

    @Override
    public List<Menu> selectMenuList(Menu menu) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        //menuName模糊查询
        queryWrapper.like(StringUtils.hasText(menu.getMenuName()),Menu::getMenuName,menu.getMenuName());
        queryWrapper.eq(StringUtils.hasText(menu.getStatus()),Menu::getStatus,menu.getStatus());
        //排序 parent_id和order_num
        queryWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> menus = list(queryWrapper);;
        return menus;
    }

    @Override
    public List<Long> selectMenuListByRoleId(Long id) {
        return getBaseMapper().selectMenuListByRoleId(id);
    }


//    @Override
//    public ResponseResult getTreeselect() {
//        List<Menu> list = list();
//        //List<MenuDto> menuDtos = BeanCopyUtils.copyBeanList(list, MenuDto.class);
//        MenuDto menuDto=null;
//        for (Menu menu : list) {
//            menuDto = new MenuDto();
//            menuDto.setId(menu.getId());
//            menuDto.setLabel(menu.getMenuName());
//            menuDto.setParentId(menuDto.getParentId());
//        }
//        return ResponseResult.okResult(menuDto);
//    }

    private List<Menu> buildMenuTree(List<Menu> menus, long parentId) {
        List<Menu> collect = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return collect;
    }

    /**
     * 获取传入参数的子菜单menu集合
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> collect = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return collect;
    }


}
