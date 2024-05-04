package cn.qihangerp.mq;

import lombok.Data;

import java.io.Serializable;

@Data
public class MQRequest<T> implements Serializable {
    private int mqRequestType;//业务标识
    private T data;//业务数据
}
