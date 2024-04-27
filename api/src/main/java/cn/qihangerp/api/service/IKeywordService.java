package cn.qihangerp.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.qihangerp.api.domain.Keyword;
import cn.qihangerp.api.req.KeyWordListReq;

import java.util.List;

public interface IKeywordService {
    void add(Keyword keyword);
    void delById(String id);
    void editById(Keyword keyword);
    Page<Keyword> getPageList(KeyWordListReq req);
}
