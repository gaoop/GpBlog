package com.gp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gp.constants.SystemConstants;
import com.gp.domain.ResponseResult;
import com.gp.domain.dto.ArticleDto;
import com.gp.domain.dto.addArticleDto;
import com.gp.domain.entity.Article;
import com.gp.domain.entity.ArticleTag;
import com.gp.domain.entity.Category;
import com.gp.domain.vo.*;
import com.gp.enums.AppHttpCodeEnum;
import com.gp.mapper.ArticleMapper;
import com.gp.service.ArticleTagService;
import com.gp.service.ArtivleService;
import com.gp.service.CategoryService;
import com.gp.utils.BeanCopyUtils;
import com.gp.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArtivleService {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private ArticleMapper articleMapper;


    @Override
    public ResponseResult hotArticleList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.orderByDesc(Article::getViewCount);
        Page<Article> page = new Page<>(1, 10);
        page(page, queryWrapper);
        List<Article> records = page.getRecords();

        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(records, HotArticleVo.class);

        return ResponseResult.okResult(hotArticleVos);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long cattegoryId) {
        //查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //如果有cattegoryIId就要查询时要和传入的相同
        queryWrapper.eq(Objects.nonNull(cattegoryId) && cattegoryId > 0, Article::getCategoryId, cattegoryId);
        //状态是正式发布的
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //对isTop降序实现置顶
        queryWrapper.orderByDesc(Article::getIsTop);
        //分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        //查询分类名称
        List<Article> records = page.getRecords();
        //articleID去查询articleName进行设置
        records.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());
//        for (Article article:records){
//            Category byId = categoryService.getById(article.getCategoryId());
//            article.setCategoryName(byId.getName());
//        }
        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);

        PageVo pageVo = new PageVo(articleListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article byId = getById(id);
        //转换成vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(byId, ArticleDetailVo.class);
        //根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category byId1 = categoryService.getById(categoryId);
        if (byId1 != null) {
            articleDetailVo.setCategoryName(byId1.getName());
        }
        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.incrementCacheMapValue("ViewCount", id.toString(), 1);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult add(addArticleDto article) {
        Article article1 = BeanCopyUtils.copyBean(article, Article.class);
        save(article1);

        List<ArticleTag> collect = article.getTags().stream()
                .map(tagId -> new ArticleTag(article1.getId(), tagId))
                .collect(Collectors.toList());

        articleTagService.saveBatch(collect);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getList(Integer pageNum, Integer pageSize, String title, String summary) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        Page<Article> page = new Page<>(pageNum, pageSize);
        if (StringUtils.hasText(title)) {
            queryWrapper.like(Article::getTitle, title);
        }
        if (StringUtils.hasText(summary)){
            queryWrapper.like(Article::getSummary,summary);
        }
        page(page,queryWrapper);
        List<Article> articleList=page.getRecords();
        List<ArticleVo> articleVos = BeanCopyUtils.copyBeanList(articleList, ArticleVo.class);
        PageVo articleVo = new PageVo(articleVos,page.getTotal());
        return ResponseResult.okResult(articleVo);
    }

    @Override
    public ResponseResult deleteArticle(Long id) {
        if (Objects.isNull(id)){
            return ResponseResult.errorResult(AppHttpCodeEnum.DELETE_ERROR);
        }
        int i = articleMapper.deleteById(id);
        if (i>0){
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.DELETE_ERROR);
    }

    @Override
    public ArticleVo getInfo(Long id) {
        Article byId = getById(id);
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper=new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,byId.getId());
        List<ArticleTag> list = articleTagService.list(articleTagLambdaQueryWrapper);
        List<Long> collect = list.stream().map(articleTag -> articleTag.getTagId()).collect(Collectors.toList());
        ArticleVo articleVo = BeanCopyUtils.copyBean(byId, ArticleVo.class);
        articleVo.setTags(collect);
        return articleVo;
    }

    @Override
    public void edit(ArticleDto article) {
        Article article1 = BeanCopyUtils.copyBean(article, Article.class);
        //更新博客信息
        updateById(article1);
        //删除原有的 标签和博客的关联
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,article.getId());
        articleTagService.remove(articleTagLambdaQueryWrapper);
        //添加新的博客和标签的关联信息
        List<ArticleTag> articleTags = article.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
    }


}