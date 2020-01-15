package com.pface.admin.modules.system.mapper;

import com.pface.admin.core.utils.MyMapper;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.member.vo.UserListVo;
import com.pface.admin.modules.system.po.User;

import java.util.List;

public interface UserMapper extends MyMapper<User> {

    List<UserListVo> queryBackUsers(UserListVo vo);

    List<UserListVo> queryFrontUsers(UserListVo vo);

    List<UserListVo> queryAllUsers(UserListVo vo);

}