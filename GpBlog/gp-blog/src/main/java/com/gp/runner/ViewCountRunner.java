package com.gp.runner;

import com.gp.domain.entity.Article;
import com.gp.mapper.ArticleMapper;
import com.gp.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {


    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private RedisCache redisCache;
    @Override
    public void run(String... args) throws Exception {
        //查询博客信息id viewcount
        List<Article> articles=articleMapper.selectList(null);
        Map<String, Integer> collect = articles.stream()
                .collect(Collectors.toMap((Article article1) -> article1.getId().toString(), article -> article.getViewCount().intValue()));
        //存储到redis
        redisCache.setCacheMap("ViewCount",collect);
    }
}
