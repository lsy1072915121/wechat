package com.liushiyao.wechat.bean;

import com.liushiyao.wechat.common.MessageState;
import com.liushiyao.wechat.servlet.WeChatCoreServlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class WeChatBean {

  public static final Logger LOGGER = Logger.getLogger(WeChatCoreServlet.class);


  private static String parseReq(HttpServletRequest request) throws IOException {

    BufferedReader bufferedReader =
        new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
    String line = null;
    String context = "";
    while ((line = bufferedReader.readLine()) != null) {
      context += line;
    }
    bufferedReader.close();
    return context;
  }


  //微信转发消息处理函数
  public static String processRequest(HttpServletRequest req) throws Exception {

    String responseStr = "";

    LOGGER.info("WeChat ReSend Context:" + parseReq(req));

    Map<String, String> requestMap = BaseMessageBean.parseXml(req);
    String msgType = requestMap.get("MsgType");
    //文本信息
    if (msgType.equals(MessageState.REQ_MESSAGE_TYPE_TEXT)) {
      responseStr = textMessageHandle(requestMap);
      //Image
    } else if (msgType.equals(MessageState.REQ_MESSAGE_TYPE_IMAGE)) {
      responseStr = imageMessageHandle(requestMap);
    } else if (msgType.equals(MessageState.REQ_MESSAGE_TYPE_SHORT_VIDEO)) {
      responseStr = shortVideoMessageHandle(requestMap);
    } else if (msgType.equals(MessageState.REQ_MESSAGE_TYPE_VIDEO)) {
      responseStr = videoMessageHandle(requestMap);
    } else if (msgType.equals(MessageState.REQ_MESSAGE_TYPE_LINK)) {
      responseStr = linkMessageHandle(requestMap);
    } else if (msgType.equals(MessageState.REQ_MESSAGE_TYPE_LOCATION)) {
      responseStr = locationMessageHandle(requestMap);
    } else if (msgType.equals(MessageState.REQ_MESSAGE_TYPE_VOICE)) {
      responseStr = voiceMessageHandle(requestMap);
    }

    TextMessage responseMessage = new TextMessage();
    responseMessage.setToUserName(requestMap.get("FromUserName"));
    responseMessage.setFromUserName(requestMap.get("ToUserName"));
    responseMessage.setCreateTime(new Date().getTime());
    responseMessage.setMsgType(MessageState.RESP_MESSAGE_TYPE_TEXT);
    responseMessage.setContent(responseStr);
    return BaseMessageBean.textMessageToXml(responseMessage);

  }


  private static String voiceMessageHandle(Map<String, String> map) {
    VoiceMessage voiceMessage = new VoiceMessage();

    voiceMessage.setFormat(map.get("Format"));
    voiceMessage.setMediaId(map.get("MediaId"));
    voiceMessage.setRecognition(map.get("Recognition"));

    return voiceMessage.getRecognition();


  }

  /**
   * 定位处理函数
   */
  private static String locationMessageHandle(Map<String, String> map) {

    LocationMessage locationMessage = new LocationMessage();

    locationMessage.setLabel(map.get("Label"));
    locationMessage.setLocation_X(map.get("Location_X"));
    locationMessage.setLocation_Y(map.get("Location_Y"));
    locationMessage.setScale(map.get("Scale"));

    return locationMessage.getLocation_Y() + ":" + locationMessage.getLocation_Y();


  }

  /**
   * 链接处理函数
   */
  private static String linkMessageHandle(Map<String, String> map) {

    LinkMessage linkMessage = new LinkMessage();

    linkMessage.setDescription(map.get("Description"));
    linkMessage.setTitle(map.get("Title"));
    linkMessage.setUrl(map.get("Url"));

    return linkMessage.getUrl();
  }

  /**
   * 视频处理函数
   */
  private static String videoMessageHandle(Map<String, String> map) {

    VideoMessage videoMessage = new VideoMessage();

    videoMessage.setMediaId(map.get("MediaId"));
    videoMessage.setThumbMediaId(map.get("ThumbMediaId"));

    return videoMessage.getThumbMediaId();
  }

  /**
   * 短视频处理函数
   */
  private static String shortVideoMessageHandle(Map<String, String> map) {

    ShortVideoMessage shortVideoMessage = new ShortVideoMessage();

    shortVideoMessage.setMediaId(map.get("MediaId"));
    shortVideoMessage.setThumbMediaId(map.get("ThumbMediaId"));


    return shortVideoMessage.getThumbMediaId();
  }


  /**
   * 图片处理函数
   */
  private static String imageMessageHandle(Map<String, String> map) {

    ImageMessage imageMessage = new ImageMessage();

    imageMessage.setPicUrl(map.get("PicUrl"));
    imageMessage.setMediaId(map.get("MediaId"));

    return imageMessage.getPicUrl();


  }


  /**
   * 文本消息处理函数
   *
   * @return 处理后的文本信息
   */
  private static String textMessageHandle(Map<String, String> map) {

    TextMessage textMessage = new TextMessage();
    textMessage.setContent(map.get("Content"));

    return textMessage.getContent();

  }


}
