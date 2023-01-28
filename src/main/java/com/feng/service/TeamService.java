package com.feng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feng.pojo.Team;
import com.feng.pojo.User;
import com.feng.pojo.request.TeamQuery;
import com.feng.pojo.vo.TeamUserVO;

import java.util.List;

/**
* @author fengfeng
*
*/
public interface TeamService extends IService<Team> {
    /**
     * 创建队伍
     * @param team
     * @param loginUser
     * @return
     */
    int addTeam(Team team, User loginUser);

    /**
     * 查询队伍
     * @param teamQuery
     * @param isadmin
     * @return
     */
    List<TeamUserVO> searchTeams(TeamQuery teamQuery, Boolean isadmin);
}
