package com.feng.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class TeamJoinRequest implements Serializable {
    private static final long serialVersionUID = -5666374219169610403L;

    /**
     * id
     */
    private Long teamId;
    /**
     * 密码
     */
    private String password;
}
