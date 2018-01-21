package com.liushiyao.wechat.bean;

import com.liushiyao.wechat.common.MessageState;
import com.liushiyao.wechat.servlet.WeChatCoreServlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class WeChatBean {

  public static final Logger LOGGER = Logger.getLogger(WeChatCoreServlet.class);


  private static String parseReq (HttpServletRequest request) throws IOException {

    BufferedReader bufferedReader =
        new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
    String line = null;
    String context ="";
    while((line=bufferedReader.readLine())!=null){
      context += line;
    }
    bufferedReader.close();
    return context;
  }


  public static String processRequest(HttpServletRequest req) throws Exception {

    String responseStr = "";

    LOGGER.info("WeChat ReSend Context:"+parseReq(req));

    BaseMessage baseMessage = BaseMessageBean.getMessage(req);
    String msgType = baseMessage.getMsgType();
    //文本信息
    if (msgType.equals(MessageState.REQ_MESSAGE_TYPE_TEXT)) {
      TextMessage textMessage = (TextMessage) baseMessage;
      responseStr = "Text=" + textMessage.getContent();
      //Image
    } else if (msgType.equals(MessageState.REQ_MESSAGE_TYPE_IMAGE)) {
      ImageMessage imageMessage = (ImageMessage) baseMessage;
      responseStr = "Image=" + imageMessage.getPicUrl();
    } else if (msgType.equals(MessageState.REQ_MESSAGE_TYPE_SHORT_VIDEO)) {
      ShortVideoMessage shortVideoMessage = (ShortVideoMessage) baseMessage;
      responseStr = "ShortVideo=" + shortVideoMessage.getThumbMediaId();
    } else if (msgType.equals(MessageState.REQ_MESSAGE_TYPE_VIDEO)) {
      VideoMessage videoMessage = (VideoMessage) baseMessage;
      responseStr = "Video=" + videoMessage.getThumbMediaId();
    } else if (msgType.equals(MessageState.REQ_MESSAGE_TYPE_LINK)) {
      LinkMessage linkMessage = (LinkMessage) baseMessage;
      responseStr = "Link=" + linkMessage.getUrl();
    } else if (msgType.equals(MessageState.REQ_MESSAGE_TYPE_LOCATION)) {
      LocationMessage locationMessage = (LocationMessage) baseMessage;
      responseStr =
          "Location=" + locationMessage.getLocation_X() + ":" + locationMessage.getLocation_Y();
    } else if (msgType.equals(MessageState.REQ_MESSAGE_TYPE_VOICE)) {
      VoiceMessage voiceMessage = (VoiceMessage) baseMessage;
      responseStr = "Voice=" + voiceMessage.getRecognition();
    }

    TextMessage responseMessage = new TextMessage();
    responseMessage.setToUserName(baseMessage.getFromUserName());
    responseMessage.setFromUserName(baseMessage.getToUserName());
    responseMessage.setCreateTime(new Date().getTime());
    responseMessage.setMsgType(MessageState.RESP_MESSAGE_TYPE_TEXT);
    responseMessage.setContent(responseStr);
    return BaseMessageBean.textMessageToXml(responseMessage);

  }


}
