package com.liushiyao.wechat.bean;


/**
 * 响应消息基类（微信端（服务器）--》用户） BaseMessage
 *
 * @author 电子小孩
 *
 * 2016年5月27日 下午5:28:20
 */
public class BaseMessage {

  // 接收方帐号（收到的OpenID）
  private String ToUserName;
  // 开发者微信号
  private String FromUserName;
  // 消息创建时间 （整型）
  private long CreateTime;
  // 消息类型（text/music/news）
  private String MsgType;
  //MsgID
  private long MsgId;


  public String getToUserName() {
    return ToUserName;
  }

  public void setToUserName(String toUserName) {
    ToUserName = toUserName;
  }

  public String getFromUserName() {
    return FromUserName;
  }

  public void setFromUserName(String fromUserName) {
    FromUserName = fromUserName;
  }

  public long getCreateTime() {
    return CreateTime;
  }

  public void setCreateTime(long createTime) {
    CreateTime = createTime;
  }

  public String getMsgType() {
    return MsgType;
  }

  public void setMsgType(String msgType) {
    MsgType = msgType;
  }

  public long getMsgId() {
    return MsgId;
  }

  public void setMsgId(long msgId) {
    MsgId = msgId;
  }
}
