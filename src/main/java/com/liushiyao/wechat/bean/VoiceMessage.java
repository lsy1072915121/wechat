package com.liushiyao.wechat.bean;

public class VoiceMessage extends BaseMessage{


  private String MediaId;

  private String Format;

  private String Recognition;

  public String getRecognition() {
    return Recognition;
  }

  public void setRecognition(String recognition) {
    Recognition = recognition;
  }

  public String getMediaId() {
    return MediaId;
  }

  public void setMediaId(String mediaId) {
    MediaId = mediaId;
  }

  public String getFormat() {
    return Format;
  }

  public void setFormat(String format) {
    Format = format;
  }
}
