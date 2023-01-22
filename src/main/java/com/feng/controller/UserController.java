package com.feng.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.feng.common.BaseResponse;
import com.feng.common.ErrorCode;
import com.feng.common.ResultUtils;
import com.feng.exception.BusinessException;
import com.feng.pojo.User;
import com.feng.pojo.vo.LoginUser;
import com.feng.pojo.vo.RequestUser;
import com.feng.service.UserService;
import com.feng.utils.TokenUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

import static com.feng.constant.UserConstant.DEFAULT_ROLE;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
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
        String header = request.getHeader("HttpServletRequest");
        if (StringUtils.isAnyBlank(loginUser.getUserName(), loginUser.getUserPassword())) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String userName = loginUser.getUserName();
        String userPassword = loginUser.getUserPassword();
        User user = userService.userLogin(userName, userPassword);
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
        isAdmin(request);
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
        isAdmin(request);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userName", userName);
        userService.remove(wrapper);
        return ResultUtils.success("删除成功");
    }

    /**
     * \根据标签查询用户
     * @param tagNameList
     * @param request
     * @return
     */
    @PostMapping("/search/tags")
    public BaseResponse searchTags(@RequestBody  List<String> tagNameList, HttpServletRequest request) {
        //是否为管理员
        // isAdmin(request);
        List<User> users = userService.searchUserByTags(tagNameList);
        return ResultUtils.success(users);
    }

    /**
     * 是否为管理员
     * @param request
     */
    public void isAdmin(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String substring = header.substring(7);
        Integer verify = TokenUtils.verify(substring);
        if (verify==DEFAULT_ROLE){
            throw  new BusinessException(ErrorCode.NOT_AUTH,"不是管理员无法查看");
        }
        if (new Date().compareTo(TokenUtils.dataDecode(substring)) > 0){
            throw  new BusinessException(ErrorCode.NOT_AUTH,"token过期 请重新登录");
        }
    }
}

