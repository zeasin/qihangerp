package cn.qihangerp.api.kwai.controller;

import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static javax.xml.crypto.dsig.SignatureMethod.HMAC_SHA256;

public class SignUtil {
    private static String PARAM = "param";
    private static String METHOD = "method";
    private static String APPKEY = "appkey";
    private static String ACCESS_TOKEN = "access_token";
    private static String VERSION = "version";
    private static String SIGN_METHOD = "signMethod";
    private static String TIMESTAMP = "timestamp";

    //签名计算
    public static String sign(String param, String signSecret) {
        StringBuffer sb = new StringBuffer();
        sb.append(param).append("&").append("signSecret").append("=").append(signSecret);
        String inputStr = sb.toString();
//        switch (signMethod) {
//            //HMAC_SHA256算法
//            case HMAC_SHA256:
                return HMACSHA256SignUtils.sign(inputStr, signSecret);
            //默认md5算法
//            case MD5:
//            default:
//                return org.apache.commons.codec.digest.DigestUtils.md5Hex(inputStr);
//        }
    }
//
    // 加签方法
//    public static String sign(Map<String, String> requestParamMap, String signSecret, SignMethodEnum signMethod) {
//        return sign(getSignParam(requestParamMap), signSecret, signMethod);
//    }
    public static String getSignParam(Map<String, String> requestParamMap) {
        String method = checkAndGetParam(requestParamMap, METHOD);
        String appKey = checkAndGetParam(requestParamMap, APPKEY);
        String accessToken = checkAndGetParam(requestParamMap, ACCESS_TOKEN);
        String version = requestParamMap.get(VERSION);
        String signMethod = requestParamMap.get(SIGN_METHOD);
        String timestamp = requestParamMap.get(TIMESTAMP);
        String param = requestParamMap.get(PARAM);
        Map<String, String> signMap = new HashMap<String, String>();
        // 必传参数
        signMap.put(METHOD, method);
        signMap.put(APPKEY, appKey);
        signMap.put(ACCESS_TOKEN, accessToken);
        //可选参数
        if (signMethod != null) {
            signMap.put(SIGN_METHOD, signMethod);
        }
        if (version != null) {
            signMap.put(VERSION, version);
        }
        if (timestamp != null) {
            signMap.put(TIMESTAMP, timestamp);
        }
        if (param != null) {
            signMap.put(PARAM, param);
        }
        String signParam =sortAndJoin(signMap);
        return signParam;
    }
    public static String checkAndGetParam(Map<String, String> paramMap, String paramKey) {
        String value = paramMap.get(paramKey);
        if (StringUtils.isEmpty(value)) {
            throw new IllegalArgumentException(paramKey + " not exist");
        }
        return value;
    }
    // 排序
    public static String sortAndJoin(Map<String, String> params) {
        TreeMap<String, String> paramsTreeMap = new TreeMap();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            paramsTreeMap.put(entry.getKey(), entry.getValue());
        }
        String signCalc = "";
        for (Map.Entry<String, String> entry : paramsTreeMap.entrySet()) {
            signCalc = String.format("%s%s=%s&", signCalc, entry.getKey(), entry.getValue(), "&");
        }
        if (signCalc.length() > 0) {
            signCalc = signCalc.substring(0, signCalc.length() - 1);
        }
        return signCalc;
    }




}
