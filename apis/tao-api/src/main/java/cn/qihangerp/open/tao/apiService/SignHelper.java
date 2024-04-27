package cn.qihangerp.open.tao.apiService;

import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;

public class SignHelper {
    public static String signTopRequest(Map<String, String> params, String secret) throws IOException {
        // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
//        if (Constants.SIGN_METHOD_MD5.equals(signMethod)) {
        //签名的摘要算法，可选值为：hmac，md5，hmac-sha256
        query.append(secret);
//        }
        for (String key : keys) {
            String value = params.get(key);
            if (StringUtils.hasText(key) && StringUtils.hasText(value)) {
                query.append(key).append(value);
            }
        }

        // 第三步：使用MD5/HMAC加密
        byte[] bytes;
//        if (Constants.SIGN_METHOD_HMAC.equals(signMethod)) {
//            bytes = encryptHMAC(query.toString(), secret);
//        } else {
        query.append(secret);
        bytes = encryptMD5(query.toString());
//        }

        // 第四步：把二进制转化为大写的十六进制(正确签名应该为32大写字符串，此方法需要时使用)
        return byte2hex(bytes);
    }

    public static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }

    public static byte[] encryptMD5(String data) throws IOException {
        return encryptMD5(data.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] encryptMD5(byte[] data) throws IOException {
        byte[] bytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            bytes = md.digest(data);
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse.toString());
        }
        return bytes;
    }
}
