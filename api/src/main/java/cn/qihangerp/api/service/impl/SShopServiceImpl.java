package cn.qihangerp.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.domain.Shop;
import cn.qihangerp.api.service.SShopService;
import cn.qihangerp.api.mapper.SShopMapper;
import org.springframework.stereotype.Service;

/**
* @author TW
* @description 针对表【s_shop(电商平台店铺表)】的数据库操作Service实现
* @createDate 2024-06-04 11:28:03
*/
@Service
public class SShopServiceImpl extends ServiceImpl<SShopMapper, Shop>
    implements SShopService{

}




