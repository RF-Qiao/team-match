package com.feng.pojo.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 队伍修改请求类
 * @TableName team
 */
@Data
public class TeamUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;
    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 用户
     */
    private Integer userId;

    /**
     * 过期时间
     */
    private Date expireTime;
    /**
     * 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

    /**
     * 密码
     */
    private String password;


}