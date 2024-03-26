package com.qihang.service.purchase.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qihang.interfaces.purchase.domain.ScmSupplier;
import com.qihang.interfaces.purchase.service.ScmSupplierService;

import com.qihang.service.purchase.mapper.ScmSupplierMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
* @author TW
* @description 针对表【scm_supplier】的数据库操作Service实现
* @createDate 2024-03-26 13:42:00
*/
@DubboService
@Service
public class ScmSupplierServiceImpl extends ServiceImpl<ScmSupplierMapper, ScmSupplier>
    implements ScmSupplierService{

}




