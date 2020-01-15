package com.pface.admin.modules.system.service;

import com.pface.admin.core.exception.BizException;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.service.IService;
import com.pface.admin.modules.member.vo.UserListVo;
import com.pface.admin.modules.system.po.User;

import java.util.List;
import java.util.Set;

public interface UserService extends IService<User> {

    /**
     * 创建用户
     * @param user
     */
    void createUser(User user) throws BizException;

    /**
     * 修改密码
     * @param userId
     * @param newPassword
     */
    void changePassword(Long userId, String newPassword);

    /**
     * 根据用户名查找其角色
     * @param username
     * @return
     */
    Set<String> queryRoles(String username);

    /**
     * 根据用户名查找其权限
     * @param username
     * @return
     */
    Set<String> queryPermissions(String username);

    List<UserListVo> queryBackUsers(PageQuery pageQuery, UserListVo vo);

    List<UserListVo> queryFrontUsers(PageQuery pageQuery,UserListVo vo);

    List<UserListVo> queryAllUsers(PageQuery pageQuery,UserListVo vo);

}