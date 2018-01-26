package com.liushiyao.wechat.bean;

import com.liushiyao.wechat.utils.PropertyUtil;
import com.liushiyao.wechat.utils.WeChatUtils;
import org.apache.log4j.Logger;

public class ButtonBean {

  public static final Logger LOGGER = Logger.getLogger(ButtonBean.class);

  public static String createButton(){


    LOGGER.debug ( "------------开始创建菜单--------------------" );
    // 第三方用户唯一凭证
    String appId = PropertyUtil.getProperty ( "APP_ID" );
    // 第三方用户唯一凭证密钥
    String appSecret = PropertyUtil.getProperty ( "APP_SECRET" );
    // 调用接口获取access_token
    AccessToken at = WeChatUtils.getAccessToken ( appId , appSecret );
    String result = "";
    if ( null != at ) {
      // 调用接口创建菜单
      result = WeChatUtils.createMenu ( getMenu ( ) , at.getToken ( ) );
      // 判断菜单创建结果
      if ( result.equals ( "0" ) ) {
        LOGGER.info ( "菜单创建成功！" );
      } else {
        LOGGER.info ( "菜单创建失败，错误码：" + result );
      }
    } else {
      LOGGER.info ( "-----------获取 AccessToken 失败----------" );
    }
    return result;
  }

  /**
   * 组装菜单数据
   * view - URL跳转
   * check-事件推送
   *
   * @return
   */
  private static Menu getMenu ( ) {
    CommonButton btn11 = new CommonButton ( );
    btn11.setName ( "扫一扫" );
    btn11.setType ( "scancode_waitmsg" );
    btn11.setKey ( "11" );
    btn11.setUrl ( PropertyUtil.getProperty ( "HOME_URL" ) );
    //==========与设备对接菜单=============//
    CommonButton btn21 = new CommonButton ( );
    btn21.setName ( "主页" );
    btn21.setType ( "view" );
    btn21.setKey ( "21" );
    btn21.setUrl ( PropertyUtil.getProperty ( "HOME_URL" ) );
    CommonButton btn31 = new CommonButton ( );
    btn31.setName ( "天气查询" );
    btn31.setType ( "click" );
    btn31.setKey ( "31" );
    CommonButton btn32 = new CommonButton ( );
    btn32.setName ( "数据库查询" );
    btn32.setType ( "view" );
    btn32.setKey ( "32" );
    btn32.setUrl ( PropertyUtil.getProperty ( "HOME_URL" ) );
    CommonButton btn33 = new CommonButton ( );
    btn33.setName ( "其他查询" );
    btn33.setType ( "click" );
    btn33.setKey ( "33" );


    ComplexButton mainBtn3 = new ComplexButton ( );
    mainBtn3.setName ( "API集合" );
    mainBtn3.setSub_button ( new CommonButton[] { btn31 , btn32 , btn33 } );

    Menu menu = new Menu ( );
    menu.setButton ( new Button[] { btn11 , btn21 , mainBtn3 } );

    return menu;


  }

}
