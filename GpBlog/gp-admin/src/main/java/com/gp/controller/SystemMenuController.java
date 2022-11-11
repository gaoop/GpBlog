package com.gp.controller;

import com.gp.domain.ResponseResult;
import com.gp.domain.dto.MenuDto;
import com.gp.domain.entity.Menu;
import com.gp.service.CommentService;
import com.gp.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class SystemMenuController {
    @Autowired
    private MenuService menuService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/list")
    public ResponseResult getList(){
        return menuService.getList();
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

        return ResponseResult.okResult();
    }

}
