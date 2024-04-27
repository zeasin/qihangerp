package cn.qihangerp.api.req;

import cn.qihangerp.api.common.PageReq;
import lombok.Data;

import java.util.Date;

@Data
public class KeyWordListReq extends PageReq {
    private String word;
    private String source;
    private String date;
}
