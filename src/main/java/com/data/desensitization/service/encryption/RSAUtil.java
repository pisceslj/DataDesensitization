package com.data.desensitization.service.encryption;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Arrays;

import org.apache.tomcat.util.codec.binary.Base64;

public class RSAUtil {
	public static String encrypt(String source, PublicKey key, String charset) {
        byte[] sourceByte = null;
        try {
            sourceByte = source.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
           }
        BigInteger temp = new BigInteger(1,sourceByte);
        BigInteger encrypted = temp.modPow(key.getB(), key.getN());
        BigInteger nums = new BigInteger("666666"); 
        System.out.println(encrypted.mod(nums));
        return Base64.encodeBase64String(encrypted.toByteArray());
	}
    
	public static String decrypt(String cryptdata, PrivateKey key, String charset) throws UnsupportedEncodingException {
        byte[] byteTmp = Base64.decodeBase64(cryptdata);
        BigInteger cryptedBig = new BigInteger(byteTmp);
        byte[] cryptedData = cryptedBig.modPow(key.getA(), key.getN()).toByteArray();
        return new String(cryptedData,charset);     
    }
}
