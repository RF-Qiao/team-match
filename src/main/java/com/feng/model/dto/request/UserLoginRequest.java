package com.feng.model.dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -8310782895897361376L;
    private String userAccount;
    private String userPassword;

}
