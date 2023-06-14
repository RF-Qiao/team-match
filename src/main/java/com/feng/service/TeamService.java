package com.feng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feng.model.domin.Team;
import com.feng.model.domin.User;
import com.feng.model.request.TeamJoinRequest;
import com.feng.model.dto.TeamQuery;
import com.feng.model.request.TeamQuitRequest;
import com.feng.model.request.TeamUpdateRequest;
import com.feng.model.vo.TeamUserVo;

import java.util.List;

/**
* @author fengfeng
*
*/
public interface TeamService extends IService<Team> {
    /**
     * 创建队伍
     *
     * @param team
     * @param loginUser
     * @return
     */
    long addTeam(Team team, User loginUser);

    /**
     * 搜索队伍
     *
     * @param teamQuery
     * @param isAdmin
     * @return
     */
    List<TeamUserVo> listTeams(TeamQuery teamQuery, boolean isAdmin);

    /**
     * 更新队伍
     *
     * @param teamUpdateRequest
     * @param loginUser
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

    /**
     * 加入队伍
     *
     * @param teamJoinRequest
     * @param loginUser
     * @return
     */
    Boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

    /**
     * 退出队伍
     *
     * @param teamQuitRequest
     * @param loginUser
     * @return
     */
    Boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    /**
     * 删除解散队伍
     *
     * @param id
     * @return
     */
    Boolean deleteTeam(Long id, User loginUser);

    /**
     * 通过id队伍
     *
     * @param id
     * @return
     */
    TeamUserVo getTeamById(long id, boolean isAdmin, User loginUser);


    List<Team> getTeamByUserId(Long userId);
}
