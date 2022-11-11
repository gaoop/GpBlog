package com.gp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gp.domain.ResponseResult;
import com.gp.domain.entity.Category;
import com.gp.domain.vo.CategoryVo;

import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2022-10-29 18:28:49
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    List<CategoryVo> listAllcategory();

    ResponseResult getList(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult add(Category category);
}
