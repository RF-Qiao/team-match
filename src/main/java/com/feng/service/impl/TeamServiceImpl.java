package com.feng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feng.common.ErrorCode;
import com.feng.constant.TeamStatusConstant;
import com.feng.exception.BusinessException;
import com.feng.mapper.TeamMapper;
import com.feng.pojo.Team;
import com.feng.pojo.User;
import com.feng.pojo.UserTeam;
import com.feng.pojo.request.TeamQuery;
import com.feng.pojo.request.TeamUpdateRequest;
import com.feng.pojo.vo.TeamUserVO;
import com.feng.pojo.vo.UserVO;
import com.feng.service.TeamService;
import com.feng.service.UserService;
import com.feng.service.UserTeamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.feng.constant.UserConstant.ADMIN_ROLE;

/**
 * @author fengfeng
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {

    @Resource
    private UserService userService;
    @Resource
    private UserTeamService userTeamService;
    @Resource
    private TeamMapper teamMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addTeam(Team team, User loginUser) {

        //1.请求参数是否为空？
        if (team == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        //2.用户是否登录，未登录不允许创建
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "用户未登录");
        }
        //3.校验信息，队伍人数>1 且<20
        Integer maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if (maxNum < 1 || maxNum > 20) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "队伍人数不满足要求");
        }
        //4.队伍标题小于20
        if (team.getName().length() > 20 || StringUtils.isBlank(team.getName())) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "队伍标题不满足要求");
        }
        //5.描述<512
        if (team.getDescription().length() >= 512 || StringUtils.isBlank(team.getDescription())) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "描述过长不满足要求");
        }
        //6.status 是否公开（int）传默认为 0（公开）
        Integer status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusConstant statusEnum = TeamStatusConstant.getTeamKey(status);
        if (statusEnum == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "队伍状态不满足要求");
        }
        //7.如果为加密状态，一定要有密码，且密码<=32
        String teamPassword = team.getPassword();
        if (TeamStatusConstant.SECRET.equals(statusEnum)) {
            if (teamPassword == null || teamPassword.length() < 32) {
                throw new BusinessException(ErrorCode.NULL_ERROR, "密码不满足要求");
            }
        }
        //8.超长时间>现在时间
        Date expireTime = team.getExpireTime();
        if (new Date().after(expireTime)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "超时时间 > 当前时间");
        }
        //9.校验用户最多创建5个队伍
        final int id = loginUser.getId();
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        QueryWrapper<Team> userId = queryWrapper.eq("userId", id);

        long userid = this.count(userId);
        if (userid >= 5) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户最多创建5个用户");
        }
        //10.插入队伍信息到队伍表
        team.setId(null);
        team.setUserId(id);
        Integer insert = teamMapper.insert(team);
        //  boolean save = this.save(team);
        Integer teamId = team.getId();
        if (insert == null || teamId == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "创建队伍失败");
        }
        //11.插入用户队伍关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(id);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());
        boolean result = userTeamService.save(userTeam);
        if (!result) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "创建队伍失败");
        }
        return teamId;
    }

    @Override
    public List<TeamUserVO> searchTeams(TeamQuery teamQuery, Boolean isadmin) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAM_ERROR);
        }
        //  1. 从请求参数中取出队伍名称等查询条件，如果存在则作为查询条件
        QueryWrapper<Team> wrapper = new QueryWrapper<>();
        String name = teamQuery.getName();
        if (!StringUtils.isBlank(name)) {
            wrapper.like("name", name);
        }
        //2. 不展示已过期的队伍（根据过期时间筛选）   ge小于
        wrapper.and(qw -> qw.ge("expireTime", new Date())).or().isNull("expireTime");
        List<Team> teamList = this.list(wrapper);
        if (teamList == null) {
            return new ArrayList<>();
        }
        //3. 可以通过某个关键词同时对名称和描述查询

        //4. 只有管理员才能查看加密还有非公开的房间
        Integer teamQueryStatus = teamQuery.getStatus();
        TeamStatusConstant teamKey = TeamStatusConstant.getTeamKey(teamQueryStatus);
        if (teamKey == null) {
            teamKey = TeamStatusConstant.PUBLIC;
        }
        if (!isadmin && teamKey.equals(TeamStatusConstant.PRIVATE)) {
            throw new BusinessException(ErrorCode.NOT_AUTH);
        }
        ArrayList<TeamUserVO> teamUserVOList = new ArrayList<>();
        //5. 关联查询已加入队伍的用户信息
        for (Team team : teamList) {
            Integer userId = team.getUserId();
            if (userId == null) {
                continue;
            }
            User user = userService.getById(userId);
            TeamUserVO teamUserVO = new TeamUserVO();
            BeanUtils.copyProperties(team, teamUserVO);
            //用户脱敏
            if (user != null) {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(user, userVO);
                teamUserVO.setCreateUser(userVO);
            }
            teamUserVOList.add(teamUserVO);
        }
        return teamUserVOList;
    }


    @Override
    public Boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser) {
//        1. 判断请求参数是否为空
        if (teamUpdateRequest == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
//        2. 查询队伍是否存在
        QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
        QueryWrapper<Team> wrapper = queryWrapper.eq("id", teamUpdateRequest.getId());
        if (count(wrapper) == 0) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "队伍不存在");
        }
//        3. 只有管理员或者队伍的创建者可以修改
        if (loginUser.getUserStatus() != ADMIN_ROLE && teamUpdateRequest.getUserId() != loginUser.getId()) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "权限不足，不可修改");
        }
//        4. 如果用户传入的新值和老值一致，就不用 update 了（可自行实现，降低数据库使用次数）

//        5. **如果队伍状态改为加密，必须要有密码**
        if (teamUpdateRequest.getStatus() == TeamStatusConstant.SECRET.getKey()) {
            if (teamUpdateRequest.getPassword() == null) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "加密房间必须要设置密码");
            }
        }
//        6. 更新成功
        Team upteTeam = new Team();
        BeanUtils.copyProperties(teamUpdateRequest, upteTeam);
        return this.updateById(upteTeam);

    }

    @Override
    public Team getTeamById(Long teamId){
        if (teamId== null){
            throw  new BusinessException(ErrorCode.PARAM_ERROR,"请求为空");
        }
        Team team = this.getById(teamId);
        if (team==null){
            throw new BusinessException(ErrorCode.PARAM_ERROR,"不存在队伍信息");
        }
        return team;
    }

    @Override
    public boolean deleteTeam(Long id, User loginUser){
        if (id==null || loginUser==null){
            throw new BusinessException(ErrorCode.PARAM_ERROR,"用户为空");
        }
        //校验是否为当前队伍
        Team team = getTeamById(id);
        Integer teamId= team.getId();
        //校验是否为队长
        if (team.getUserId()!=loginUser.getId()){
            throw new BusinessException(ErrorCode.PARAM_ERROR,"无权限");
        }
        //移除所有队伍信息
        QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
         queryWrapper.eq("teamId",teamId);
        //移除队伍关系信息
        boolean remove = userTeamService.remove(queryWrapper);
        if (!remove){
            throw new BusinessException(ErrorCode.PARAM_ERROR,"删除队伍关系表失败");
        }
        return this.removeById(teamId);
    }


}