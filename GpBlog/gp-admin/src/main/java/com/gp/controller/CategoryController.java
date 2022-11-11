package com.gp.controller;

import com.gp.domain.ResponseResult;
import com.gp.domain.dto.CategoryDto;
import com.gp.domain.entity.Category;
import com.gp.domain.vo.CategoryVo;
import com.gp.enums.AppHttpCodeEnum;
import com.gp.service.CategoryService;
import com.gp.utils.BeanCopyUtils;
import com.gp.utils.SecurityUtils;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize,String name,String status){
        return categoryService.getList(pageNum,pageSize,name,status);
    }

    @PostMapping
    public ResponseResult add(@RequestBody Category category){
        return categoryService.add(category);
    }
    @GetMapping("/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id") Long id){
        Category byId = categoryService.getById(id);
        CategoryDto categoryDto = BeanCopyUtils.copyBean(byId, CategoryDto.class);
        return ResponseResult.okResult(categoryDto);
    }

    @PutMapping
    public ResponseResult  modify(@RequestBody Category category){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/ HH/mm/ss");
        String datePath = sdf.format(new Date());
        try {
            category.setUpdateTime(sdf.parse(datePath));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean b = categoryService.updateById(category);
        if (b){
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    @DeleteMapping("/{id}")
    public ResponseResult dele(@PathVariable(value = "id") Long id){
        boolean b = categoryService.removeById(id);
        if (b){
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.DELETE_ERROR);
    }
}
