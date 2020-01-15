package com.pface.admin.modules.base.service.impl;

import com.github.pagehelper.PageHelper;
import com.pface.admin.core.utils.DateUtils;
import com.pface.admin.core.utils.MyMapper;
import com.pface.admin.modules.front.vo.FaceAppUserPojo;
import com.pface.admin.modules.front.vo.FaceAppUsescenePojo;
import com.pface.admin.modules.member.po.FaceUser;
import com.pface.admin.modules.system.po.User;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.service.IService;
import tk.mybatis.mapper.code.Style;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import java.util.Date;
import java.util.List;

/**
 * @param <T>
 * @author cjbi,daniel.liu@outlook.com
 */
public abstract class BaseService<T> implements IService<T> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected MyMapper<T> mapper;

    public MyMapper<T> getMapper() {
        return mapper;
    }

    @Override
    public List<T> queryAll() {
        return mapper.selectAll();
    }

    @Override
    public List<T> queryList(T entity) {
        return mapper.select(entity);
    }

    @Override
    public T queryOne(T entity) {
        return mapper.selectOne(entity);
    }

    @Override
    public T queryById(Object id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<T> queryList(T entity, PageQuery pageQuery) {
        if (pageQuery.getOrderBy() != null) {
            pageQuery.setOrderBy(StringUtil.convertByStyle(pageQuery.getOrderBy(), Style.camelhump));
        }
        return PageHelper.startPage(pageQuery)
                .doSelectPage(() -> mapper.select(entity));
    }

    @Override
    public List<T> queryListByExample(Example example){
        return mapper.selectByExample(example);
    }

    @Override
    public List<T> queryList(PageQuery pageQuery,Condition condition) {
        if (pageQuery.getOrderBy() != null) {
            pageQuery.setOrderBy(StringUtil.convertByStyle(pageQuery.getOrderBy(), Style.camelhump));
        }

        return PageHelper.startPage(pageQuery)
                .doSelectPage(() -> mapper.selectByCondition(condition));
    }

    @Override
    public List<T> queryList(PageQuery pageQuery,Example example) {
        if (pageQuery.getOrderBy() != null) {
            pageQuery.setOrderBy(StringUtil.convertByStyle(pageQuery.getOrderBy(), Style.camelhump));
        }
     // List<T> rs=  mapper.selectByExample(example);
        return PageHelper.startPage(pageQuery)
                .doSelectPage(() -> mapper.selectByExample(example));
    }

    @Override
    public int create(T entity) {
        return mapper.insertSelective(entity);
    }

    @Override
    public int updateAll(T entity) {
        return mapper.updateByPrimaryKey(entity);
    }

    @Override
    public int updateNotNull(T entity) {
        return mapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int delete(T entity) {
        return mapper.delete(entity);
    }

    @Override
    public int deleteById(Object id) {
        return mapper.deleteByPrimaryKey(id);
    }

    @Override
    public int count(T entity) {
        return mapper.selectCount(entity);
    }

    @Override
    public void logicDeleteBatchByIds(Long[] ids){
    }
    //face前端使用
    @Override
    public void logicDeleteBatchByIds(Long[] ids, FaceAppUserPojo userinfo){}
}
