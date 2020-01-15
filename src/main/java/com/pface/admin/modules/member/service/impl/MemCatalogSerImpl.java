package com.pface.admin.modules.member.service.impl;

import com.pface.admin.core.utils.Constants;
import com.pface.admin.modules.base.service.impl.BaseService;
import com.pface.admin.modules.member.mapper.MemberCatalogueMapper;
import com.pface.admin.modules.member.po.MemberCatalogue;
import com.pface.admin.modules.member.po.MemberCert;
import com.pface.admin.modules.member.po.MemberMediaFile;
import com.pface.admin.modules.member.service.MemCatalogService;
import com.pface.admin.modules.system.dto.TreeDto;
import com.pface.admin.modules.system.po.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.Weekend;
import tk.mybatis.mapper.weekend.WeekendCriteria;

import java.util.ArrayList;
import java.util.List;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/8
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
@Service
public class MemCatalogSerImpl extends BaseService<MemberCatalogue> implements MemCatalogService {


    @Autowired
    private MemberCatalogueMapper catalogueMapper;

    @Override
    @Transactional
    public void createCatalog(MemberCatalogue catalogue) {
        MemberCatalogue parent = catalogueMapper.selectOne(new MemberCatalogue().setId(catalogue.getParentId()));
        catalogue.setParentIds(parent.makeSelfAsParentIds());
        catalogue.setAvailable(true);
        catalogueMapper.insertSelective(catalogue);
    }

    @Override
    public List<TreeDto> queryCataTree(Long pId) {
        if (StringUtils.isEmpty(pId)) {
            pId = Constants.ORG_ROOT_ID;
        }
        List<TreeDto> tds = new ArrayList<>();
        Weekend example = Weekend.of(MemberCatalogue.class);
        WeekendCriteria<MemberCatalogue, Object> criteria = example.weekendCriteria();
        criteria.andEqualTo(MemberCatalogue::getParentId, pId);
        catalogueMapper.selectByExample(example).forEach(o -> tds.add(new TreeDto(o.getId(), o.getParentId(), o.getName(), Boolean.FALSE.equals(o.getLeaf()), o)));
        return tds;
    }

    @Override
    public List<MemberCatalogue> queryAllWithExclude(MemberCatalogue  exclude) {
        Weekend weekend = Weekend.of(MemberCatalogue.class);
        WeekendCriteria<MemberCatalogue, Object> criteria = weekend.weekendCriteria();
        criteria.andNotEqualTo(MemberCatalogue::getId, exclude.getId()).andNotLike(MemberCatalogue::getParentIds, exclude.makeSelfAsParentIds() + "%");
        return catalogueMapper.selectByExample(weekend);
    }

    @Override
    @Transactional
    public void move(MemberCatalogue source, MemberCatalogue target) {
        catalogueMapper.updateByPrimaryKeySelective(target);
        catalogueMapper.updateSalefParentIds(source.makeSelfAsParentIds());
    }

    @Override
    @Transactional
    public void logicDeleteBatchByIds(Long[] ids) {

        for(int i=0;i<ids.length;i++) {
            MemberCatalogue label = new MemberCatalogue();
            label.setId(ids[i]);
            label.setDelFlag(1);
            super.updateNotNull(label);
        }
    }

    @Override
    public List<MemberCatalogue> queryCataData(){
        Example example = new Example(MemberCatalogue.class);
        example.orderBy("priority").asc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("leaf", 1);//是叶子
        criteria.andEqualTo("parentId", 1);
        List<MemberCatalogue> cataloguesLabels = queryListByExample(example);
        return cataloguesLabels;
    }
}
