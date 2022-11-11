package com.gp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gp.constants.SystemConstants;
import com.gp.domain.ResponseResult;
import com.gp.domain.dto.CategoryDto;
import com.gp.domain.entity.Article;
import com.gp.domain.entity.Category;
import com.gp.domain.vo.CategoryVo;
import com.gp.domain.vo.PageVo;
import com.gp.enums.AppHttpCodeEnum;
import com.gp.mapper.CategoryMapper;
import com.gp.service.ArtivleService;
import com.gp.service.CategoryService;
import com.gp.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2022-10-29 18:28:49
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArtivleService artivleService;


    @Override
    public ResponseResult getCategoryList() {
        //查询文章表，状态为已发布的状态
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> list = artivleService.list(queryWrapper);
        //获取文章的分类id，并且去重
        Set<Long> categoryIds = list.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());
        //查询分类表

        List<Category> categories = listByIds(categoryIds);
        List<Category> collect = categories.stream()
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        //封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(collect, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public List<CategoryVo> listAllcategory() {
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getStatus,"0");
        List<Category> list = list(queryWrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return categoryVos;
    }

    @Override
    public ResponseResult getList(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name),Category::getName,name);
        queryWrapper.eq(StringUtils.hasText(status),Category::getStatus,status);
        Page page = new Page(pageNum, pageSize);
        page(page,queryWrapper);
        List<Category> categories= page.getRecords();
        List<CategoryDto> vs = BeanCopyUtils.copyBeanList(categories, CategoryDto.class);
        return ResponseResult.okResult(new PageVo(vs,page.getTotal()));
    }

    @Override
    public ResponseResult add(Category category) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/ HH/mm/ss");
        String datePath = sdf.format(new Date());
        try {
            category.setCreateTime(sdf.parse(datePath));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (save(category)) {
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }
}
