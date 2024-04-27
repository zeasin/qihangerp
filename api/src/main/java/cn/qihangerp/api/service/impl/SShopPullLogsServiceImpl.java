package cn.qihangerp.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.api.domain.SShopPullLogs;
import cn.qihangerp.api.service.SShopPullLogsService;
import cn.qihangerp.api.mapper.SShopPullLogsMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
* @author TW
* @description 针对表【s_shop_pull_logs(店铺更新日志表)】的数据库操作Service实现
* @createDate 2024-04-25 10:08:31
*/
@AllArgsConstructor
@Service
public class SShopPullLogsServiceImpl extends ServiceImpl<SShopPullLogsMapper, SShopPullLogs>
    implements SShopPullLogsService{
    private final SShopPullLogsMapper mapper;

    @Override
    public PageResult<SShopPullLogs> queryPageList(SShopPullLogs bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SShopPullLogs> queryWrapper = new LambdaQueryWrapper<SShopPullLogs>();
        queryWrapper.eq(bo.getShopId()!=null,SShopPullLogs::getShopId,bo.getShopId());
        queryWrapper.eq(bo.getShopType()!=null,SShopPullLogs::getShopType,bo.getShopType());
        queryWrapper.eq(StringUtils.hasText(bo.getPullType()),SShopPullLogs::getPullType,bo.getPullType());
        Page<SShopPullLogs> pages = mapper.selectPage(pageQuery.build(), queryWrapper);

        return PageResult.build(pages);
    }
}




