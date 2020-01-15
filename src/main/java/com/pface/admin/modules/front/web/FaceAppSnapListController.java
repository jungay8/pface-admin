package com.pface.admin.modules.front.web;

import com.github.pagehelper.Page;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.base.query.PageQuery;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.front.constants.FaceAppConstant;
import com.pface.admin.modules.front.vo.FaceAppSnapListPojo;
import com.pface.admin.modules.front.vo.FaceAppSnapListQueryParams;
import com.pface.admin.modules.front.vo.FaceAppUserPojo;
import com.pface.admin.modules.front.vo.MemberMediaPojo;
import com.pface.admin.modules.member.po.FaceAppSnapList;
import com.pface.admin.modules.member.po.FaceAppUsescene;
import com.pface.admin.modules.member.service.FaceAppSnapListService;
import com.pface.admin.modules.member.service.FaceAppUsesceneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/front/appsnaplist")
public class FaceAppSnapListController  extends BaseCrudController<FaceAppSnapList> {
    @Autowired
    FaceAppSnapListService faceAppSnapListService;
    @Autowired
    FaceAppUsesceneService faceAppUsesceneService;

    @ModelAttribute("userinfo")
    public FaceAppUserPojo getUser(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        FaceAppUserPojo user = (FaceAppUserPojo) session.getAttribute(FaceAppConstant.USER_SESSION_KEY);
        return user;
    }

}
