package com.pface.admin.modules.front.web;

import com.pface.admin.core.annotation.SystemLog;
import com.pface.admin.core.utils.Result;
import com.pface.admin.modules.base.web.BaseCrudController;
import com.pface.admin.modules.front.vo.GoodsVo;
import com.pface.admin.modules.member.enums.MediaTypeEnum;
import com.pface.admin.modules.member.po.MemberCert;
import com.pface.admin.modules.member.po.MemberUser;
import com.pface.admin.modules.member.service.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/7/2
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
@Controller
@RequestMapping("/front/content")
public class FrontContentController extends BaseCrudController<MemberCert> {

    @Autowired
    private MemberUserService memberService;

    @Autowired
    private MemberCertService certService;

    @Autowired
    private MemMediaFileService mediaFileService;

    @Autowired
    private MemberMediaService memberMediaService;

    @Autowired
    private MediaDocImgService mediaDocImgService;



    @ResponseBody
    @PostMapping("/logic-delete-batch")
    @SystemLog("商品删除")
    @ApiOperation("删除多条数据")
    public Result logicDeleteBatchByIds(@NotNull @RequestParam("id")  String[] ids) {

        List<GoodsVo> vedioAudioList=new ArrayList<GoodsVo>();
        //List<Long> imageTextList=new ArrayList<Long>();
        for(int i=0;ids!=null && i<ids.length;i++){
          String str=ids[i];
          int idx= str.indexOf("@");
          String id=str.substring(0,idx);
          String type=str.substring(idx);
            vedioAudioList.add(new GoodsVo(Long.valueOf(id),type));
          /*if(str.contains(MediaTypeEnum.IMAGETEXT.getName())){
              imageTextList.add(Long.valueOf(id));
          }else{
              vedioAudioList.add(Long.valueOf(id));
          }*/
          //GoodsVo vo=new GoodsVo();
        }
        mediaFileService.batchDel(vedioAudioList);
        //Long[] vehicleIds =Arrays.stream(ids).map(p -> Long.valueOf(p.toString())).toArray(Long[]::new);
        //memPriceLabService.logicDeleteBatchByIds(vehicleIds);
        return Result.success();
    }


    @ResponseBody
    @PostMapping("/goodsOn-batch")
    @SystemLog("商品上架")
    @ApiOperation("商品上架")
    public Result goodsOnBatchByIds(@NotNull @RequestParam("id")  String[] ids) {
        List<GoodsVo> vedioAudioList=new ArrayList<GoodsVo>();

//        ${mediaFile.mediaType}+','+${mediaFile.mediaId}+','+${mediaFile.id}
        if (ids!=null && ids.length>0) {
            for (String id : ids) {
                String[] idArray = id.split("#");
                vedioAudioList.add(new GoodsVo(Long.valueOf(idArray[2]), idArray[0]));
            }
        }
//        for(int i=0;ids!=null && i<ids.length;i++){
//            String str=ids[i];
//            int idx= str.indexOf(",");
//            String id=str.substring(0,idx);
//            String type=str.substring(idx);
//            vedioAudioList.add(new GoodsVo(Long.valueOf(id),type));
//        }
        mediaFileService.batchGoodsOn(vedioAudioList);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/goodsOff-batch")
    @SystemLog("商品下架")
    @ApiOperation("商品下架")
    public Result goodsOffBatchByIds(@NotNull @RequestParam("id")  String[] ids) {
        List<GoodsVo> vedioAudioList=new ArrayList<GoodsVo>();

        if (ids!=null && ids.length>0) {
            for (String id : ids) {
                String[] idArray = id.split("#");
                vedioAudioList.add(new GoodsVo(Long.valueOf(idArray[2]), idArray[0]));
            }
        }
//
//        for(int i=0;ids!=null && i<ids.length;i++){
//            String str=ids[i];
//            int idx= str.indexOf("@");
//            String id=str.substring(0,idx);
//            String type=str.substring(idx);
//            vedioAudioList.add(new GoodsVo(Long.valueOf(id),type));
//        }
        mediaFileService.batchGoodsOff(vedioAudioList);
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/logic-delete")
    @SystemLog("价格标签删除")
    @ApiOperation("删除多条数据")
    public Result logicDeleteByIds(@NotNull @RequestParam("id")  String[] ids) {

        Long[] vehicleIds =Arrays.stream(ids).map(p -> Long.valueOf(p.toString())).toArray(Long[]::new);
        mediaFileService.logicDeleteBatchByIds(vehicleIds);
        return Result.success();
    }



    @ResponseBody
    @PostMapping("/physical-delete-batch")
    @SystemLog("商品删除")
    @ApiOperation("删除多条数据")
    public Result physicalDeleteBatchByIds(@NotNull @RequestParam("id")  String[] ids,HttpServletRequest request) {

        HttpSession session = request.getSession(true);
        MemberUser user = (MemberUser)session.getAttribute(MemberUser.USER_SESSION_KEY);
        for (String id:ids){
            String[] idArray = id.split("#");
            mediaFileService.physicalDeleteFileAndDb(idArray[0], idArray[1], idArray[2],user);
        }
        return Result.success();
    }

    @ResponseBody
    @PostMapping("/physical-delete")
    @SystemLog("价格标签删除")
    @ApiOperation("删除多条数据")
    public Result physicalDeleteByIds(@NotNull @RequestParam("id")  String[] ids, Model model, HttpServletRequest request) {

        HttpSession session = request.getSession(true);
        MemberUser user = (MemberUser)session.getAttribute(MemberUser.USER_SESSION_KEY);

        String mediaType=ids[0], mediaId=ids[1], mediaFileId=ids[2];
        mediaFileService.physicalDeleteFileAndDb(mediaType, mediaId, mediaFileId, user);
        return Result.success();
    }
}
