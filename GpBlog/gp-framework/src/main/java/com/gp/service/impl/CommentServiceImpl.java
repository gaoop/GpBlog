package com.gp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gp.constants.SystemConstants;
import com.gp.domain.ResponseResult;
import com.gp.domain.entity.Comment;
import com.gp.domain.vo.CommentVo;
import com.gp.domain.vo.PageVo;
import com.gp.enums.AppHttpCodeEnum;
import com.gp.handler.exception.SystemException;
import com.gp.mapper.CommentMapper;
import com.gp.service.CommentService;
import com.gp.service.UserService;
import com.gp.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2022-10-30 15:47:07
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;
    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize) {
        //查询对应文章的根评论

        //对articleId进行判断
        LambdaQueryWrapper<Comment> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId,articleId);
        //根评论 rootId为-1
        queryWrapper.eq(Comment::getRootId,-1);
        //评论类型
        queryWrapper.eq(Comment::getType,commentType);
        queryWrapper.orderByDesc(Comment::getCreateTime);
        //分页查询
        Page<Comment> page=new Page(pageNum,pageSize);
        page(page,queryWrapper);

        List<CommentVo> commentVos= toCommentList(page.getRecords());
        //查询所有根评论对应的子评论集合
        for (CommentVo commentVo : commentVos) {
            //查询对应的子评论
            List<CommentVo>children=getChildren(commentVo.getId());
            commentVo.setChildren(children);
        }
        return ResponseResult.okResult(new PageVo(commentVos,page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        //comment.setCreateBy(SecurityUtils.getUserId());
        if (!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTENE_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }

    /**
     * 根据根评论查询所对应的子评论的集合
     * @param id 根评论的id
     * @return
     */
    private List<CommentVo> getChildren(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper=new LambdaQueryWrapper<>();

        queryWrapper.eq(Comment::getRootId,id);

        queryWrapper.orderByDesc(Comment::getCreateTime);
        List<Comment> list = list(queryWrapper);

        List<CommentVo> commentVos = toCommentList(list);

        return commentVos;
    }

    private List<CommentVo> toCommentList(List<Comment> list){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //遍历vo集合
        for (CommentVo commentVo:commentVos){
        //通过creatyBy查询用户的昵称并赋值
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
            //通过toCommentUserId查询用户并赋值
            //如果toCommentUserId不为-1才进行查询
            if (commentVo.getToCommentUserId()!=-1){
                String byId = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(byId);
            }

        }
        return commentVos;
    }
}
