package com.pface.admin.modules.member.web;
//
//import com.pface.admin.common.MultipartFileParam;
//import com.pface.admin.core.utils.JedisUtils;
//import com.pface.admin.core.utils.Result;
//import com.pface.admin.core.utils.ResultCodeEnum;
//import com.pface.admin.modules.base.service.StorageService;
//import com.pface.admin.modules.member.enums.MemberTypeEnum;
//import com.pface.admin.modules.member.utils.Constants;
//import com.pface.admin.modules.member.vo.ResultStatus;
//import com.pface.admin.modules.member.vo.ResultVo;
//import org.apache.commons.fileupload.FileItem;
//import org.apache.commons.fileupload.FileUploadException;
//import org.apache.commons.fileupload.disk.DiskFileItemFactory;
//import org.apache.commons.fileupload.servlet.ServletFileUpload;
//import org.apache.commons.io.FileUtils;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.nio.channels.FileChannel;
//import java.util.*;



import com.pface.admin.modules.front.vo.VideoMultipartFileParam;
import com.pface.admin.modules.member.po.JmgoFilePath;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
//import win.pangniu.learn.param.MultipartFileParam;
//import win.pangniu.learn.service.StorageService;
//import win.pangniu.learn.utils.Constants;
//import win.pangniu.learn.vo.ResultStatus;
//import win.pangniu.learn.vo.ResultVo;
import com.pface.admin.common.MultipartFileParam;
import com.pface.admin.modules.base.service.StorageService;
import com.pface.admin.modules.member.utils.Constants;
import com.pface.admin.modules.member.vo.ResultStatus;
import com.pface.admin.modules.member.vo.ResultVo;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * 默认控制层
 * Created by wenwen on 2017/4/11.
 * version 1.0
 */
@Controller
@RequestMapping(value = "/admin/file")
public class FileUploadController {

    private Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private StorageService storageService;

//    private static String FILENAME = "";

//    @Value("${pface.upload.dir}")
//    private String decryptFilePath;
//
//    @Value("${pface.upload.temp}")
//    private String decryptFilePathTemp;
//

    @GetMapping
//    @RequiresPermissions("file:view")
    public String memberPage() {
//        model.addAttribute("memberTypeList", MemberTypeEnum.values());
        return "member/index2";
    }


//    private Logger logger = LoggerFactory.getLogger(IndexController.class);
//
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    @Autowired
//    private StorageService storageService;

    /**
     * 秒传判断，断点判断
     *
     * @return
     */
    @RequestMapping(value = "checkFileMd5", method = RequestMethod.POST)
    @ResponseBody
    public Object checkFileMd5(String md5) throws IOException {
        Object processingObj = stringRedisTemplate.opsForHash().get(Constants.FILE_UPLOAD_STATUS, md5);
        if (processingObj == null) {
            return new ResultVo(ResultStatus.NO_HAVE);
        }
        String processingStr = processingObj.toString();
        boolean processing = Boolean.parseBoolean(processingStr);
        String value = stringRedisTemplate.opsForValue().get(Constants.FILE_MD5_KEY + md5);
        if (processing) {
            return new ResultVo(ResultStatus.IS_HAVE, value);
        } else {
            File confFile = new File(value);
            byte[] completeList = FileUtils.readFileToByteArray(confFile);
            List<String> missChunkList = new LinkedList<>();
            for (int i = 0; i < completeList.length; i++) {
                if (completeList[i] != Byte.MAX_VALUE) {
                    missChunkList.add(i + "");
                }
            }
            return new ResultVo<>(ResultStatus.ING_HAVE, missChunkList);
        }
    }

