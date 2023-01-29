package com.feng.controller;

import com.feng.common.BaseResponse;
import com.feng.common.ErrorCode;
import com.feng.common.ResultUtils;
import com.feng.exception.BusinessException;
import com.feng.pojo.Team;
import com.feng.pojo.User;
import com.feng.pojo.request.TeamQuery;
import com.feng.pojo.request.TeamRequest;
import com.feng.pojo.request.TeamUpdateRequest;
import com.feng.pojo.vo.TeamUserVO;
import com.feng.service.TeamService;
import com.feng.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/team")
@Slf4j
public class TeamController {
    @Resource
    private UserService userService;
    @Resource
    private TeamService teamService;

    /**
     * 创建队伍
     *
     * @param teamRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse addTeam(@RequestBody TeamRequest teamRequest, HttpServletRequest request) {
        if (teamRequest == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        User loginUser = userService.getCurrentUser(request);
        Team team = new Team();
        BeanUtils.copyProperties(teamRequest, team);
        int teamId = teamService.addTeam(team, loginUser);
        return ResultUtils.success(teamId);
    }

    /**
     * 查询队伍
     *
     * @param teamQuery
     * @param request
     * @return
     */
    @GetMapping("/list")
    public BaseResponse listTeams( TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        boolean isadmin = userService.isadmin(request);
        List<TeamUserVO> teamUserVOList = teamService.searchTeams(teamQuery, isadmin);
        return ResultUtils.success(teamUserVOList);
    }

    /**
     * 查询队伍
     *
     * @param teamUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest, HttpServletRequest request) {
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        User currentUser = userService.getCurrentUser(request);
        Boolean result = teamService.updateTeam(teamUpdateRequest, currentUser);
        if (!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更新失败");
        }
        return ResultUtils.success(result);
    }
}