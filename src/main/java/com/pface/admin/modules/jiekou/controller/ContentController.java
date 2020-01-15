package com.pface.admin.modules.jiekou.controller;

import com.github.pagehelper.Page;
import com.pface.admin.core.exception.BizException;
import com.pface.admin.core.utils.ResultCodeEnum;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.base.query.JsonResult;
import com.pface.admin.modules.base.query.JsonResultUtils;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.front.vo.MemberMediaPojo;
import com.pface.admin.modules.front.vo.MemberMediaQueryParams;
import com.pface.admin.modules.jiekou.pojo.ContentListParams;
import com.pface.admin.modules.jiekou.pojo.JmgoMediaFileRetInfo;
import com.pface.admin.modules.jiekou.pojo.JmgoMemberUserParams;
import com.pface.admin.modules.member.po.MemberMedia;
import com.pface.admin.modules.member.po.MemberUser;
import com.pface.admin.modules.member.service.MemMediaFileService;
import com.pface.admin.modules.member.service.MemberUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(tags= "内容相关接口", description = "媒体内容获取", value = "ddd看看")
@RestController
@RequestMapping("/appapi/content")
public class ContentController {

    @Autowired
    private MemberUserService memberUserService;
    @Autowired
    private MemMediaFileService mediaFileService;

    @ApiOperation(value = "内容查询接口", position = 1, produces = "ww", notes = "内容查询接口", httpMethod = "GET")
    @ApiImplicitParams({
    })
    @RequestMapping("/list")
    @ResponseBody
    public JsonResult<List<MemberMedia>> list(JmgoMemberUserParams userInfo, PageQuery pageQuery, ContentListParams contentListParams) {
        MemberUser memberUser = getMemberUser(userInfo);

        MemberMediaQueryParams params = new MemberMediaQueryParams();
        params.setUid(memberUser.getId());

        if (pageQuery.getPageSizeKey() <= 0) {
            pageQuery.setPageSizeKey(10);
        }
        if (pageQuery.getPageNumKey() <= 0) {
            pageQuery.setPageNumKey(1);
        }

        Page<MemberMediaPojo> memberMediaPage = (Page<MemberMediaPojo>) mediaFileService.queryMemberMediaPageList(pageQuery, params);

        return JsonResultUtils.produceJsonResult(userInfo, 1, "操作成功。", memberMediaPage);
    }


    @ApiOperation(value = "获得文件地址", position = 2, produces = "ww", notes = "获得文件地址", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contentId", value = "内容id", required = true, paramType = "query", dataType = "String", dataTypeClass = Integer.class),
    })
    @RequestMapping("/getMediaFileAddr")
    @ResponseBody
    public JsonResult<JmgoMediaFileRetInfo> getMediaFileAddr(JmgoMemberUserParams userInfo, Integer contentId) {
        MemberUser memberUser = getMemberUser(userInfo);
        //


        //
        JmgoMediaFileRetInfo retInfo = new JmgoMediaFileRetInfo();
        return JsonResultUtils.produceJsonResult(userInfo, 1, "操作成功。", retInfo);

    }


    /**
     * 登陆鉴权，成功返回登陆用户实体
     *
     * @param userInfo
     * @return
     */
    @NotNull
    public MemberUser getMemberUser(JmgoMemberUserParams userInfo) {
        if (userInfo == null) {
            throw new BizException(ResultCodeEnum.UNAUTHORIZED);
        }
        if (StringUtils.isEmpty(userInfo.getGroupId())) {
            throw new BizException(ResultCodeEnum.PARAM_ERROR);
        }
        if (StringUtils.isEmpty(userInfo.getAppId())) {
            throw new BizException(ResultCodeEnum.PARAM_ERROR);
        }
        if (StringUtils.isEmpty(userInfo.getAppSecret())) {
            throw new BizException(ResultCodeEnum.PARAM_ERROR);
        }
        MemberUser memberUser_cond = new MemberUser();
        memberUser_cond.setMobile(userInfo.getAppId());
        memberUser_cond.setPwd(userInfo.getAppSecret());
        MemberUser memberUser = memberUserService.queryOne(memberUser_cond);
        if (memberUser == null) {
            throw new BizException(ResultCodeEnum.UNAUTHORIZED);
        }
        return memberUser;
    }
}
