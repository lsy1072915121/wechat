package com.liushiyao.wechat.bean;

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

public class TextMessageBean {


  /**
   * 解析微信发来的请求（xml）
   */
  @SuppressWarnings("unchecked")
  private static Map<String, String> parseXml(HttpServletRequest request) throws Exception {

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


  public static TextMessage getTextMessage(HttpServletRequest request) throws Exception {

    Map<String, String> map = parseXml(request);

    TextMessage textMessage = new TextMessage();
    textMessage.setToUserName(map.get("FromUserName"));
    textMessage.setFromUserName(map.get("ToUserName"));
    textMessage.setMsgType(map.get("MsgType"));
    textMessage.setCreateTime(Long.parseLong(map.get("CreateTime")));
    textMessage.setMsgId(Long.parseLong(map.get("MsgId")));
    textMessage.setContent(map.get("Content"));

    return textMessage;
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
