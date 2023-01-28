package com.feng.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.feng.common.BaseResponse;
import com.feng.common.ErrorCode;
import com.feng.common.ResultUtils;
import com.feng.exception.BusinessException;
import com.feng.pojo.User;
import com.feng.pojo.request.LoginUser;
import com.feng.pojo.request.RequestUser;
import com.feng.service.UserService;
import com.feng.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 注册
     *
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
     *
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
        User user = userService.userLogin(userName, userPassword, request);
        String token = TokenUtils.token(user.getId(), user.getUserStatus());
        return ResultUtils.success(user, token);
    }

    /**
     * 查询
     *
     * @param userName
     * @param request
     * @return
     */
    @GetMapping("/query")
    public BaseResponse query(String userName, HttpServletRequest request) {
        //是否为管理员
        if (!userService.isadmin(request)){
            throw new BusinessException(ErrorCode.NOT_AUTH);
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("userName", userName);
        List<User> list = userService.list(wrapper);
        if (list.isEmpty()) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "无用户信息");
        }
        List<User> userList = list.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(userList);
    }

    /**
     * 删除
     *
     * @param userName
     * @param request
     * @return
     */
    @DeleteMapping("/delete")
    public BaseResponse delete(String userName, HttpServletRequest request) {
        //是否为管理员
        if (!userService.isadmin(request)) {
            throw new BusinessException(ErrorCode.NOT_AUTH);
        }
        if (userName == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        userService.deleteUserByUserName(userName);
        return ResultUtils.success("删除成功");
    }

    /**
     * \根据标签查询用户
     *
     * @param tagNameList
     * @return
     */
    @PostMapping("/search/tags")
    public BaseResponse searchTags(@RequestBody List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        List<User> users = userService.searchUserByTags(tagNameList);
        return ResultUtils.success(users);
    }

    /**
     * 首页推荐用户
     * @param request
     * @return
     */
    @GetMapping("/recommend")
    public BaseResponse recommendUsers(HttpServletRequest request) {
        User loginUser = userService.loginUser(request);
        String redisKey = String.format("feng:user:recommend:%s", loginUser.getId());
        //如果有缓存直接读数据
        ValueOperations<String, Object> objectValueOperations = redisTemplate.opsForValue();
        Object result = objectValueOperations.get(redisKey);
        if (result!=null){
            return ResultUtils.success(result);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> userList = userService.list(queryWrapper);
        try {
            objectValueOperations.set(redisKey,userList,30000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("redis set key error", e);
        }
        return ResultUtils.success(userList);
    }

    /**
     * 获得自己信息
     * @param request
     * @return
     */
    @GetMapping("/getCurrentUser")
    public BaseResponse getCurrentUser(HttpServletRequest request) {
        if (request==null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        User currentUser = userService.getCurrentUser(request);
        return ResultUtils.success(currentUser);
    }

    /**
     * 用户登出
     * @param request
     * @return
     */
    @GetMapping("/userLogOut")
    public BaseResponse userLogOut( HttpServletRequest request) {
        if (request==null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        userService.userLogOut(request);

        return ResultUtils.success("退出成功");
    }

}