package com.feng.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.feng.pojo.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface UserService extends IService<User> {

    /**
     * 用户登录
     * @param userName
     * @param userPassword
     * @return
     */
    User userLogin(String userName,String userPassword,HttpServletRequest request);

    /**
     * 脱敏后的用户信息
     * @param suser
     * @return
     */
    User getSafetyUser(User suser );



    /**
     * 用户注册
     * @param userName
     * @param userPassword
     * @param checkPassword
     * @return
     */
    User register(String userName,String userPassword, String checkPassword);

    Integer deleteUserByUserName(String username);

    /**
     * 根据标签查询用户
     * @param tagNameList
     * @return
     */
    List<User> searchUserByTags(List<String> tagNameList);

    /**
     * 是否为管理员  token
     * @param request
     */
    void isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员  session
     * @param request
     * @return
     */
    boolean isadmin(HttpServletRequest request);

    User loginUser(HttpServletRequest request);
}