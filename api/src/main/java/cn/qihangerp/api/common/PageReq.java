package cn.qihangerp.api.common;

import lombok.Data;

@Data
public class PageReq {
    private Integer currentPage;
    private Integer pageSize;
}
