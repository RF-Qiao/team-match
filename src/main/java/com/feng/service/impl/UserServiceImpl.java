package com.feng.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.common.ErrorCode;
import com.feng.exception.BusinessException;
import com.feng.mapper.UserMapper;
import com.feng.pojo.User;
import com.feng.service.UserService;
import com.feng.utils.MD5;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
    //正则表达式
    public static final String DEFAULT_QUERY_REGEX = "[!$^&*+=|{}';'\",<>/?~！#￥%……&*——|{}【】‘；：”“'。，、？]";

    @Autowired
    private UserMapper userMapper;

    @Override
    public User userLogin(String userName,String userPassword) {
     if (StringUtils.isAnyBlank(userName,userPassword)){
         throw new BusinessException(ErrorCode.NULL_ERROR,"用户名或密码为空");
     }
    if (Pattern.compile(DEFAULT_QUERY_REGEX).matcher(userName).find()){
        throw new BusinessException(ErrorCode.PARAM_ERROR, "用户名账号存在特殊字符");
    }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userName",userName);
        queryWrapper.eq("userPassword",MD5.md5(userPassword));
         if (!userMapper.exists(queryWrapper)){
             throw new BusinessException(ErrorCode.PARAM_ERROR,"用户名或密码错误");
         }
         return getSafetyUser(userMapper.selectOne(queryWrapper));
    }

    @Override
    public User getSafetyUser(User suser) {
        User user = new User();
        user.setUserName(suser.getUserName());
        user.setGender(suser.getGender());
        user.setEmail(suser.getEmail());
        user.setUserStatus(suser.getUserStatus());
        user.setPhone(suser.getPhone());
        user.setCreateTime(suser.getCreateTime());
        return user;
    }


    @Override
    public User register(String userName, String userPassword, String checkPassword) {
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(userPassword)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "参数为空");
        }
        if (userName.length() < 4) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户名过短");
        }
        if (userPassword.length() < 6) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户名密码过短");
        }
        //用户名账号不含特殊字符
        Matcher matcher = Pattern.compile(DEFAULT_QUERY_REGEX).matcher(userName);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户名账号存在特殊字符");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "二次密码不一致");
        }
        //用户名是否存在
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        QueryWrapper<User> userName1 = wrapper.eq("userName", userName);
        Boolean count = userMapper.exists(userName1);
            if (count) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "用户名存在 请重新注入");
            }
            //注册
            User user = new User();
            user.setUserName(userName);
            user.setUserPassword(MD5.md5(userPassword));
            userMapper.insert(user);
            return user;
    }


}
