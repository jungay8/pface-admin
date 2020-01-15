package com.pface.admin.modules.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.pface.admin.core.utils.DateUtils;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.jiekou.Constants.Faceconstant;
import com.pface.admin.modules.member.vo.UserListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pface.admin.core.exception.BizException;
import com.pface.admin.core.utils.ResultCodeEnum;
import com.pface.admin.modules.base.service.impl.BaseService;
import com.pface.admin.modules.system.mapper.UserMapper;
import com.pface.admin.modules.system.po.User;
import com.pface.admin.modules.system.service.PasswordHelper;
import com.pface.admin.modules.system.service.RoleService;
import com.pface.admin.modules.system.service.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends BaseService<User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordHelper passwordHelper;

    @Override
    @Transactional
    public void createUser(User user) {
        User u = userMapper.selectOne(new User().setUsername(user.getUsername()));
        if (u != null) {
            throw new BizException(ResultCodeEnum.FAILED_USER_ALREADY_EXIST);
        }

        user.setPassword(Faceconstant.sysuser_password_default);  //创建系统用户的默认密码
        user.setRoleIds(Faceconstant.sysuser_roleids_default);
        user.setOrganizationId(Faceconstant.sysuser_organizationId_default);
        user.setGroupIds(Faceconstant.sysuser_groupIds_default);
        user.setOpDate(DateUtils.getNowDate());
        // 加密密码
        passwordHelper.encryptPassword(user);
        userMapper.insertSelective(user);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String newPassword) {
        User user = userMapper.selectByPrimaryKey(userId);
        user.setPassword(newPassword);
        passwordHelper.encryptPassword(user);
        userMapper.updateByPrimaryKey(user);
    }

    @Override
    public Set<String> queryRoles(String username) {
        User user = userMapper.selectOne(new User().setUsername(username));
        if (user == null) {
            return Collections.EMPTY_SET;
        }
        return roleService.queryRoles(
                Arrays.asList(user.getRoleIds().split(",")).stream().map(Long::valueOf).collect(Collectors.toList()).toArray(new Long[0])
        );
    }

    @Override
    public Set<String> queryPermissions(String username) {
        User user = userMapper.selectOne(new User().setUsername(username));
        if (user == null) {
            return Collections.EMPTY_SET;
        }
        return roleService.queryPermissions(
                Arrays.asList(user.getRoleIds().split(",")).stream().map(Long::valueOf).collect(Collectors.toList()).toArray(new Long[0])
        );
    }

    @Override
    public List<UserListVo> queryBackUsers(PageQuery pageQuery, UserListVo vo) {
        return PageHelper.startPage(pageQuery)
                .doSelectPage(() -> userMapper.queryBackUsers(vo));
    }

    @Override
    public List<UserListVo> queryFrontUsers(PageQuery pageQuery, UserListVo vo) {
        return PageHelper.startPage(pageQuery)
                .doSelectPage(() -> userMapper.queryFrontUsers(vo));
    }

    @Override
    public List<UserListVo> queryAllUsers(PageQuery pageQuery, UserListVo vo) {
        return PageHelper.startPage(pageQuery)
                .doSelectPage(() -> userMapper.queryAllUsers(vo));
    }
}
