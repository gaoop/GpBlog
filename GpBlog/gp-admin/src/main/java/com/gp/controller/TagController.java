package com.gp.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gp.domain.ResponseResult;
import com.gp.domain.dto.TagListDto;
import com.gp.domain.entity.Tag;
import com.gp.domain.vo.PageVo;
import com.gp.domain.vo.TagVo;
import com.gp.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.serializer.RedisElementReader;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("@ps.hasPermissions('content:tag:index')")
@RequestMapping("/content/tag")
public class TagController {


    @Autowired
    private TagService tagService;
    @GetMapping("/list")
    public ResponseResult<PageVo>  list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody Tag tag){
        return tagService.addTag(tag);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseResult deleteTag(@PathVariable(value = "id") Long id){
        return tagService.deleteTag(id);
    }


    @GetMapping(value = "/{id}")
    public ResponseResult getTag(@PathVariable(value = "id") Long id){
        return tagService.getTag(id);
    }
    @PutMapping
    public ResponseResult putTag(@RequestBody Tag tag){
        return tagService.putTag(tag);
    }


    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagVo> list=tagService.listAllTag();
        return ResponseResult.okResult(list);
    }

}
