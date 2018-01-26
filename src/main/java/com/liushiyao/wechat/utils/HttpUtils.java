package com.liushiyao.wechat.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.liushiyao.wechat.MyX509TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import org.apache.log4j.Logger;

public class HttpUtils {

  public static final Logger LOGGER = Logger.getLogger(HttpUtils.class);

  /**
   * 发起https请求
   * @param requestUrl 请求地址
   * @param requestMethod 请求方式（Get或者post）
   * @param outputStr 提交数据
   * @return
   */
  public static JsonObject httpsRequest ( String requestUrl, String requestMethod, String outputStr ) {
    JsonObject jsonObject = null;
    StringBuffer buffer = new StringBuffer ( );
    try {
      //创建SSLcontext管理器对像，使用我们指定的信任管理器初始化
      TrustManager[] tm = { new MyX509TrustManager( ) };
      SSLContext sslContext = SSLContext.getInstance ( "SSL", "SunJSSE" );
      sslContext.init ( null, tm, new java.security.SecureRandom ( ) );
      SSLSocketFactory ssf = sslContext.getSocketFactory ( );

      URL url = new URL( requestUrl );
      HttpsURLConnection httpsUrlConn = ( HttpsURLConnection ) url.openConnection ( );
      httpsUrlConn.setSSLSocketFactory ( ssf );
      httpsUrlConn.setDoInput ( true );
      httpsUrlConn.setDoOutput ( true );
      httpsUrlConn.setUseCaches ( false );
      //设置请求方式（GET/POST）
      httpsUrlConn.setRequestMethod ( requestMethod );
      if ( "GET".equalsIgnoreCase ( requestMethod ) ) {
        httpsUrlConn.connect ( );
      }

      //当有数据需要提交时
      if ( outputStr != null ) {
        OutputStream outputStream = httpsUrlConn.getOutputStream ( );
        //防止中文乱码
        outputStream.write ( outputStr.getBytes ( "UTF-8" ) );
        outputStream.close ( );
        outputStream = null;
      }

      //将返回的输入流转换成字符串
      InputStream inputStream = httpsUrlConn.getInputStream ( );
      InputStreamReader inputStreamReader = new InputStreamReader ( inputStream, "UTF-8" );
      BufferedReader bufferedReader = new BufferedReader ( inputStreamReader );

      String str = null;
      while ( ( str = bufferedReader.readLine ( ) ) != null ) {
        buffer.append ( str );
      }

      bufferedReader.close ( );
      inputStreamReader.close ( );

      inputStream.close ( );
      inputStream = null;

      httpsUrlConn.disconnect ( );
      JsonElement jsonElement = new JsonParser().parse ( buffer.toString () );
      jsonObject = jsonElement.getAsJsonObject ();
      LOGGER.info (jsonObject );

    } catch ( ConnectException ce ) {
      // TODO: handle exception
      LOGGER.error ( "Weixin server connection timed out." );
    } catch ( Exception e ) {
      // TODO: handle exception
      LOGGER.error ( "https request error:{}", e );
    }

    return jsonObject;
  }

}
