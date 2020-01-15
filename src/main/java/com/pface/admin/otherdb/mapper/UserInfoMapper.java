package com.pface.admin.otherdb.mapper;

import com.pface.admin.core.utils.MyMapper;
import com.pface.admin.otherdb.po.UserInfo;
import org.apache.ibatis.annotations.SelectKey;

public interface UserInfoMapper extends MyMapper<UserInfo> {
      //https://www.cnblogs.com/duanxz/p/3862315.html
  //https://blog.csdn.net/qq_37676559/article/details/81735534
    void  save(UserInfo userInfo);

}