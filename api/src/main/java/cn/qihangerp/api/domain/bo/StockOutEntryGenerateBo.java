package cn.qihangerp.api.domain.bo;

import lombok.Data;

import java.util.Date;

@Data
public class StockOutEntryGenerateBo {
    private String stockOutNum;
    private Date createTime;
    private Long[] orderItemIds;
}
