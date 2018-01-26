package com.liushiyao.wechat.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.liushiyao.wechat.bean.AccessToken;
import com.liushiyao.wechat.bean.Menu;
import org.apache.log4j.Logger;

public class WeChatUtils {

  public static final Logger LOGGER = Logger.getLogger(WeChatUtils.class);


  // 获取access_token的接口地址（GET） 限200（次/天）
  public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

  // 菜单创建（POST） 限100（次/天）
  public static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
  //发送模板消息
  public static String model_msg_url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

  public static AccessToken getAccessToken ( String appid, String appsecret ) {
    AccessToken accessToken = null;
    String requestUrl = access_token_url.replace ("APPID", appid ).replace ( "APPSECRET", appsecret );
    LOGGER.info ( requestUrl );
    JsonObject jsonObject = HttpUtils.httpsRequest(requestUrl, "GET", null);
    //如果请求成功
    if ( jsonObject != null ) {
      try {
        accessToken = new AccessToken ( );
        accessToken.setToken ( jsonObject.get ( "access_token" ).toString ().replace ( "\"","" ) );
        accessToken.setExpiresIn ( jsonObject.get ( "expires_in" ).toString ().replace ( "\"","" ) );
      } catch ( Exception e ) {
        // TODO: handle exception
        accessToken = null;
        // 获取token失败
        LOGGER.info ( "获取token失败 errcode:{" + jsonObject.get ( "errcode" ) + "} errmsg:{" + jsonObject.get ( "errmsg" ) + "}" );

      }
    }
    return accessToken;
  }


  /**
   * 创建菜单
   *
   * @param menu 菜单实例
   * @param accessToken 有效的access_token
   * @return 0表示成功，其他值表示失败
   */
  public static String createMenu ( Menu menu, String accessToken ) {
    String  result = "";

    // 拼装创建菜单的url
    String url = menu_create_url.replace ( "ACCESS_TOKEN", accessToken );
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    String jsonMenu = gson.toJson (menu);
    LOGGER.info ( "自定义菜单json：" + jsonMenu );
    // 调用接口创建菜单
    JsonObject jsonObject = HttpUtils.httpsRequest ( url, "POST", jsonMenu );
    if ( null != jsonObject ) {
      if ( !jsonObject.get ( "errcode" ).equals ( "0" ) ) {
        result = jsonObject.get ( "errcode" ).toString ();
      }
    }

    return result;
  }
}
