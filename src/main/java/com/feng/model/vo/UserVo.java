package com.feng.model.vo;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户包装类（脱敏）
 */
@Data
public class UserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private long id;
    private String username;
     private String userAccount;

    private String avatarUrl;

    private Integer gender;
     private String tags;

    private String phone;

    private String contactInfo;
    private String profile;

    private Integer userStatus;

    private Date createTime;


    private Integer userRole;

    private String planetCode;


}