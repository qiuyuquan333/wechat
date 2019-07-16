package com.qyq.springbootapi.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES生成密钥，加解密工具类
 */
public class AesUtil {

    /**
     * 指定加密算法
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * 指定生成多少位的密钥
     */
    private static final int KEY_BIT = 128;

    /**
     * 随机生成AES密钥
     * @return
     * @throws Exception
     */
    public static String getGenerateKey()throws Exception{
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(KEY_BIT);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] encoded = secretKey.getEncoded();
        return Base64.encodeBase64String(encoded);
    }

    /**
     * 将String类型的密钥转化为SecretKey类型
     * @param generateKey
     * @return
     */
    private static SecretKey StringToSecretKey(String generateKey){
        byte[] bytes = Base64.decodeBase64(generateKey);
        SecretKeySpec spec = new SecretKeySpec(bytes,KEY_ALGORITHM);
        return  spec;
    }

    /**
     * 加密
     * @param data
     * @param generateKey
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String generateKey)throws Exception{
        SecretKey secretKey = StringToSecretKey(generateKey);

        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE,secretKey);
        byte[] aFinal = cipher.doFinal(data.getBytes("UTF-8"));
        return Base64.encodeBase64String(aFinal);
    }

    /**
     * 解密
     * @param data
     * @param generateKey
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, String generateKey)throws Exception{
        SecretKey secretKey = StringToSecretKey(generateKey);
        byte[] bytes = Base64.decodeBase64(data);

        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE,secretKey);
        byte[] aFinal = cipher.doFinal(bytes);
        return new String(aFinal,"UTF-8");
    }

}
