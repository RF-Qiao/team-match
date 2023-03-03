package com.feng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feng.pojo.Team;
import com.feng.pojo.User;
import com.feng.pojo.request.TeamQuery;
import com.feng.pojo.request.TeamUpdateRequest;
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

    /**
     * 更新队伍信息
     * @param teamUpdateRequest
     * @param loginUser
     * @return
     */

    Boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

    /**
     * 根据Id获取当前队伍信息
     * @param teamId
     * @return
     */
    Team getTeamById(Long teamId);

    /**
     * 根基id删除队伍
     * @param id
     * @param loginUser
     * @return
     */
    boolean deleteTeam(Long id, User loginUser);
}
