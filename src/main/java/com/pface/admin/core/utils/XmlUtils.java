package com.pface.admin.core.utils;

import com.pface.admin.modules.member.po.MemberMediaFile;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import com.thoughtworks.xstream.XStream;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;
import springfox.documentation.schema.Xml;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * XML工具类
 */
@Slf4j
public class XmlUtils {

    private final static String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";

    private XmlUtils() {
    }

    /**
     * 序列化XML：object --》 xml string
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String toXML(Object obj) {
        XStream stream = getXStream();
        stream.processAnnotations(obj.getClass());
        return new StringBuffer(XML_DECLARATION).append(stream.toXML(obj)).toString();
    }

    /**
     * 反序列化XML: xml string --> T
     *
     * @param xmlStr
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromXML(String xmlStr, Class<T> clazz) {
        XStream stream = getXStream();
        stream.processAnnotations(clazz);
        Object obj = stream.fromXML(xmlStr);
        try {
            return clazz.cast(obj);
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     * 获取指定节点的值
     *
     * @param xpath
     * @param
     * @return
     */
    public static String getNodeValue(String xpath, String dataStr) {
        try {
            // 将字符串转为xml
            Document document = DocumentHelper.parseText(dataStr);
            // 查找节点
            Element element = (Element) document.selectSingleNode(xpath);
            if (element != null) {
                return element.getStringValue();
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取节点属性值
     *
     * @param xpath
     * @param dataStr
     * @param attrname
     * @return
     */
    public static String getNodeAttrValue(String xpath, String dataStr, String attrname) {
        try {
            // 将字符串转为xml
            Document document = DocumentHelper.parseText(dataStr);
            // 查找节点
            Element element = (Element) document.selectSingleNode(xpath);
            if (element != null) {

                return element.attributeValue(attrname);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取Xstream实例
     *
     * @return
     */
    public static XStream getXStream() {
        return new XStream();
    }

    /////////////////////////////////////////////////////////////////////////////

    //转码模板文件地址
    public static final String INPUT_XML_PATH = "/www/wwwroot/rmt.jiemugo.com/springboot/config/taskdata1.xml";

    //转码名称xpath
    public static final String INPUT_TASK_NAME = "/task/name";

    //水印图片地址，如：/mnt/data/remote/节目购转码/节目购LOGO/jmg_logo.png
    public static final String INPUT_LOGO_XPATH = "/task/streams/stream/h264/preprocessor/logo/uri";
    public static final String INPUT_LOGO_RESIZE_XPATH = "/task/streams/stream/h264/preprocessor/logo/resize"; //缩放

    //截图输出目录，如：/mnt/data/remote/节目购/rmt/dir001/
    public static final String OUT_IMAGE_XPATH = "/task/streams/stream/h264/preprocessor/imagegrabbing/targetpath";
    public static final String OUT_IMAGE_NAME_XPATH = "/task/streams/stream/h264/preprocessor/imagegrabbing/targetname";

    //视频源文件输入地址，如：/mnt/data/remote/节目购/rmt/dir001/周杰伦-给我一首歌的时间(live).mp4
    public static final String INPUT_VIDEO_XPATH = "/task/inputs/localfile/uri";

    //视频输出目录,包括pre 和 std，如：/mnt/data/remote/节目购/rmt/dir001/
    public static final String OUT_VIDEO_PRE = "/task/outputgroups/filearchive[1]/uri";
    public static final String OUT_VIDEO_PRE_NAME = "/task/outputgroups/filearchive[1]/targetname";
    public static final String OUT_VIDEO_STD = "/task/outputgroups/filearchive[2]/uri";
    public static final String OUT_VIDEO_STD_NAME = "/task/outputgroups/filearchive[2]/targetname";

    //可以参与转码的文件扩张名
    public static final String ZHICHI_CHANGECODE_FILEEXT = "mp4,m4v,flv,mov,mpg,mpeg,avi";
//    public static final String ZHICHI_CHANGECODE_FILEEXT = "mp4,ts,flv";

    //支持的音频上传扩展名
    public static final String ZHICHI_AUDIO_FILEEXT = "mp3,mp2,wav,aac,ac3,flac,ape,mpa,m4a,ogg";

    //转码提交地址
    public static final String CHANGE_VIDEO_URL = "http://192.168.55.80/api/task/launch";
    //获取媒体信息提交地址
    public static final String GET_MIDEAINO_URL = "http://192.168.55.80/api/cloudtask/mediainfo?url=";

    public static final String GET_ZHUANGMA_STATUS_URL = "http://192.168.55.80/api/task/taskid/progress";

    //获取媒体信息相关属性xpath
    public static final String MEDIAINFO_EXT = "/mediaInfo/container";  //文件格式
    public static final String MEDIAINFO_DURATION = "/mediaInfo/duration";  //时长，单位为毫秒
    public static final String MEDIAINFO_SIZE = "/mediaInfo/size";  //文件大小，单位为Byte，除以两个1024获得MB
    public static final String MEDIAINFO_BITRATE = "/mediaInfo/bitrate";  //平均比特率
    public static final String MEDIAINFO_CODEC = "/mediaInfo/videos/video/codec";  //视频编码
    public static final String MEDIAINFO_RESOLUTION = "/mediaInfo/videos/video/resolution";  //视频分辨率
    public static final String MEDIAINFO_ASPECT_RATIO = "/mediaInfo/videos/video/aspect_ratio";  //视频宽高比

    /**
     * 转码
     *
     * @param inputFilepath ，不含文件名
     * @return
     */
    public static String changeVideo(String fileName, String newFilename, String filename_std, String filename_pre, String jietuimgName,
                                     String aspectRation,
                                     String resolution,
                                     String inputFilepath) {
        log.info("转码文件：" + inputFilepath + fileName);
        long s = System.currentTimeMillis();
        String ret = null;
        try {
            //转码
            String xml = updateChangeCodeXml(fileName, newFilename, filename_std, filename_pre, jietuimgName, aspectRation, resolution, inputFilepath);
            ret = Okhttp3Utils.postXml(CHANGE_VIDEO_URL, xml);
            log.debug("转码返回：" + ret);
        } catch (Exception ex) {
            log.debug("转码出错了");
            ex.printStackTrace();
        }
        log.info("耗时：" + (System.currentTimeMillis() - s) + "ms");
        return ret;
    }

    /**
     * 更新转码
     *
     * @param fileName
     * @param inputFilepath
     * @return
     */
    public static String updateChangeCodeXml(String fileName, String newFilename, String filename_std, String filename_pre, String jietuimgName,
                                             String aspectRation, String resolution,
                                             String inputFilepath) {
        Document xmldoc = getXmlFile();

        //任务名称
        xmldoc = setNodeValue(INPUT_TASK_NAME, xmldoc, fileName);

        //水印
        String shuiyinImg = getShuiyinImg(aspectRation);
        xmldoc = setNodeValue(INPUT_LOGO_XPATH, xmldoc, shuiyinImg); //水印图片
        xmldoc = setNodeValue(INPUT_LOGO_RESIZE_XPATH, xmldoc, getAsize(shuiyinImg,resolution)); //水印缩放

        //截图
        xmldoc = setNodeValue(OUT_IMAGE_XPATH, xmldoc, inputFilepath); //图片地址
        xmldoc = setNodeValue(OUT_IMAGE_NAME_XPATH, xmldoc, jietuimgName.substring(0, jietuimgName.indexOf("."))); //图片文件

        //视频源文件
        xmldoc = setNodeValue(INPUT_VIDEO_XPATH, xmldoc, inputFilepath + newFilename);

        //标准片和预览片输出
        xmldoc = setNodeValue(OUT_VIDEO_STD, xmldoc, inputFilepath);
        xmldoc = setNodeValue(OUT_VIDEO_STD_NAME, xmldoc, filename_std.substring(0, filename_std.indexOf(".")));
        xmldoc = setNodeValue(OUT_VIDEO_PRE, xmldoc, inputFilepath);
        xmldoc = setNodeValue(OUT_VIDEO_PRE_NAME, xmldoc, filename_pre.substring(0, filename_pre.indexOf(".")));

        return doc2xml(xmldoc);
    }

    private static String getShuiyinImg(String aspectRation) {
        Integer w = null;
        Integer h = null;
        if (StringUtils.isNotEmpty(aspectRation)) {
            if (aspectRation.indexOf(":") > 0) {
                String[] ar = aspectRation.split(":");
                w = Integer.parseInt(ar[0].trim());
                h = Integer.parseInt(ar[1].trim());
            }
        }
        String shuipath = "/mnt/data/remote/jmg01/rmt/shuiyin/";
        String shuiyinimg = "jmg_logo_h.png";
        if (w != null && h != null) {
            if (w <= h) {
                shuiyinimg = "jmg_logo_s.png";
            }
        }

        return shuipath.concat(shuiyinimg);
    }

    private static String getAsize(String shuiyinImg, String resolution) {

        Integer w = null;
        Integer h = null;
        if (StringUtils.isNotEmpty(resolution)) {
            if (resolution.indexOf("x") > 0) {
                String[] ar = resolution.split("x");
                w = Integer.parseInt(ar[0].trim());
                h = Integer.parseInt(ar[1].trim());
            }
        }

        String asize = "100";
        if (w != null && h != null) {
            if (shuiyinImg.indexOf("jmg_logo_h") >=0){//横屏
                BigDecimal d = (new BigDecimal(h) .divide(new BigDecimal("1080"), 2,BigDecimal.ROUND_FLOOR)).//抹掉小数
                        multiply(new BigDecimal("100"));
                asize = d.intValue() + "";
            }else{//竖屏
                BigDecimal d = (new BigDecimal(h) .divide(new BigDecimal("1920"), 2,BigDecimal.ROUND_FLOOR)).//抹掉小数
                        multiply(new BigDecimal("100"));
                asize = d.intValue() + "";
            }
        }
        return asize;
    }

    /**
     * 获取媒体信息
     *
     * @param filepath
     * @return
     */
    public static String getMediaInfo(String filepath) {
        String ret = null;
        try {
            ret = OKHttpUtil.httpGet(GET_MIDEAINO_URL + filepath);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ret;
    }

    /**
     * 获取转码任务进度
     *
     * @param memberMediaFile
     * @return
     */
    public static String getChangeCodeStatus(MemberMediaFile memberMediaFile) {
        String ret = null;
        try {
            if (StringUtils.isNotEmpty(memberMediaFile.getChangcodetaskid())) {
                ret = OKHttpUtil.httpGet(GET_ZHUANGMA_STATUS_URL.replaceAll("taskid", memberMediaFile.getChangcodetaskid()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ret;
    }


    /**
     * document 到xml string
     *
     * @param document
     * @return
     */
    public static String doc2xml(Document document) {
        return document.asXML();
    }

    /**
     * xml 到 document
     *
     * @param xmlstring
     * @return
     */
    public static Document xml2doc(String xmlstring) {
        Document document = null;
        try {
            document = DocumentHelper.parseText(xmlstring);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return document;
    }

    /**
     * 获取节点列表，根据xpath，可能有多个
     *
     * @param xpath
     * @param xmlstring
     * @return
     */
    public static List<Element> getNodes(String xpath, String xmlstring) {
        // 将字符串转为xml
        Document document = xml2doc(xmlstring);
        // 查找节点
        return document != null ? document.selectNodes(xpath) : null;
    }

    /**
     * @param xpath
     * @param document
     * @return
     */
    public static List<Element> getNodes(String xpath, Document document) {
        return document.selectNodes(xpath);
    }

    /**
     * 获取节点值，
     *
     * @param xpath  其下的节点要求唯一
     * @param mediaInfo
     * @return
     */
//    public static String getNodevalue(String xpath, String mediaInfo){
//        String nodevalue = null;
//        List<Element> documents = XmlUtils.getNodes(xpath, mediaInfo);
//        if (!documents.isEmpty()){
//            nodevalue = documents.get(0).getText();
//        }
//        return nodevalue;
//    }

//    public static void setNodeValue(String xpath, String xmlstring, String val) {
//        try {
//            // 将字符串转为xml
//            Document document = DocumentHelper.parseText(xmlstring);
//            // 查找节点
//            Element element = (Element) document.selectSingleNode(xpath);
//            if (element != null) {
//                element.setText(val);
//            }
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 修改某个文档中某节点的值，并返回修改后的文档
     *
     * @param xpath
     * @param inputdocument
     * @param val
     * @return
     */
    public static Document setNodeValue(String xpath, Document inputdocument, String val) {
        Document ret_doc = inputdocument;
        List<Element> elements = getNodes(xpath, ret_doc);
        if (elements != null) {
            for (Element element : elements) {
                element.setText(val);
            }
        }
        return ret_doc;
    }

    /**
     * 获取转码模板文件
     *
     * @return
     */
    public static Document getXmlFile() {
        Document doc = null;
        try {
            doc = new SAXReader().read(new File(INPUT_XML_PATH));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return doc;
    }


    public static void main(String[] args) {
//        Document xml = getXmlFile();
//        String xpath = INPUT_LOGO_XPATH;
////        String newval = "/mnt/data/remote/节目购转码/节目购LOGO/jmg_logo_new.png";
//        List<Element> ret = getNodes(xpath, xml);
//        for (Element el : ret) {
//            System.out.println("当前值 = " + el.getText());
//        }

//        System.out.println("修改后");
//
//        Document updated_docment = setNodeValue(xpath, xml, newval);
//        List<Element> ret2 = getNodes(xpath, updated_docment);
//        for (Element el : ret2) {
//            System.out.println("新值 = " + el.getText());
//        }

//        String id = getNodeAttrValue("/bookstore", getxml(), "id");
//        System.out.printf("id = " + id);

//        String filename = "sfsdfsdfsdg.png";
//        String GET_MIDEAINO_URL = "http://192.168.55.80/api/cloudtask/mediainfo?url=urlval";
//        System.out.printf(GET_ZHUANGMA_STATUS_URL.replaceAll("taskid" ,"5445454"));

//
//        String ff = "/mnt/data/remote/jmg01/rmt/dir001/周杰伦-给我一首歌的时间(live)1080.mp4";
//        String f = ff.substring(0, ff.indexOf("周杰伦-给我一首歌的时间(live)1080.mp4"));
//        System.out.printf(f);


//        List<File> files = FileUtil.searchFileInDirLike(new File("G:\\p\\rongmeiti\\开发资料\\转码"),"周杰伦-给我一首歌的时间(live)_img.jpg");
//        if (!files.isEmpty()) {
//            File file = files.get(0);
//            boolean flag = FileUtil.renameFile(file, "周杰伦-给我一首歌的时间(live)_img_update.jpg");
//            if (flag) {
//                log.info("修正文件名：" + file.getName() + "  成功");
//            } else {
//                log.info("修正文件名：" + file.getName() + "  失败");
//            }
//        }

//        System.out.println(getShuiyinImg("16:16"));
//        System.out.println(getAsize("1280x960"));

        String status = XmlUtils.getNodeValue("/results/status", ss());
        System.out.println("获取转码进度的状态："+status);
    }

    private static String ss(){

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "        <results>\n" +
                "        \t\t<result id=\"64944\">\n" +
                "        \t\t<status>COMPLETED</status>\n" +
                "        \t\t<startAt>2019-07-15 11:47:44</startAt>\n" +
                "        \t\t<completeAt>2019-07-15 11:47:58</completeAt>\n" +
                "        \t\t<lastError></lastError>\n" +
                "        \t\t<lastErrorDescription></lastErrorDescription>\n" +
                "        \t\t<progress>\n" +
                "        \t\t\t<input index=\"0\" elapsed=\"14\" power=\"42\">100</input>\n" +
                "        \t\t</progress>\n" +
                "        \t</result>\n" +
                "        </results>";
    }
    private static String getxml() {
        return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "\n" +
                "<bookstore id='77777777777777777777'>\n" +
                "\n" +
                "<book>\n" +
                "  <title lang=\"eng\">Harry Potter</title>\n" +
                "  <price>29.99</price>\n" +
                "</book>\n" +
                "\n" +
                "<book>\n" +
                "  <title lang=\"eng\">Learning XML</title>\n" +
                "  <price>39.95</price>\n" +
                "</book>\n" +
                "\n" +
                "</bookstore>\n"
                ;
    }

    private static String getTestMediaInfo() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<mediaInfo\n" +
                "\tname=\"/mnt/data/remote/jmg01/rmt/dir001/08a291e341c3b57b6113e15852aa419c/（宣传片）1“颠倒小屋”带你体验天旋地转0001.mp4\">\n" +
                "\t<container>MP4</container>\n" +
                "\t<duration>56840</duration>\n" +
                "\t<size>7316787</size>\n" +
                "\t<bitrate>1029 kbps</bitrate>\n" +
                "\t<bitratemode>VBR</bitratemode>\n" +
                "\t<videos count=\"1\">\n" +
                "\t\t<video>\n" +
                "\t\t\t<trackid>1</trackid>\n" +
                "\t\t\t<name>Video</name>\n" +
                "\t\t\t<used>0</used>\n" +
                "\t\t\t<codec>H.264</codec>\n" +
                "\t\t\t<duration>56840</duration>\n" +
                "\t\t\t<bitrate>3000.0 kbps</bitrate>\n" +
                "\t\t\t<framerate>25.00</framerate>\n" +
                "\t\t\t<resolution>1280x720</resolution>\n" +
                "\t\t\t<aspect_ratio>16:17</aspect_ratio>\t\t\t\n" +
                "\t\t\t<interlacemode>Progressive</interlacemode>\n" +
                "\t\t</video>\n" +
                "\t</videos>\n" +
                "\t<audios count=\"1\">\n" +
                "\t\t<audio>\n" +
                "\t\t\t<trackid>2</trackid>\n" +
                "\t\t\t<name>Audio 1</name>\n" +
                "\t\t\t<language></language>\n" +
                "\t\t\t<used>0</used>\n" +
                "\t\t\t<codec>AAC</codec>\n" +
                "\t\t\t<duration>56749</duration>\n" +
                "\t\t\t<bitrate>53.6 kbps</bitrate>\n" +
                "\t\t\t<channel>2</channel>\n" +
                "\t\t\t<samplerate>44.100 kHz</samplerate>\n" +
                "\t\t\t<bitdepth>16</bitdepth>\n" +
                "\t\t</audio>\n" +
                "\t</audios>\n" +
                "\t<subtitles count=\"0\">\n" +
                "    </subtitles>\n" +
                "</mediaInfo>";

    }
}