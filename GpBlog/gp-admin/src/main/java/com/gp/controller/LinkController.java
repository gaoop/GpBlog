package com.gp.controller;

import com.gp.domain.ResponseResult;
import com.gp.domain.dto.LinkDto;
import com.gp.domain.entity.Link;
import com.gp.enums.AppHttpCodeEnum;
import com.gp.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult list(Integer pageNum,Integer pageSize,String name,String  status){
        return linkService.getList(pageNum,pageSize,name,status);
    }
    @PostMapping
    public ResponseResult add(@RequestBody Link link){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/ HH/mm/ss");
        String datePath = sdf.format(new Date());
        try {
            link.setCreateTime(sdf.parse(datePath));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean b = linkService.save(link);
        if (b){
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    @GetMapping("/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id") Long id){
        return linkService.getInfo(id);
    }

    @PutMapping
    public ResponseResult modify(@RequestBody Link link){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/ HH/mm/ss");
        String datePath = sdf.format(new Date());
        try {
            link.setUpdateTime(sdf.parse(datePath));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean b = linkService.updateById(link);
        if (b){
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }
    @DeleteMapping("/{id}")
    public ResponseResult dele(@PathVariable(value = "id") Long id){
        boolean b = linkService.removeById(id);
        if (b){
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.DELETE_ERROR);
    }
    @PutMapping("/changeLinkStatus")
    public ResponseResult tongguo(@RequestBody Link link){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/ HH/mm/ss");
        String datePath = sdf.format(new Date());
        try {
            link.setUpdateTime(sdf.parse(datePath));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean b = linkService.updateById(link);
        if (b){
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }
}
