package com.gp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gp.domain.ResponseResult;
import com.gp.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2022-10-30 15:47:07
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}
