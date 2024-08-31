package cn.qihangerp.api.kwai.controller;


import com.alibaba.fastjson2.JSONObject;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface OrderApiService {

    @GetExchange("")
    String getOrderList();
    @GetExchange
    String getOrderDetail();
}
