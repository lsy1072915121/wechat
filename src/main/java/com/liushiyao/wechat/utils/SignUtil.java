package com.liushiyao.wechat.utils;

import java.security.MessageDigest;
import java.util.Arrays;
import org.apache.log4j.Logger;

public class SignUtil {

  // 与微信接口配置信息中的Token要一致

  private static final Logger LOGGER = Logger.getLogger(SignUtil.class);

  public static boolean checkSignature(String signature, String timeStamp, String nonce) {
    String[] arr = new String[]{PropertyUtil.getProperty("TOKEN"), timeStamp, nonce};
    LOGGER.info("==========token,timeStamp,nonce按字典序排序==========");
    LOGGER.info(Arrays.toString(arr));
    //将token,timeStamp,nonce按字典序排序
    Arrays.sort(arr);
    StringBuffer contentBuffer = new StringBuffer();
    for (int i = 0; i < arr.length; i++) {
      contentBuffer.append(arr[i]);
    }

    MessageDigest mDigest = null;
    String tmpString = null;
    try {
      mDigest = MessageDigest.getInstance("SHA-1");
      //将三个参数拼接成一个字符串进行SHA-1加密
      byte[] digest = mDigest.digest(contentBuffer.toString().getBytes());
      tmpString = StringUtil.byteToStr(digest);
      LOGGER.info("==========加密后的结果==========");
      LOGGER.info("tmpString:" + tmpString);
      LOGGER.info("signature.toUpperCase:" + signature.toUpperCase());


    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
    }
    contentBuffer = null;
    // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
    return tmpString != null ? tmpString.equals(signature.toUpperCase()) : false;


  }


}