package com.liushiyao.wechat.bean;

import com.liushiyao.wechat.common.MessageState;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

public class WeChatBean {

  public static String processRequest(HttpServletRequest req) throws Exception {

    String responseStr = "";
    TextMessage requestMessage = TextMessageBean.getTextMessage(req);
    TextMessage responseMessage = new TextMessage();
    responseMessage.setToUserName(requestMessage.getFromUserName());
    responseMessage.setFromUserName(requestMessage.getToUserName());
    responseMessage.setCreateTime(new Date().getTime());
    responseMessage.setMsgType(MessageState.RESP_MESSAGE_TYPE_TEXT);
    //文本信息
    if (requestMessage.getMsgType().
        equals(MessageState.REQ_MESSAGE_TYPE_TEXT)) {
      String content = requestMessage.getContent();
      responseMessage.setContent("返回："+content);
    }

    responseStr = TextMessageBean.textMessageToXml(responseMessage);
    return responseStr;

  }


}
