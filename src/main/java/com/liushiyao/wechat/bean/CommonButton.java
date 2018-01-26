package com.liushiyao.wechat.bean;


public class CommonButton extends Button {

  //数据类型
  private String type;
  //菜单关键字
  private String key;

  //===========新增==========//
  //数据内容
  private String content;
  //菜单url
  private String url;


  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }


}
