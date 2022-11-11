package com.gp.controller;

import com.gp.domain.ResponseResult;
import com.gp.domain.entity.Article;
import com.gp.service.ArtivleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
@Api(tags = "主页",description = "查询主页类容")
public class ArticleController {

    @Autowired
    private ArtivleService artivleService;

    //    @GetMapping("/list")
//    public List<Article> test(){
//        return artivleService.list();
//    }
    @GetMapping("/hotArticleList")
    @ApiOperation(value = "返回热门文章")
    public ResponseResult hotArticleList() {
        //查询热门文章 封装成ResponseResult返回
        return artivleService.hotArticleList();
    }
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id")Long id){
        return artivleService.updateViewCount(id);
    }

    @GetMapping("/articleList")
    public ResponseResult articleList( Integer pageNum, Integer pageSize, Long categoryId){
        return artivleService.articleList(pageNum,pageSize,categoryId);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return artivleService.getArticleDetail(id);
    }

}
