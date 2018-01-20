package com.liushiyao.wechat.servlet;


import com.liushiyao.wechat.bean.WeChatBean;
import com.liushiyao.wechat.utils.SignUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 * WeChat二次开发核心控制器
 */
@WebServlet(name = "wecahtCore",urlPatterns = "/wechatCore")
public class WeChatCoreServlet  extends HttpServlet{


  public static final Logger LOGGER = Logger.getLogger(WeChatCoreServlet.class);

  /**
   * 处理微信服务器验证
   * @param req
   * @param resp
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
    LOGGER.info ( "-------开始验证请求是否来自微信-----------！" );
    //微信加密签名
    String signature = req.getParameter ( "signature" );
    //时间戳
    String timestamp = req.getParameter ( "timestamp" );
    //随机数
    String nonce = req.getParameter ( "nonce" );
    //随机字符串
    String echostr = req.getParameter ( "echostr" );
    LOGGER.info ( "---接收到的来自" + req.getRemoteHost ( ) + ",请求参数：signature:" + signature + ",timestamp:" + timestamp + ",nonce:" + nonce + ",echostr:" + echostr );
    PrintWriter out = resp.getWriter ( );
    //通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
    if ( signature == null || timestamp == null || nonce == null || echostr == null ) {
      out.write ( "you records has recorded,please leave it now !" );
    } else {
      if ( SignUtil.checkSignature ( signature , timestamp , nonce ) ) {
        out.write ( echostr );
      }
    }
    out.close ( );
  }

  /**
   * 处理微信转发的内容
   * @param req
   * @param resp
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
    resp.setContentType ( "text/html;charset=utf-8" );
    req.setCharacterEncoding ( "UTF-8" );
    //调用核心业务类处理微信请求
    String respMsg = null;
    try {
      respMsg = WeChatBean.processRequest ( req );
    } catch (Exception e) {
      LOGGER.error("微信转发内容处理有误");
      e.printStackTrace();
    }
    LOGGER.info ( "微信公众号处理后的信息：" + respMsg );
    PrintWriter writer = resp.getWriter ( );
    writer.write ( respMsg );
    writer.close ( );
  }
}
