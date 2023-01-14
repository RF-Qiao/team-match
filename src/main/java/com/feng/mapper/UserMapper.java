package com.feng.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.feng.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @author fengfeng
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-01-11 18:00:15
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {


}
