package com.liushiyao.wechat.bean;

import com.liushiyao.wechat.common.MessageState;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class BaseMessageBean {


  /**
   * 解析微信发来的请求（xml）
   */
  @SuppressWarnings("unchecked")
  public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {

    //将解析结果存在Map中
    Map<String, String> map = new HashMap<String, String>();

    //从request中取得输入流
    InputStream inputStream = request.getInputStream();

    //读取输入流
    SAXReader reader = new SAXReader();
    Document document = reader.read(inputStream);

    //取得xml的根元素
    Element root = document.getRootElement();

    //得到根元素下所有子节点
    List<Element> elementList = root.elements();

    //遍历所有子节点
    for (Element e : elementList) {
      map.put(e.getName(), e.getText());
    }

    inputStream.close();
    return map;

  }


  /**
   * Message exchange
   * @param request
   * @return
   * @throws Exception
   */
  public static BaseMessage getMessage(HttpServletRequest request) throws Exception {

    Map<String, String> map = parseXml(request);

    BaseMessage baseMessage = new TextMessage();
    baseMessage.setToUserName(map.get("ToUserName"));
    baseMessage.setFromUserName(map.get("FromUserName"));
    baseMessage.setMsgType(map.get("MsgType"));
    baseMessage.setCreateTime(Long.parseLong(map.get("CreateTime")));
    baseMessage.setMsgId(Long.parseLong(map.get("MsgId")));

    String msgType = baseMessage.getMsgType();
    if (msgType.equals(MessageState.REQ_MESSAGE_TYPE_TEXT)){
      TextMessage textMessage = (TextMessage) baseMessage;
      textMessage.setContent(map.get("Content"));
      return textMessage;
    }else if(msgType.equals(MessageState.REQ_MESSAGE_TYPE_IMAGE)){
      ImageMessage imageMessage = (ImageMessage) baseMessage;
      imageMessage.setPicUrl(map.get("PicUrl"));
      imageMessage.setMediaId(map.get("MediaId"));
      return imageMessage;
    }else if (msgType.equals(MessageState.REQ_MESSAGE_TYPE_VOICE)){
      VoiceMessage voiceMessage = (VoiceMessage) baseMessage;
      voiceMessage.setFormat(map.get("Format"));
      voiceMessage.setMediaId(map.get("MediaId"));
      voiceMessage.setRecognition(map.get("Recognition"));
      return voiceMessage;
    }else if (msgType.equals(MessageState.REQ_MESSAGE_TYPE_LOCATION)){
      LocationMessage location = (LocationMessage) baseMessage;
      location.setLabel(map.get("Label"));
      location.setLocation_X(map.get("Location_X"));
      location.setLocation_Y(map.get("Location_Y"));
      location.setScale(map.get("Scale"));
      return location;
    }else if (msgType.equals(MessageState.REQ_MESSAGE_TYPE_VIDEO)){
      VideoMessage videoMessage = (VideoMessage) baseMessage;
      videoMessage.setMediaId(map.get("MediaId"));
      videoMessage.setThumbMediaId(map.get("ThumbMediaId"));
      return videoMessage;
    }else if (msgType.equals(MessageState.REQ_MESSAGE_TYPE_SHORT_VIDEO)){
      ShortVideoMessage shortVideoMessage = (ShortVideoMessage) baseMessage;
      shortVideoMessage.setMediaId(map.get("MediaId"));
      shortVideoMessage.setThumbMediaId(map.get("ThumbMediaId"));
      return shortVideoMessage;
    }else if (msgType.equals(MessageState.REQ_MESSAGE_TYPE_LINK)){
      LinkMessage linkMessage = (LinkMessage) baseMessage;
      linkMessage.setDescription(map.get("Description"));
      linkMessage.setTitle(map.get("Title"));
      linkMessage.setUrl(map.get("Url"));
      return linkMessage;
    }
    return baseMessage;
  }




  /**
   * 回复的文本消息转换成xml格式
   */
  public static String textMessageToXml(TextMessage textMessage) {
    xStream.alias("xml", textMessage.getClass());
    return xStream.toXML(textMessage);
  }

  /**
   * 扩展xstream，使其支持CDATA块
   */
  private static XStream xStream=new XStream (new XppDriver(){
    public HierarchicalStreamWriter createWriter( Writer out){
      return new PrettyPrintWriter(out){
        // 对所有xml节点的转换都增加CDATA标记
        boolean cdata=true;

        public void  startNode(String name,Class clazz) {
          super.startNode(name, clazz);
        }

        protected void writeText( QuickWriter writer, String text) {
          if (cdata) {
            writer.write("<![CDATA[");
            writer.write(text);
            writer.write("]]>");
          } else {
            writer.write(text);
          }
        }
      };
    }
  });


}
