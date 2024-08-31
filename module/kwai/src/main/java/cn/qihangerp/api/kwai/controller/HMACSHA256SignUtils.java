package cn.qihangerp.api.kwai.controller;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HMACSHA256SignUtils {
//    protected static final Logger logger = Logger.getLogger(HMACSHA256SignUtils.class.getName());

    /**
     * hmac_sha256取hash Base64编码
     */
    public static String sign(String params, String secret) {
        String result = "";
        try {
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256HMAC.init(secretKey);
            byte[] sha256HMACBytes = sha256HMAC.doFinal(params.getBytes());
            String hash = Base64.encodeBase64String(sha256HMACBytes);
            return hash;
        } catch (Exception e) {
//            logger.warning("HMACSHA256SignUtils sign failed, params=" + params + ", error=" + e.getMessage());
        }
        return result;
    }
}
