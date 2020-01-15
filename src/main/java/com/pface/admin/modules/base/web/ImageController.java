package com.pface.admin.modules.base.web;

import com.pface.admin.common.Global;
import com.pface.admin.core.utils.StringUtils;
import com.pface.admin.modules.member.po.JmgoFilePath;
import com.pface.admin.modules.member.po.MemberUser;
import com.pface.admin.modules.member.service.JmgoFilePathService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @copyright 刘冬
 * @创建人 liudong
 * @创建时间 2019/6/28
 * @描述
 * @旁白 命诚可贵, 爱情价更高, 若为自由故, 两者皆可抛
 */
@Slf4j
@Controller
@RequestMapping("/image")
public class ImageController  extends BaseController{

//    @Value("${pface.upload.dir}")
//    private String uploadDir;

    @Value("${face.uploaddir}")
    private String uploaddir;
    @Autowired
    private JmgoFilePathService jmgoFilePathService;

    @RequestMapping(value={""},method = {RequestMethod.GET})
  public String imageView(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String url=req.getParameter("url");
//        String filename=req.getParameter("filename");
        String action=req.getParameter("action");
       /* String filepath = req.getRequestURI();
        int index = filepath.indexOf(Global.IMAGE_BASE_URL);
        if(index >= 0) {
            filepath = filepath.substring(index + Global.IMAGE_BASE_URL.length());
        }
        filepath = UriUtils.decode(filepath, "UTF-8");*/

//        HttpSession session = req.getSession(true);
//        MemberUser user = (MemberUser)session.getAttribute(MemberUser.USER_SESSION_KEY);
//        String filepath= "";
//        if(user!=null){
//            filepath=String.valueOf(user.getId())+File.separator;
//        }

//        JmgoFilePath jmgoFilePath = jmgoFilePathService.getCurrentFP();
//        if(jmgoFilePath==null){
//            log.debug("上传路径没有配置！请先配置好表：jmgo_file_path");
//        }else {
//            String dir=jmgoFilePath.getVirtualpath();
//            String fileRealPath = dir + Global.USERFILES_BASE_URL +File.separator + filepath + filename;
              String fileRealPath = uploaddir +File.separator;
            if (org.apache.commons.lang3.StringUtils.isNotBlank(url)) {
                fileRealPath = URLDecoder.decode(url, "UTF-8");
            }
            File file = new File(fileRealPath);
//            if(org.apache.commons.lang3.StringUtils.isEmpty(filename)){
//                filename=file.getName();
//            }
            OutputStream out = resp.getOutputStream();
            try {

                if ("down".equals(action)) {
                    resp.reset();
                    resp.setContentType("application/octet-stream; charset=utf-8");
                    resp.setHeader("Content-Disposition", "attachment; filename="+new String(file.getName().getBytes("UTF-8"),"iso-8859-1"));
                    IOUtils.copy(new FileInputStream(file),out);
                    out.flush();
                }else{
                    resp.reset();
                    resp.setHeader("Content-Type", "application/octet-stream; charset=utf-8");
                    resp.addHeader("Pargam", "no-cache");
                    resp.addHeader("Cache-Control", "no-cache");
                    FileCopyUtils.copy(new FileInputStream(file), out);
                    out.flush();
                }
                //关闭响应输出流
                out.close();
                return null;
            } catch (FileNotFoundException e) {
                //得到向客户端输出文本的对象
                resp.setContentType("text/html;charset=UTF-8");
                out.write("无法打开文件!".getBytes());
                out.close();
            } catch (IOException e) {}
//        }
      return null;
  }

    public  String processFileName(String fileName, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String userAgent = request.getHeader("USER-AGENT");
        log.info("获取的Agent为：：：：：：：{}",userAgent);
        /*try {
            if(StringUtils.contains(userAgent, "MSIE")){//IE浏览器
                fileName = URLEncoder.encode(fileName,"UTF8");
            }else if(StringUtils.contains(userAgent, "Mozilla")){//google,火狐浏览器
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            }else{
                fileName = URLEncoder.encode(fileName,"UTF8");//其他浏览器
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        return new String(fileName.getBytes("UTF-8"),"iso-8859-1");
    }



}
