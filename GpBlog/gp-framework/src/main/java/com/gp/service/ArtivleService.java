package com.gp.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.gp.domain.ResponseResult;
import com.gp.domain.dto.ArticleDto;
import com.gp.domain.dto.addArticleDto;
import com.gp.domain.entity.Article;
import com.gp.domain.vo.ArticleVo;

public interface ArtivleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long cattegoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(addArticleDto article);

    ResponseResult getList(Integer pageNum, Integer pageSize, String title, String summary);

    ResponseResult deleteArticle(Long id);

    ArticleVo getInfo(Long id);

    void edit(ArticleDto article);
}

