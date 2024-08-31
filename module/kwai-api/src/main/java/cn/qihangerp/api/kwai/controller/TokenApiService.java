package cn.qihangerp.api.kwai.controller;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface TokenApiService {
    @GetExchange()
    String getToken();
}
