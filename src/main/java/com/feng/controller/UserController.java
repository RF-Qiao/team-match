package com.feng.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.feng.common.BaseResponse;
import com.feng.common.ErrorCode;
import com.feng.common.ResultUtils;
import com.feng.constant.UserConstant;
import com.feng.exception.BusinessException;
import com.feng.pojo.User;
import com.feng.pojo.request.LoginUser;
import com.feng.pojo.request.RequestUser;
import com.feng.service.UserService;
import com.feng.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.feng.constant.UserConstant.*;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    /**
     * 注册
     * @param requestUser
     * @return
     */
    @PostMapping("/register")
    public BaseResponse register(@RequestBody RequestUser requestUser) {
        if (StringUtils.isAnyBlank(requestUser.getUserName(), requestUser.getUserPassword())) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String userName = requestUser.getUserName();
        String userPassword = requestUser.getUserPassword();
        String checkPassword = requestUser.getCheckPassword();
        User register = userService.register(userName, userPassword, checkPassword);
        String token = TokenUtils.token(register.getId(), register.getUserStatus());
        return ResultUtils.success("注册成功", token);
    }

    /**
     * 登录
     * @param loginUser
     * @return
     */
    @PostMapping("/login")
    public BaseResponse userLogin(@RequestBody LoginUser loginUser, HttpServletRequest request) {
        //是否为管理员
        if (StringUtils.isAnyBlank(loginUser.getUserName(), loginUser.getUserPassword())) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String userName = loginUser.getUserName();
        String userPassword = loginUser.getUserPassword();
        User user = userService.userLogin(userName, userPassword,request);
        String token = TokenUtils.token(user.getId(), user.getUserStatus());
        return ResultUtils.success(user, token);
    }

    /**
     * 查询
     * @param userName
     * @param request
     * @return
     */
    @GetMapping("/query")
    public BaseResponse query(String userName, HttpServletRequest request) {
        //是否为管理员
        userService.isAdmin(request);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("userName", userName);
        List<User> list = userService.list(wrapper);
        if (list.isEmpty()){
            throw  new BusinessException(ErrorCode.NULL_ERROR,"无用户信息");
        }
        return ResultUtils.success(list);
    }

    /**
     * 删除
     * @param userName
     * @param request
     * @return
     */
    @DeleteMapping("/delete")
    public BaseResponse delete(String userName, HttpServletRequest request) {
        //是否为管理员
        if (!userService.isadmin(request)){
            throw new BusinessException(ErrorCode.NOT_AUTH);
        }
        if (userName==null){
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        userService.deleteUserByUserName(userName);
        return ResultUtils.success("删除成功");
    }

    /**
     * \根据标签查询用户
     * @param tagNameList
     * @return
     */
    @PostMapping("/search/tags")
    public BaseResponse searchTags(@RequestBody  List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        List<User> users = userService.searchUserByTags(tagNameList);
        return ResultUtils.success(users);
    }
}

