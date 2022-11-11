package com.gp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gp.domain.ResponseResult;
import com.gp.domain.dto.TagListDto;
import com.gp.domain.entity.Tag;
import com.gp.domain.vo.PageVo;
import com.gp.domain.vo.TagVo;
import com.gp.enums.AppHttpCodeEnum;
import com.gp.mapper.TagMapper;
import com.gp.service.TagService;
import com.gp.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2022-10-31 17:19:43
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        //分页查询
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()), Tag::getName, tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()), Tag::getRemark, tagListDto.getRemark());
        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);
        //封装数据返回
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(Tag tag) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/ HH/mm/ss");
        String datePath = sdf.format(new Date());
        try {
            tag.setCreateTime(sdf.parse(datePath));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTag(Long id) {
//        for (Tag tag : id) {
//            int i = tagMapper.deleteById(tag.getId());
//            if (i != 0 && i > 0) {
//                return ResponseResult.okResult();
//            }
//        }
        int i = tagMapper.deleteById(id);
        if (i != 0 && i > 0) {
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.DELETE_ERROR);
    }

    @Override
    public ResponseResult getTag(Long id) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getId, id);
        TagVo tag = new TagVo();
        List<Tag> list = list(queryWrapper);
        for (Tag tag1 : list) {
            tag.setId(tag1.getId());
            tag.setName(tag1.getName());
            tag.setRemark(tag1.getRemark());

        }
        return ResponseResult.okResult(tag);
    }

    @Override
    public ResponseResult putTag(Tag tag) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/ HH/mm/ss");
        String datePath = sdf.format(new Date());
        try {
            tag.setUpdateTime(sdf.parse(datePath));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (updateById(tag)) {
            return ResponseResult.okResult();
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    @Override
    public List<TagVo> listAllTag() {
        LambdaQueryWrapper<Tag> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId,Tag::getName);
        List<Tag> list = list(queryWrapper);
        List<TagVo> list1 = BeanCopyUtils.copyBeanList(list, TagVo.class);
        return list1;
    }
}
