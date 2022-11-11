package com.gp.controller;

import com.gp.domain.ResponseResult;
import com.gp.domain.dto.ArticleDto;
import com.gp.domain.dto.addArticleDto;
import com.gp.domain.vo.ArticleVo;
import com.gp.service.ArtivleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Autowired
    private ArtivleService artivleService;

    @PostMapping
    public ResponseResult add(@RequestBody addArticleDto article){
        return artivleService.add(article);
    }


    @GetMapping("/list")
    public ResponseResult getList(Integer pageNum, Integer pageSize,  String title,String summary){
        return artivleService.getList(pageNum,pageSize,title,summary);
    }

    @DeleteMapping("{id}")
    public ResponseResult deleteArticle(@PathVariable(value = "id") Long id){
        return artivleService.deleteArticle(id);
    }
    @GetMapping("{id}")
    public ResponseResult putArticle(@PathVariable(value = "id") Long id){
        ArticleVo article = artivleService.getInfo(id);
        return ResponseResult.okResult(article);
    }
    @PutMapping
    public ResponseResult edit(@RequestBody ArticleDto article){
        artivleService.edit(article);
        return ResponseResult.okResult();
    }
}
