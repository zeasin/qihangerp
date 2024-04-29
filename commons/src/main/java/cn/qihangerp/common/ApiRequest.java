package cn.qihangerp.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApiRequest<T> implements Serializable {
    private int type;//业务标识101保存订单102确认订单
    private T data;//业务数据
}
