package com.feng.pojo.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUser {
    String userName;
    String userPassword;
    String checkPassword;
}
