package com.gp.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 用户和角色关联表(UserRole)表实体类
 *
 * @author makejava
 * @since 2022-11-11 22:04:20
 */
@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user_role")
public class UserRole {
    //用户ID
    private Long userId;
    //角色ID
    private Long roleId;



}