    /**
     * 上传文件
     *
     * @param param
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity fileUpload(VideoMultipartFileParam param, HttpServletRequest request) {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            logger.info("上传文件start。");
            try {
                // 方法1
                //storageService.uploadFileRandomAccessFile(param);
                // 方法2 这个更快点
                JmgoFilePath jmgoFilePath = null;
                storageService.uploadFileByMappedByteBuffer(param,  jmgoFilePath);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("文件上传失败。{}", param.toString());
            }
            logger.info("上传文件end。");
        }
        return ResponseEntity.ok().body("上传成功。");
    }


//
//    /**
//     * 秒传判断，断点判断
//     *
//     * @return
//     */
//    @RequestMapping(value = "checkFileMd5", method = RequestMethod.POST)
//    @ResponseBody
//    public Object checkFileMd5(String md5) throws IOException {
//        Object processingObj = stringRedisTemplate.opsForHash().get(Constants.FILE_UPLOAD_STATUS, md5);
//        if (processingObj == null) {
//            return new ResultVo(ResultStatus.NO_HAVE);
//        }
//        String processingStr = processingObj.toString();
//        boolean processing = Boolean.parseBoolean(processingStr);
//        String value = stringRedisTemplate.opsForValue().get(Constants.FILE_MD5_KEY + md5);
//        if (processing) {
//            return new ResultVo(ResultStatus.IS_HAVE, value);
//        } else {
//            File confFile = new File(value);
//            byte[] completeList = FileUtils.readFileToByteArray(confFile);
//            List<String> missChunkList = new LinkedList<>();
//            for (int i = 0; i < completeList.length; i++) {
//                if (completeList[i] != Byte.MAX_VALUE) {
//                    missChunkList.add(i + "");
//                }
//            }
//            return new ResultVo<>(ResultStatus.ING_HAVE, missChunkList);
//        }
//    }
//
//    /**
//     * 上传文件
//     *
//     * @param param
//     * @param request
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
//    @ResponseBody
//    public ResponseEntity fileUpload(MultipartFileParam param, HttpServletRequest request) {
//        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
//        if (isMultipart) {
//            logger.info("上传文件start。");
//            try {
//                // 方法1
//                //storageService.uploadFileRandomAccessFile(param);
//                // 方法2 这个更快点
//                storageService.uploadFileByMappedByteBuffer(param);
//            } catch (IOException e) {
//                e.printStackTrace();
//                logger.error("文件上传失败。{}", param.toString());
//            }
//            logger.info("上传文件end。");
//        }
//        return ResponseEntity.ok().body("上传成功。");
//    }
//
//    /**
//     * 分片上传
//     *
//     * @return ResponseEntity<Void>
//     */
//    @PostMapping("/upload")
//    @ResponseBody
//    public ResponseEntity<Void> decrypt(HttpServletRequest request,
//                                        @RequestParam(value = "file", required = false) MultipartFile file,
//                                        Integer chunks, Integer chunk, String name, String guid,String md5Code) throws IOException {
//        System.out.println("upload:" + guid+",md5:"+md5Code);
//        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
//        if (isMultipart) {
//            if (file == null) {
//                //throw new ServiceException(ExceptionEnum.PARAMS_VALIDATE_FAIL);
//            }
//            System.out.println("guid:" + guid);
//            if (chunks == null && chunk == null) {
//                chunk = 0;
//            }
//            File outFile = new File(decryptFilePathTemp + File.separator +guid, chunk + ".part");
//            if ("".equals(FILENAME)) {
//                FILENAME = name;
//            }
//            InputStream inputStream = file.getInputStream();
//            FileUtils.copyInputStreamToFile(inputStream, outFile);
//        }
//        return ResponseEntity.ok().build();
//    }
//
//    /**
//     * 合并所有分片
//     *
//     * @throws Exception Exception
//     */
//    @GetMapping("/merge")
//    @ResponseBody
//    public void byteMergeAll(String guid,String md5Code) throws Exception {
//        System.out.println("merge:" + guid+",md5:"+md5Code);
//        File file = new File(decryptFilePathTemp + File.separator + guid);
//        if (file.isDirectory()) {
//            File[] files = file.listFiles();
//            if (files != null && files.length > 0) {
//                File tempFile = new File(decryptFilePath + File.separator +md5Code);
//                if(!tempFile.exists()){
//                    tempFile.mkdir();
//                }
//                File partFile = new File(decryptFilePath + File.separator +md5Code+File.separator+ FILENAME);
//                for (int i = 0; i < files.length; i++) {
//                    File s = new File(decryptFilePathTemp + File.separator+guid , i + ".part");
//                    FileOutputStream destTempfos = new FileOutputStream(partFile, true);
//                    FileUtils.copyFile(s, destTempfos);
//                    destTempfos.close();
//                }
//                FileUtils.deleteDirectory(file);
//                FILENAME = "";
//            }
//        }
//    }
//
//    @PostMapping("/checkFileExist")
//    @ResponseBody
//    public Result checkFileExist(String guid,String md5Code){
//        System.out.println("checkFileExist:" + guid+"md5:"+md5Code);
//        File checkFile = new File(decryptFilePath + File.separator + md5Code);
//        if (checkFile.isDirectory()) {
//            /*File[] files = checkFile.listFiles();
//            if (files != null && files.length > 0) {
//                File partFile = new File(decryptFilePath + File.separator + FILENAME);
//                for (int i = 0; i < files.length; i++) {
//                    //检查文件是否存在，且大小是否一致
//                    if(checkFile.exists()){
//                        //上传过
//                        //return "{\"ifExist\":1}";
//                        return Result.success();
//                    }else{
//                        //没有上传过
//                        //return "{\"ifExist\":0}";
//                        return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
//                    }
//                }
//
//            }*/
//            return Result.success();
//        }
//        return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
//    }
//
//       @PostMapping("/checkChunk")
//       @ResponseBody
//       public Result checkChunk(HttpServletRequest request,
//                                HttpServletResponse response)throws ServletException, IOException{
//        //检查当前分块是否上传成功
//        String fileMd5 = request.getParameter("fileMd5");
//        String chunk = request.getParameter("chunk");
//        String chunkSize = request.getParameter("chunkSize");
//
//        System.out.println("checkChunk,fileMd5="+fileMd5);
//
//        File checkFile = new File(decryptFilePath+File.separator+fileMd5+File.separator+chunk+".part");
//
//        response.setContentType("text/html;charset=utf-8");
//        //检查文件是否存在，且大小是否一致
//        if(checkFile.exists() && checkFile.length()==Integer.parseInt(chunkSize)){
//            //上传过
//            //return "{\"ifExist\":1}";
//            return Result.success();
//        }else{
//            //没有上传过
//            //return "{\"ifExist\":0}";
//            return Result.failure(ResultCodeEnum.NOT_IMPLEMENTED);
//        }
//  }
//
//       @PostMapping("/mergeChunks")
//      public Result mergeChunks(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        //合并文件
//        //需要合并的文件的目录标记
//        String fileMd5 = request.getParameter("fileMd5");
//        System.out.println("mergeChunks,fileMd5="+fileMd5);
//        //读取目录里的所有文件
//        File f = new File(decryptFilePath+File.separator+fileMd5);
//        File[] fileArray = f.listFiles(new FileFilter(){
//            //排除目录只要文件
//            @Override
//            public boolean accept(File pathname) {
//                if(pathname.isDirectory()){ return false;}
//                return true;
//            }
//        });
//        //转成集合，便于排序
//        List<File> fileList = new ArrayList<File>(Arrays.asList(fileArray));
//        Collections.sort(fileList,new Comparator<File>() {
//            @Override
//            public int compare(File o1, File o2) {
//                if(Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName())){ return -1;}
//                return 1;
//            }
//        });
//        //UUID.randomUUID().toString()-->随机名
//        File outputFile = new File(decryptFilePath+File.separator+fileMd5+File.separator+FILENAME);
//        //创建文件
//        outputFile.createNewFile();
//        //输出流
//        FileChannel outChnnel = new FileOutputStream(outputFile).getChannel();
//        //合并
//        FileChannel inChannel;
//        for(File file : fileList){
//            inChannel = new FileInputStream(file).getChannel();
//            inChannel.transferTo(0, inChannel.size(), outChnnel);
//            inChannel.close();
//            //删除分片
//            file.delete();
//        }
//        outChnnel.close();
//        //清除文件夹
//        File tempFile = new File(decryptFilePath+File.separator+fileMd5);
//        if(tempFile.isDirectory() && tempFile.exists()){
//            tempFile.delete();
//        }
//        FILENAME="";
//       return Result.success("合并成功");
//  }
//
//    @PostMapping("/uploadVideo")
//    public void uploadVideo(HttpServletRequest request,
//                            @RequestParam(value = "files", required = false) MultipartFile[] files,
//                            Integer chunks, Integer chunk, String name,
//                            HttpServletResponse response) throws FileUploadException, IOException {
//
//        String savePath = decryptFilePath;
//        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
//        if (isMultipart) {
//            if (files == null) {
//                //throw new ServiceException(ExceptionEnum.PARAMS_VALIDATE_FAIL);
//                return;
//            }
//            if (chunks == null && chunk == null) {
//                chunk = 0;
//            }
//            String fileMd5 = request.getParameter("fileMd5");
//            System.out.println("uploadVideo,fileMd5="+fileMd5);
//           for(int i=0,s=files.length;i<s;i++){
//               MultipartFile file=files[i];
//               if ("".equals(FILENAME)) {
//                   FILENAME = file.getOriginalFilename();
//               }
//               File dir = new File(decryptFilePath+File.separator+fileMd5);
//               if(!dir.exists()){
//                   dir.mkdir();
//               }
//               File outFile = new File(decryptFilePath+File.separator+fileMd5+File.separator+chunk+".part");
//               InputStream inputStream = file.getInputStream();
//               FileUtils.copyInputStreamToFile(inputStream, outFile);
//
//           }
//        }
//  }


}
