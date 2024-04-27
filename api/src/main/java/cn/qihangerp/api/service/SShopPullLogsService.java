package cn.qihangerp.api.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.api.domain.SShopPullLogs;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author TW
* @description 针对表【s_shop_pull_logs(店铺更新日志表)】的数据库操作Service
* @createDate 2024-04-25 10:08:31
*/
public interface SShopPullLogsService extends IService<SShopPullLogs> {
    PageResult<SShopPullLogs> queryPageList(SShopPullLogs bo, PageQuery pageQuery);
}
