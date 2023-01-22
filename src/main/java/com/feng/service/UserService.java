package com.feng.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.feng.pojo.User;

import java.util.List;


public interface UserService extends IService<User> {

    /**
     * 用户登录
     * @param userName
     * @param userPassword
     * @return
     */
    User userLogin(String userName,String userPassword);

    /**
     * 脱敏后的用户信息
     * @param suser
     * @return
     */
    User getSafetyUser(User suser);

    /**
     * 用户注册
     * @param userName
     * @param userPassword
     * @param checkPassword
     * @return
     */
    User register(String userName,String userPassword, String checkPassword);

    /**
     * 根据标签查询用户
     * @param tagNameList
     * @return
     */
    List<User> searchUserByTags(List<String> tagNameList);
}