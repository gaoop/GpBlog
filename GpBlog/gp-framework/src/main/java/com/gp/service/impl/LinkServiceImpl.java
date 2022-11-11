package com.gp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gp.constants.SystemConstants;
import com.gp.domain.ResponseResult;
import com.gp.domain.dto.LinkDto;
import com.gp.domain.entity.Link;
import com.gp.domain.vo.LinkVo;
import com.gp.domain.vo.PageVo;
import com.gp.mapper.LinkMapper;
import com.gp.service.LinkService;
import com.gp.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2022-10-29 21:39:42
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        LambdaQueryWrapper<Link> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> list = list(queryWrapper);
        List<LinkVo> linkvo = BeanCopyUtils.copyBeanList(list, LinkVo.class);
        return ResponseResult.okResult(linkvo);
    }

    @Override
    public ResponseResult getList(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Link> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name),Link::getName,name);
        queryWrapper.eq(StringUtils.hasText(status),Link::getStatus,status);
        Page page= new Page(pageNum,pageSize);
        page(page,queryWrapper);
        List<Link> records = page.getRecords();
        List<LinkDto> linkVos = BeanCopyUtils.copyBeanList(records, LinkDto.class);
        return ResponseResult.okResult(new PageVo(linkVos,page.getTotal()));
    }

    @Override
    public ResponseResult getInfo(Long id) {
        LambdaQueryWrapper<Link> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getId,id);
        List<Link> list = list(queryWrapper);
        List<LinkDto> linkDtos = BeanCopyUtils.copyBeanList(list, LinkDto.class);
        return ResponseResult.okResult(linkDtos);
    }
}
