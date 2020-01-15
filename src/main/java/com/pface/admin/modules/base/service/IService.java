package com.pface.admin.modules.base.service;

import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.front.vo.FaceAppUserPojo;
import com.pface.admin.modules.front.vo.FaceAppUsescenePojo;
import com.pface.admin.modules.member.po.FaceSensebox;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 通用接口
 *
 * @param <T>
 * @author cjbi,daniel.liu@outlook.com
 */
public interface IService<T> {

    List<T> queryAll();

    List<T> queryList(T entity);

    List<T> queryListByExample(Example example);

    T queryOne(T entity);

    T queryById(Object id);

    List<T> queryList(T entity, PageQuery pageQuery);

    List<T> queryList(PageQuery pageQuery,Condition condition);

    List<T> queryList(PageQuery pageQuery,Example example);

    int create(T entity);

    int updateAll(T entity);

    int updateNotNull(T entity);

    int delete(T entity);

    int deleteById(Object id);

    void logicDeleteBatchByIds(Long[] ids);
    void logicDeleteBatchByIds(Long[] ids, FaceAppUserPojo userinfo);//Face前端使用
    int count(T entity);

}
