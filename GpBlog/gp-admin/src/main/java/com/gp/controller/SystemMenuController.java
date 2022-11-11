package com.gp.controller;

import com.gp.domain.ResponseResult;
import com.gp.domain.dto.MenuDto;
import com.gp.domain.entity.Menu;
import com.gp.domain.vo.MenuTreeVo;
import com.gp.domain.vo.RoleMenuTreeSelectVo;
import com.gp.service.CommentService;
import com.gp.service.MenuService;
import com.gp.utils.SystemConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class SystemMenuController {
    @Autowired
    private MenuService menuService;
    

    @GetMapping("/list")
    public ResponseResult getList(String menuName,String status){
        return menuService.getList(menuName,status);
    }
    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu){
        return menuService.addMenu(menu);
    }
    @DeleteMapping("{menuId}")
    public ResponseResult deleteMenu(@PathVariable(value = "menuId")Long menuId){
        return menuService.deleteMenu(menuId);
    }
    @GetMapping("/{menuId}")
    public ResponseResult upMenu(@PathVariable(value = "menuId") Long menuId){
        return menuService.getMenu(menuId);
    }
    @GetMapping("/treeselect")
    public ResponseResult getTreeselect(){
        List<Menu> menus= menuService.selectMenuList(new Menu());
        List<MenuTreeVo> options =  SystemConverter.buildMenuSelectTree(menus);
        return ResponseResult.okResult(options);
    }
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult getTree(@PathVariable(value = "id") Long id){
        List<Menu> menus = menuService.selectMenuList(new Menu());
        List<Long> checkedKeys = menuService.selectMenuListByRoleId(id);
        List<MenuTreeVo> menuTreeVos = SystemConverter.buildMenuSelectTree(menus);
        RoleMenuTreeSelectVo vo = new RoleMenuTreeSelectVo(checkedKeys,menuTreeVos);
        return ResponseResult.okResult(vo);
    }

}
