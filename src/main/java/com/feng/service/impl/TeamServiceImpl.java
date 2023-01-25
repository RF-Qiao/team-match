package com.feng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.pojo.Team;
import com.feng.service.TeamService;
import com.feng.mapper.TeamMapper;
import org.springframework.stereotype.Service;

/**
* @author fengfeng
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2023-01-25 13:44:48
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{

}




