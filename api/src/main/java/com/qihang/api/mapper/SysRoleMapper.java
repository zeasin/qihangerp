package com.qihang.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qihang.api.domain.SysRole;


import java.util.List;

/**
* @author TW
* @description 针对表【sys_role(角色信息表)】的数据库操作Mapper
* @createDate 2024-03-15 16:55:10
* @Entity com.qihang.sys.api.domain.SysRole
*/
public interface SysRoleMapper extends BaseMapper<SysRole> {
    List<SysRole> selectRolePermissionByUserId(Long userId);
}




