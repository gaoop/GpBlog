package com.gp.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailVo {
    private Long id;
    //标题
    private String title;
    //文章摘要
    private String summary;
    //文章内容
    private String content;
    //所属分类名
    private String categoryName;
    //所属分类id
    private Long categoryId;
    //缩略图
    private String thumbnail;
    //访问量
    private Long viewCount;
    private Date createTime;
}
