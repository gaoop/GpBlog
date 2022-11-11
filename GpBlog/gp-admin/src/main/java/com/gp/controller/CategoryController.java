package com.gp.controller;

import com.gp.domain.ResponseResult;
import com.gp.domain.entity.Category;
import com.gp.domain.vo.CategoryVo;
import com.gp.domain.vo.TagVo;
import com.gp.enums.AppHttpCodeEnum;
import com.gp.service.CategoryService;
import com.gp.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        List<CategoryVo> list = categoryService.listAllcategory();
        for (CategoryVo categoryVo : list) {
            System.out.println(categoryVo);
        }
        return ResponseResult.okResult(list);
    }

}
