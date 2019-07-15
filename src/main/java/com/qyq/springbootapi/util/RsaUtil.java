package com.qyq.springbootapi.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA生成密钥，加解密工具类
 */
public class RsaUtil {

    /**
     * 指定加密算法
     */
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 指定生成多少位的密钥
     */
    private static final int KEY_BIT = 1024;

    /** *//**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /** *//**
     * RSA最大解密密文大小
     * 1024位的密钥要改成128,2048位的才是256
     */
    private static final int MAX_DECRYPT_BLOCK = 256;

    /** *//**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";


    /**
     * 生成密钥对
     * @return
     * @throws Exception
     */
    public static Map<String,Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(KEY_BIT);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();//生成公钥
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();//生成密钥
        Map<String,Object> map = new HashMap<>(2);
        map.put("publicKey",rsaPublicKey);
        map.put("privateKey",rsaPrivateKey);
        return map;
    }

    /**
     * 获取公钥
     * @param map
     * @return
     */
    public static String getPublicKey(Map<String,Object> map){
        Key key = (Key) map.get("publicKey");
        String publicKey = Base64.encodeBase64String(key.getEncoded());
        return publicKey;
    }

    /**
     * 获取私钥
     * @param map
     * @return
     */
    public static String getPrivate(Map<String,Object> map){
        Key key = (Key) map.get("privateKey");
        String privateKey = Base64.encodeBase64String(key.getEncoded());
        return privateKey;
    }

    /**
     * 公钥分段加密 --》公钥加密
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String publicKey) throws Exception {
        PublicKey publicK = StringToPublicKey(publicKey);

        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.getBytes("UTF-8").length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes("UTF-8"), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes("UTF-8"), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
        // 加密后的字符串
        return Base64.encodeBase64String(encryptedData);
    }

    /**
     * 私钥分段解密 --》私钥解密
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, String privateKey) throws Exception {
        PrivateKey privateK = StringToPrivateKey(privateKey);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateK);
        byte[] dataBytes = Base64.decodeBase64(data);//对要解密的数据进行解码
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        // 解密后的内容
        return new String(decryptedData, "UTF-8");
    }

    /**
     * 加签
     * @param data 数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String sign(String data,String privateKey)throws Exception{
        PrivateKey privateK = StringToPrivateKey(privateKey);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data.getBytes("UTF-8"));
        return Base64.encodeBase64String(signature.sign());
    }

    /**
     * 验证数字签名
     * @param data 数据
     * @param publicKey 公钥
     * @param sign 签名
     * @return
     * @throws Exception
     */
    public static boolean verify(String data,String publicKey,String sign)throws Exception{
        PublicKey publicK = StringToPublicKey(publicKey);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data.getBytes("UTF-8"));
        return signature.verify(Base64.decodeBase64(sign));
    }

    /**
     * String类型的公钥转换为PublicKey类型
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static PublicKey StringToPublicKey(String publicKey)throws Exception{
        byte[] bytes = Base64.decodeBase64(publicKey);//先把公钥进行解码
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(x509EncodedKeySpec);
        return publicK;
    }

    /**
     * String类型的私钥转换为PrivateKey类型
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static PrivateKey StringToPrivateKey(String privateKey)throws Exception{
        byte[] bytes = Base64.decodeBase64(privateKey);//先对私钥进行解码
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        return  privateK;
    }

}
