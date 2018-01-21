package com.liushiyao.wechat.bean;

public class ShortVideoMessage extends BaseMessage{


  private String MediaId;

  private String ThumbMediaId;

  public String getThumbMediaId() {
    return ThumbMediaId;
  }

  public void setThumbMediaId(String thumbMediaId) {
    ThumbMediaId = thumbMediaId;
  }

  public String getMediaId() {
    return MediaId;
  }

  public void setMediaId(String mediaId) {
    MediaId = mediaId;
  }
}
