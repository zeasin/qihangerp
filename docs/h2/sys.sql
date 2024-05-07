
-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100)  DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100)  DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500)  DEFAULT '' COMMENT '参数键值',
  `config_type` char(1)  DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64)  DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64)  DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) 
) ;
-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-blue', 'Y', 'admin', '2023-08-07 19:31:38', '', NULL, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2023-08-07 19:31:38', '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-dark', 'Y', 'admin', '2023-08-07 19:31:38', '', NULL, '深色主题theme-dark，浅色主题theme-light');
INSERT INTO `sys_config` VALUES (4, '账号自助-验证码开关', 'sys.account.captchaEnabled', 'true', 'Y', 'admin', '2023-08-07 19:31:38', '', NULL, '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (5, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 'admin', '2023-08-07 19:31:38', '', NULL, '是否开启注册用户功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (6, '用户登录-黑名单列表', 'sys.login.blackIPList', '', 'Y', 'admin', '2023-08-07 19:31:38', '', NULL, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint(0) DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(50)  DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30)  DEFAULT '' COMMENT '部门名称',
  `order_num` int(0) DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20)  DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11)  DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50)  DEFAULT NULL COMMENT '邮箱',
  `status` char(1)  DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1)  DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64)  DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64)  DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) 
  ) ;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', '启航技术', 0, '启航', '15888888888', '280645618@qq.com', '0', '0', 'admin', '2023-08-07 19:31:37', 'admin', '2024-04-14 18:28:55');
INSERT INTO `sys_dept` VALUES (101, 100, '0,100', '研发部', 1, '启航', '15888888888', '280645618@qq.com', '0', '0', 'admin', '2023-08-07 19:31:37', 'admin', '2024-04-14 18:31:54');
INSERT INTO `sys_dept` VALUES (102, 100, '0,100', '长沙分公司', 2, '至简', '15888888888', 'ry@qq.com', '0', '2', 'admin', '2023-08-07 19:31:37', '', NULL);
INSERT INTO `sys_dept` VALUES (103, 101, '0,100,101', '研发部门', 1, '至简', '15888888888', 'ry@qq.com', '0', '2', 'admin', '2023-08-07 19:31:37', '', NULL);
INSERT INTO `sys_dept` VALUES (104, 101, '0,100,101', '市场部门', 2, '至简', '15888888888', 'ry@qq.com', '0', '2', 'admin', '2023-08-07 19:31:37', '', NULL);
INSERT INTO `sys_dept` VALUES (105, 101, '0,100,101', '测试部门', 3, '至简', '15888888888', 'ry@qq.com', '0', '2', 'admin', '2023-08-07 19:31:37', '', NULL);
INSERT INTO `sys_dept` VALUES (106, 101, '0,100,101', '财务部门', 4, '至简', '15888888888', 'ry@qq.com', '0', '2', 'admin', '2023-08-07 19:31:37', '', NULL);
INSERT INTO `sys_dept` VALUES (107, 101, '0,100,101', '运维部门', 5, '至简', '15888888888', 'ry@qq.com', '0', '2', 'admin', '2023-08-07 19:31:37', '', NULL);
INSERT INTO `sys_dept` VALUES (108, 102, '0,100,102', '市场部门', 1, '至简', '15888888888', 'ry@qq.com', '0', '2', 'admin', '2023-08-07 19:31:37', '', NULL);
INSERT INTO `sys_dept` VALUES (109, 102, '0,100,102', '财务部门', 2, '至简', '15888888888', 'ry@qq.com', '0', '2', 'admin', '2023-08-07 19:31:37', '', NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int(0) DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100)  DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100)  DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100)  DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100)  DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100)  DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1)  DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1)  DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64)  DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64)  DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) 
) ;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '默认分组');
INSERT INTO `sys_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '系统分组');
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '通知');
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '公告');
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '关闭状态');
INSERT INTO `sys_dict_data` VALUES (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '其他操作');
INSERT INTO `sys_dict_data` VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '新增操作');
INSERT INTO `sys_dict_data` VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '修改操作');
INSERT INTO `sys_dict_data` VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '删除操作');
INSERT INTO `sys_dict_data` VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '授权操作');
INSERT INTO `sys_dict_data` VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '导出操作');
INSERT INTO `sys_dict_data` VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '导入操作');
INSERT INTO `sys_dict_data` VALUES (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '强退操作');
INSERT INTO `sys_dict_data` VALUES (26, 8, '生成代码', '8', 'sys_oper_type', '', 'warning', 'N', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '生成操作');
INSERT INTO `sys_dict_data` VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '清空操作');
INSERT INTO `sys_dict_data` VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '停用状态');

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100)  DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100)  DEFAULT '' COMMENT '字典类型',
  `status` char(1)  DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64)  DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64)  DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) ,
  UNIQUE INDEX `dict_type`(`dict_type`) 
) ;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '任务状态列表');
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '任务分组列表');
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '通知类型列表');
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '通知状态列表');
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '操作类型列表');
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', 'admin', '2023-08-07 19:31:38', '', NULL, '登录状态列表');

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `job_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64)  NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64)  NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500)  NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255)  DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20)  DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1)  DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1)  DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64)  DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64)  DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500)  DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`, `job_name`, `job_group`) 
) ;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES (1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '0/10 * * * * ?', '1', '1', '1', 'admin', '2023-08-07 19:31:38', 'admin', '2024-01-28 13:30:18', '');
INSERT INTO `sys_job` VALUES (2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', 'admin', '2023-08-07 19:31:38', '', NULL, '');
INSERT INTO `sys_job` VALUES (3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', 'admin', '2023-08-07 19:31:38', '', NULL, '');
INSERT INTO `sys_job` VALUES (100, '库存存货日报生成', 'SYSTEM', 'inventoryReportTask.run()', '0/30 * * * * ?', '1', '1', '1', 'admin', '2024-01-28 13:44:43', '', '2024-01-28 13:47:13', '');

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log`  (
  `job_log_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64)  NOT NULL COMMENT '任务名称',
  `job_group` varchar(64)  NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500)  NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500)  DEFAULT NULL COMMENT '日志信息',
  `status` char(1)  DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000)  DEFAULT '' COMMENT '异常信息',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`) 
) ;

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------
INSERT INTO `sys_job_log` VALUES (1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '系统默认（无参） 总共耗时：2毫秒', '0', '', '2024-01-28 13:30:00');
INSERT INTO `sys_job_log` VALUES (2, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams', '系统默认（无参） 总共耗时：0毫秒', '0', '', '2024-01-28 13:30:10');
INSERT INTO `sys_job_log` VALUES (3, '库存存货日报生成', 'SYSTEM', 'inventoryReportTask.run()', '库存存货日报生成 总共耗时：2毫秒', '0', '', '2024-01-28 13:45:00');
INSERT INTO `sys_job_log` VALUES (4, '库存存货日报生成', 'SYSTEM', 'inventoryReportTask.run()', '库存存货日报生成 总共耗时：0毫秒', '0', '', '2024-01-28 13:45:30');
INSERT INTO `sys_job_log` VALUES (5, '库存存货日报生成', 'SYSTEM', 'inventoryReportTask.run()', '库存存货日报生成 总共耗时：2毫秒', '0', '', '2024-01-28 13:46:00');
INSERT INTO `sys_job_log` VALUES (6, '库存存货日报生成', 'SYSTEM', 'inventoryReportTask.run()', '库存存货日报生成 总共耗时：0毫秒', '0', '', '2024-01-28 13:46:30');
INSERT INTO `sys_job_log` VALUES (7, '库存存货日报生成', 'SYSTEM', 'inventoryReportTask.run()', '库存存货日报生成 总共耗时：0毫秒', '0', '', '2024-01-28 13:47:00');

-- ----------------------------
-- Table structure for sys_logininfor
-- ----------------------------
DROP TABLE IF EXISTS `sys_logininfor`;
CREATE TABLE `sys_logininfor`  (
  `info_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50)  DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128)  DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255)  DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50)  DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50)  DEFAULT '' COMMENT '操作系统',
  `status` char(1)  DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255)  DEFAULT '' COMMENT '提示消息',
  `login_time` datetime(0) DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) ,
  INDEX `idx_sys_logininfor_s`(`status`) ,
  INDEX `idx_sys_logininfor_lt`(`login_time`) 
) ;

-- ----------------------------
-- Records of sys_logininfor
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50)  NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(0) DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int(0) DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200)  DEFAULT '' COMMENT '路由地址',
  `component` varchar(255)  DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255)  DEFAULT NULL COMMENT '路由参数',
  `is_frame` int(0) DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int(0) DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1)  DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1)  DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1)  DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100)  DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100)  DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64)  DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64)  DEFAULT '' COMMENT '更新者',
  `update_time` varchar(64)  DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500)  DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) 
) ;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 99, 'system', '', '', 1, 0, 'M', '0', '0', '', 'system', 'admin', '2023-12-27 15:00:27', 'admin', '2023-12-29 09:07:42.856856', '系统管理目录');
INSERT INTO `sys_menu` VALUES (2, '商品管理', 0, 9, 'goods', '', '', 1, 0, 'M', '0', '0', '', 'theme', 'admin', '2023-12-29 13:29:44', '', '', '');
INSERT INTO `sys_menu` VALUES (4, '采购管理', 0, 1, 'purchase', '', '', 1, 0, 'M', '0', '0', '', 'server', 'admin', '2023-12-27 15:00:27', 'admin', '2024-04-24 17:06:13', '至简官网地址');
INSERT INTO `sys_menu` VALUES (5, '订单管理', 0, 2, 'sale', '', '', 1, 0, 'M', '0', '0', '', 'shopping', 'admin', '2023-12-29 16:53:03', 'admin', '2024-03-24 19:31:53', '');
INSERT INTO `sys_menu` VALUES (6, '发货管理', 0, 3, 'ship', '', '', 1, 0, 'M', '0', '0', '', 'excel', 'admin', '2024-01-01 14:08:04', 'admin', '2024-01-03 14:07:29', '');
INSERT INTO `sys_menu` VALUES (7, '售后管理', 0, 4, 'saleafter', NULL, NULL, 1, 0, 'M', '0', '0', '', 'clipboard', 'admin', '2024-01-03 14:23:55', 'admin', '2024-01-12 19:47:33', '');
INSERT INTO `sys_menu` VALUES (8, '店铺管理', 0, 7, 'shop', NULL, NULL, 1, 0, 'M', '0', '0', '', 'server', 'admin', '2024-04-12 16:46:19', 'admin', '2024-04-24 11:17:09', '');
INSERT INTO `sys_menu` VALUES (9, '库存管理', 0, 5, 'wms', '', '', 1, 0, 'M', '0', '0', '', 'dict', 'admin', '2023-12-31 12:14:33', 'admin', '2024-04-23 15:43:32', '');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', '2023-12-27 15:00:27', '', '', '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', '2023-12-27 15:00:27', '', '', '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', '2023-12-27 15:00:27', '', '', '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin', '2023-12-27 15:00:27', '', '', '部门管理菜单');
INSERT INTO `sys_menu` VALUES (104, '岗位管理', 1, 5, 'post', 'system/post/index', '', 1, 0, 'C', '1', '0', 'system:post:list', 'post', 'admin', '2023-12-27 15:00:27', 'admin', '2024-04-12 16:44:52', '岗位管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin', '2023-12-27 15:00:27', '', '', '字典管理菜单');
INSERT INTO `sys_menu` VALUES (108, '日志管理', 1, 9, 'log', '', '', 1, 0, 'M', '0', '0', '', 'log', 'admin', '2023-12-27 15:00:27', '', '', '日志管理菜单');
INSERT INTO `sys_menu` VALUES (500, '操作日志', 108, 1, 'operlog', 'monitor/operlog/index', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list', 'form', 'admin', '2023-12-27 15:00:27', '', '', '操作日志菜单');
INSERT INTO `sys_menu` VALUES (501, '登录日志', 108, 2, 'logininfor', 'monitor/logininfor/index', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor', 'admin', '2023-12-27 15:00:27', '', '', '登录日志菜单');
INSERT INTO `sys_menu` VALUES (1000, '用户查询', 100, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1001, '用户新增', 100, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1002, '用户修改', 100, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1003, '用户删除', 100, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1004, '用户导出', 100, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1005, '用户导入', 100, 6, '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1006, '重置密码', 100, 7, '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1007, '角色查询', 101, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1008, '角色新增', 101, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1009, '角色修改', 101, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1010, '角色删除', 101, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1011, '角色导出', 101, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1012, '菜单查询', 102, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1013, '菜单新增', 102, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1014, '菜单修改', 102, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1015, '菜单删除', 102, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1016, '部门查询', 103, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1017, '部门新增', 103, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1018, '部门修改', 103, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1019, '部门删除', 103, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1020, '岗位查询', 104, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1021, '岗位新增', 104, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1022, '岗位修改', 104, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1023, '岗位删除', 104, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1024, '岗位导出', 104, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1025, '字典查询', 105, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1026, '字典新增', 105, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1027, '字典修改', 105, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1028, '字典删除', 105, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1029, '字典导出', 105, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1039, '操作查询', 500, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1040, '操作删除', 500, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1041, '日志导出', 500, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1042, '登录查询', 501, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1043, '登录删除', 501, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1044, '日志导出', 501, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (1045, '账户解锁', 501, 4, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock', '#', 'admin', '2023-12-27 15:00:27', '', '', '');
INSERT INTO `sys_menu` VALUES (2005, '供应商管理', 4, 9, 'supplier/list', 'scm/supplier/index', '', 1, 0, 'C', '0', '0', 'scm:supplier', 'qq', 'admin', '2023-12-29 09:14:02', 'admin', '2023-12-29 09:17:27.514466', '');
INSERT INTO `sys_menu` VALUES (2007, '商品管理', 2, 1, 'goods_list', 'goods/index', '', 1, 0, 'C', '0', '0', 'goods:list', 'theme', 'admin', '2023-12-29 13:31:01', 'admin', '2023-12-29 15:02:40.869685', '');
INSERT INTO `sys_menu` VALUES (2008, '商品分类', 2, 88, 'goods_category', 'goods/category/index', '', 1, 0, 'C', '0', '0', 'goods:category', 'tree-table', 'admin', '2023-12-29 13:32:41', 'admin', '2023-12-29 15:02:22.220534', '');
INSERT INTO `sys_menu` VALUES (2009, '品牌管理', 2, 99, 'goods/brand', 'goods/brand/index', '', 1, 0, 'C', '0', '1', 'goods:brand', 'clipboard', 'admin', '2023-12-29 13:34:49', 'admin', '2024-04-14 18:51:23', '');
INSERT INTO `sys_menu` VALUES (2010, '采购单管理', 4, 1, 'order', 'scm/purchase/order', '', 1, 0, 'C', '0', '0', 'scm:purchase:order', 'button', 'admin', '2023-12-29 16:35:55', 'admin', '2024-04-24 17:06:32', '');
INSERT INTO `sys_menu` VALUES (2012, '采购物流管理', 4, 2, 'ship', 'scm/purchase/ship', '', 1, 0, 'C', '0', '0', 'scm:purchase:ship', 'component', 'admin', '2023-12-29 16:45:42', 'admin', '2024-04-24 17:10:48', '');
INSERT INTO `sys_menu` VALUES (2015, '店铺管理', 8, 10, 'shop_list', 'shop/shop_index', '', 1, 0, 'C', '0', '0', 'shop:list', 'example', 'admin', '2023-12-29 16:54:02', 'admin', '2024-04-25 11:15:07', '');
INSERT INTO `sys_menu` VALUES (2017, '店铺数据统计', 5, 8, 'data', 'shop/data', '', 1, 0, 'C', '0', '1', 'shop:data', 'chart', 'admin', '2023-12-29 17:04:08', 'admin', '2023-12-31 19:04:58.785609', '');
INSERT INTO `sys_menu` VALUES (2019, '采购账单管理1', 4, 2, 'purchase', 'scm/purchase/cost', '', 1, 0, 'C', '0', '1', '', 'money', 'admin', '2023-12-29 17:09:32', 'admin', '2024-04-24 17:05:06', '');
INSERT INTO `sys_menu` VALUES (2021, '创建采购单', 4, 0, 'order/create', 'scm/purchase/order/create', '', 1, 0, 'C', '1', '0', '', 'edit', 'admin', '2023-12-29 21:23:45', 'admin', '2024-04-24 17:11:38', '');
INSERT INTO `sys_menu` VALUES (2025, '采购单详情', 4, 1, 'order/detail', 'scm/purchase/order/detail', '', 1, 0, 'C', '1', '0', '', 'button', 'admin', '2023-12-30 17:08:01', 'admin', '2024-04-24 17:15:00', '');
INSERT INTO `sys_menu` VALUES (2027, '入库管理', 9, 0, 'stock_in_entry/list', 'wms/stockInEntry', '', 1, 0, 'C', '0', '0', 'wms:stock_in_entry:list', 'stockin', 'admin', '2023-12-31 12:27:37', 'admin', '2024-01-14 15:12:48', '');
INSERT INTO `sys_menu` VALUES (2028, '生成采购入库单', 4, 3, 'ship/create_stock_in_entry', 'scm/purchase/ship/create_stock_in_entry', '', 1, 0, 'C', '1', '0', '', 'button', 'admin', '2023-12-31 12:31:32', 'admin', '2024-04-24 17:12:38', '');
INSERT INTO `sys_menu` VALUES (2029, '店铺售后管理', 7, 2, 'shop_refund', 'shop/refund/index', '', 1, 0, 'C', '0', '0', '', 'clipboard', 'admin', '2023-12-31 17:29:03', 'admin', '2024-05-04 18:57:32', '');
INSERT INTO `sys_menu` VALUES (2030, '手动创建订单', 5, 1, 'order/create', 'sale/order/create', '', 1, 0, 'C', '1', '0', '', 'documentation', 'admin', '2023-12-31 20:01:22', 'admin', '2024-03-24 19:32:32', '');
INSERT INTO `sys_menu` VALUES (2032, '订单拉取日志', 5, 3, 'order/pull_log', 'shop/order_pull_log', '', 1, 0, 'C', '0', '1', '', 'upload', 'admin', '2023-12-31 20:04:12', 'admin', '2024-05-04 18:56:46', '');
INSERT INTO `sys_menu` VALUES (2033, '订单管理', 5, 1, 'order_list', 'sale/order/index', '', 1, 0, 'C', '0', '0', '', 'list', 'admin', '2023-12-31 20:05:05', 'admin', '2024-05-05 18:07:27', '');
INSERT INTO `sys_menu` VALUES (2036, '店铺订单管理', 5, 2, 'shop_order', 'shop/order_index', '', 1, 0, 'C', '0', '0', '', 'excel', 'admin', '2024-01-01 14:14:42', 'admin', '2024-05-04 18:55:37', '');
INSERT INTO `sys_menu` VALUES (2046, '出库管理', 9, 5, 'stockOut', 'wms/stockOutEntry', NULL, 1, 0, 'C', '0', '0', '', 'link', 'admin', '2024-01-03 11:00:53', 'admin', '2024-01-12 15:52:19', '');
INSERT INTO `sys_menu` VALUES (2047, '库存查询', 9, 6, 'goodsInventory', 'goods/goodsInventory', NULL, 1, 0, 'C', '0', '0', '', 'monitor', 'admin', '2024-01-03 11:01:14', 'admin', '2024-01-09 17:55:33', '');
INSERT INTO `sys_menu` VALUES (2048, '库存盘点', 9, 9, 'pan', NULL, NULL, 1, 0, 'C', '0', '1', '', 'bug', 'admin', '2024-01-03 11:01:43', 'admin', '2024-01-09 19:57:08', '');
INSERT INTO `sys_menu` VALUES (2049, '打包发货', 6, 3, 'ship_order', 'shipping/shipOrder/index', NULL, 1, 0, 'C', '0', '0', '', 'guide', 'admin', '2024-01-03 14:09:18', 'admin', '2024-05-02 10:47:38', '');
INSERT INTO `sys_menu` VALUES (2051, '物流跟踪', 6, 4, 'ship_logistics', 'shipping/shipOrder/logistics', NULL, 1, 0, 'C', '0', '0', '', 'email', 'admin', '2024-01-03 14:13:12', 'admin', '2024-05-02 10:50:34', '');
INSERT INTO `sys_menu` VALUES (2052, '物流公司管理', 6, 9, 'logistics_company', 'shipping/logistics/company', NULL, 1, 0, 'C', '0', '0', '', 'checkbox', 'admin', '2024-01-03 14:14:09', 'admin', '2024-04-24 13:53:14', '');
INSERT INTO `sys_menu` VALUES (2054, '售后管理', 7, 1, 'refund_list', 'sale/refund', NULL, 1, 0, 'C', '0', '0', '', 'size', 'admin', '2024-01-03 14:24:36', 'admin', '2024-05-05 20:14:23', '');
INSERT INTO `sys_menu` VALUES (2059, '备货清单', 6, 1, 'stocking', 'shipping/stocking', '', 1, 0, 'C', '0', '0', '', 'component', 'admin', '2024-01-09 11:51:52', 'admin', '2024-05-01 21:34:33', '');
INSERT INTO `sys_menu` VALUES (2060, '拣货出库', 6, 2, 'stockout', 'shipping/stockOut', NULL, 1, 0, 'C', '0', '0', '', 'bug', 'admin', '2024-01-09 13:39:00', 'admin', '2024-04-26 13:51:21', '');
INSERT INTO `sys_menu` VALUES (2061, '库位管理', 9, 99, 'stock_location', 'wms/location', NULL, 1, 0, 'C', '0', '0', '', 'education', 'admin', '2024-01-09 13:54:30', 'admin', '2024-01-09 14:50:33', '');
INSERT INTO `sys_menu` VALUES (2063, '发货费用', 6, 5, 'ship_fee', 'shipping/shipFee', NULL, 1, 0, 'C', '0', '0', '', 'money', 'admin', '2024-01-12 18:35:31', 'admin', '2024-05-02 10:50:58', '');
INSERT INTO `sys_menu` VALUES (2066, '添加商品', 2, 2, 'create', 'goods/create', NULL, 1, 0, 'C', '1', '0', '', 'component', 'admin', '2024-01-14 19:42:11', 'admin', '2024-04-14 18:50:36', '');
INSERT INTO `sys_menu` VALUES (2067, '商品SKU管理', 2, 3, 'spec_list', 'goods/spec', NULL, 1, 0, 'C', '0', '0', '', 'theme', 'admin', '2024-01-16 14:17:39', 'admin', '2024-04-14 18:51:13', '');
INSERT INTO `sys_menu` VALUES (2079, '平台设置', 8, 20, 'platform', 'shop/platform', NULL, 1, 0, 'C', '0', '0', NULL, 'date-range', 'admin', '2024-04-12 16:58:07', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2080, '售后处理查询', 7, 3, 'result_list', 'afterSale/index', NULL, 1, 0, 'C', '0', '0', '', 'time', 'admin', '2024-04-15 14:23:40', 'admin', '2024-05-05 20:27:37', '');
INSERT INTO `sys_menu` VALUES (2084, '店铺商品管理', 8, 1, 'goods_list', 'shop/goods/', NULL, 1, 0, 'C', '0', '0', '', 'shopping', 'admin', '2024-04-15 14:52:16', 'admin', '2024-04-15 14:52:27', '');
INSERT INTO `sys_menu` VALUES (2085, '采购账单管理', 4, 4, 'bill', 'scm/purchase/bill', NULL, 1, 0, 'C', '0', '0', '', 'money', 'admin', '2024-04-24 17:03:07', 'admin', '2024-05-02 10:59:08', '');

-- ----------------------------
-- Table structure for sys_menu_oms
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu_oms`;
CREATE TABLE `sys_menu_oms`  (
  `menu_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50)  NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(0) DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int(0) DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200)  DEFAULT '' COMMENT '路由地址',
  `component` varchar(255)  DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255)  DEFAULT NULL COMMENT '路由参数',
  `is_frame` int(0) DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int(0) DEFAULT 0 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1)  DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1)  DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1)  DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100)  DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100)  DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64)  DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64)  DEFAULT '' COMMENT '更新者',
  `update_time` varchar(64)  DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500)  DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) 
) ;

-- ----------------------------
-- Records of sys_menu_oms
-- ----------------------------
INSERT INTO `sys_menu_oms` VALUES (1, '订单管理', 0, 1, '/order', 'Layout', '', 1, 0, 'M', '0', '0', '', 'shopping', 'admin', '2023-12-27 15:00:27', 'admin', '2024-03-30 17:44:37', '系统管理目录');
INSERT INTO `sys_menu_oms` VALUES (2, '售后管理', 0, 2, '/refund', 'Layout', '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', '2023-12-27 15:00:27', 'admin', '2024-04-06 14:49:33', '至简官网地址');
INSERT INTO `sys_menu_oms` VALUES (3, '店铺管理', 0, 5, 'shop', 'Layout', '', 1, 0, 'M', '0', '0', '', 'dict', 'admin', '2023-12-29 13:29:44', 'admin', '2024-03-30 17:43:35', '');
INSERT INTO `sys_menu_oms` VALUES (4, '商品管理', 0, 4, '/goods', 'Layout', '', 1, 0, 'M', '0', '0', '', 'build', 'admin', '2023-12-29 16:53:03', 'admin', '2024-03-30 17:43:57', '');
INSERT INTO `sys_menu_oms` VALUES (5, '系统设置', 0, 99, '/system', 'Layout', '', 1, 0, 'M', '0', '0', '', 'system', 'admin', '2023-12-27 15:00:27', 'admin', '2023-12-29 09:07:42.856856', '系统管理目录');
INSERT INTO `sys_menu_oms` VALUES (100, '订单处理', 1, 1, 'order_list', 'order/index', '', 1, 0, 'C', '0', '0', '', 'shopping', 'admin', '2023-12-27 15:00:27', 'admin', '2024-04-06 17:25:10', '用户管理菜单');
INSERT INTO `sys_menu_oms` VALUES (101, '订单更新日志', 1, 4, 'pull_logs', 'order/shopOrder/pull_log', '', 1, 0, 'C', '0', '0', '', 'peoples', 'admin', '2023-12-27 15:00:27', 'admin', '2024-04-06 11:19:26', '角色管理菜单');
INSERT INTO `sys_menu_oms` VALUES (102, '店铺订单管理', 1, 3, 'shop_order_list', 'order/shopOrder/index', '', 1, 0, 'C', '0', '0', '', 'monitor', 'admin', '2023-12-27 15:00:27', 'admin', '2024-04-06 11:18:00', '菜单管理菜单');
INSERT INTO `sys_menu_oms` VALUES (104, '退款查询', 2, 1, 'refund_list', 'refund/index', '', 1, 0, 'C', '0', '0', '', 'tree', 'admin', '2023-12-27 15:00:27', 'admin', '2024-04-06 17:27:38', '岗位管理菜单');
INSERT INTO `sys_menu_oms` VALUES (105, '退款更新日志', 2, 6, 'pull_logs', 'refund/shopRefund/pull_log', '', 1, 0, 'C', '0', '0', '', 'dict', 'admin', '2023-12-27 15:00:27', 'admin', '2024-04-06 15:48:34', '字典管理菜单');
INSERT INTO `sys_menu_oms` VALUES (106, '店铺退款管理', 2, 2, 'shop_refund_list', 'refund/shopRefund/index', '', 1, 0, 'C', '0', '0', '', 'edit', 'admin', '2023-12-27 15:00:27', 'admin', '2024-04-06 15:48:21', '参数设置菜单');
INSERT INTO `sys_menu_oms` VALUES (108, '店铺列表', 3, 9, 'shop_list', 'shop/index', '', 1, 0, 'C', '0', '0', '', 'tree', 'admin', '2023-12-29 09:14:02', 'admin', '2024-03-21 17:01:52', '');
INSERT INTO `sys_menu_oms` VALUES (109, '店铺商品管理', 3, 10, 'goods', '', '', 1, 0, 'M', '1', '0', '', 'peoples', 'admin', '2023-12-29 13:31:01', 'admin', '2024-03-22 10:45:01', '');
INSERT INTO `sys_menu_oms` VALUES (110, '开放平台设置', 3, 88, 'platform_setting', 'shop/platform/index', '', 1, 0, 'C', '0', '0', '', 'peoples', 'admin', '2023-12-29 13:32:41', 'admin', '2024-03-21 13:46:05', '');
INSERT INTO `sys_menu_oms` VALUES (112, 'ERP商品SKU', 4, 1, 'sku_list', 'goods/spec/index', '', 1, 0, 'C', '0', '0', '', 'tree', 'admin', '2023-12-29 16:35:55', 'admin', '2024-03-21 11:40:29', '');
INSERT INTO `sys_menu_oms` VALUES (116, '用户管理', 5, 1, 'user', 'system/user/index', '', 1, 0, 'C', '0', '0', '', 'user', 'admin', '2023-12-27 15:00:27', '', '', '用户管理菜单');
INSERT INTO `sys_menu_oms` VALUES (117, '菜单管理', 5, 1, 'menu', 'system/menu/index', '', 1, 0, 'C', '0', '0', '', 'user', 'admin', '2023-12-27 15:00:27', '', '', '用户管理菜单');
INSERT INTO `sys_menu_oms` VALUES (2077, '店铺商品管理', 4, 2, 'shop_goods', 'goods/shopGoods/index', NULL, 1, 0, 'C', '0', '0', '', 'shopping', 'admin', '2024-03-28 10:29:59', 'admin', '2024-03-28 10:30:59', '');
INSERT INTO `sys_menu_oms` VALUES (2078, '添加ERP商品', 4, 9, 'goods_add', 'goods/create', NULL, 1, 0, 'C', '1', '0', '', 'checkbox', 'admin', '2024-03-18 07:59:57', 'admin', '2024-03-21 10:10:11', '');
INSERT INTO `sys_menu_oms` VALUES (2079, '字典管理', 5, 9, 'dict', 'system/dict/index', NULL, 1, 0, 'C', '0', '0', '', 'dict', 'admin', '2024-03-18 08:43:55', 'admin', '2024-03-18 08:44:08', '');
INSERT INTO `sys_menu_oms` VALUES (2080, '店铺商品管理-淘宝', 109, 1, 'tao_goods_list', 'tao/goods/index', NULL, 1, 0, 'C', '1', '0', '', 'code', 'admin', '2024-03-21 11:41:36', 'admin', '2024-03-22 10:42:41', '');
INSERT INTO `sys_menu_oms` VALUES (2081, '店铺商品管理-京东', 109, 2, 'jd_goods_list', 'jd/goods/index', NULL, 1, 0, 'C', '1', '0', '', 'code', 'admin', '2024-03-21 11:42:30', 'admin', '2024-03-23 10:14:24', '');
INSERT INTO `sys_menu_oms` VALUES (2082, '店铺商品管理-拼多多', 109, 4, 'pdd_goods_list', 'pdd/goods/index', NULL, 1, 0, 'C', '1', '0', '', 'tool', 'admin', '2024-03-21 13:35:26', 'admin', '2024-03-23 10:15:16', '');
INSERT INTO `sys_menu_oms` VALUES (2083, '店铺商品管理-抖店', 109, 3, 'dou_goods_list', 'dou/goods/index', NULL, 1, 0, 'C', '1', '0', '', 'code', 'admin', '2024-03-21 20:04:15', 'admin', '2024-03-23 10:14:57', '');
INSERT INTO `sys_menu_oms` VALUES (2084, '快递公司库', 3, 99, 'logistics_companies', 'shipping/logistics/company', NULL, 1, 0, 'C', '1', '0', '', 'code', 'admin', '2024-03-21 20:05:09', 'admin', '2024-03-31 10:09:38', '');
INSERT INTO `sys_menu_oms` VALUES (2085, '省市区地址库', 110, 6, 'addr', NULL, NULL, 1, 0, 'C', '1', '0', NULL, 'color', 'admin', '2024-03-21 20:05:39', '', NULL, '');
INSERT INTO `sys_menu_oms` VALUES (2086, '定时任务', 5, 5, 'sys_task', 'system/task/index', NULL, 1, 0, 'C', '0', '0', '', 'time-range', 'admin', '2024-03-22 19:29:20', 'admin', '2024-03-22 19:31:23', '');
INSERT INTO `sys_menu_oms` VALUES (2087, '发货管理', 0, 3, 'shipping', NULL, NULL, 1, 0, 'M', '0', '0', '', 'guide', 'admin', '2024-03-30 17:36:10', 'admin', '2024-03-31 09:57:41', '');
INSERT INTO `sys_menu_oms` VALUES (2088, '快递公司管理', 2087, 9, 'logistics_company', 'shipping/logistics/company', NULL, 1, 0, 'C', '0', '0', '', 'checkbox', 'admin', '2024-03-30 17:37:01', 'admin', '2024-03-31 09:58:18', '');
INSERT INTO `sys_menu_oms` VALUES (2089, '发货管理', 2087, 2, 'ship', 'shipping/index', NULL, 1, 0, 'C', '0', '0', '', 'edit', 'admin', '2024-03-30 17:37:42', 'admin', '2024-04-06 14:49:59', '');
INSERT INTO `sys_menu_oms` VALUES (2090, '角色管理', 5, 2, 'role', 'system/role/index', NULL, 1, 0, 'C', '0', '0', NULL, 'peoples', 'admin', '2024-03-31 12:40:50', '', NULL, '');
INSERT INTO `sys_menu_oms` VALUES (2091, '部门管理', 5, 3, 'dept', 'system/dept/index', NULL, 1, 0, 'C', '0', '0', NULL, 'tree', 'admin', '2024-03-31 12:42:57', '', NULL, '');
INSERT INTO `sys_menu_oms` VALUES (2092, '售后处理', 2, 0, 'processing', 'afterSale/index', NULL, 1, 0, 'C', '0', '0', '', 'documentation', 'admin', '2024-04-06 17:27:03', 'admin', '2024-04-06 17:31:12', '');
INSERT INTO `sys_menu_oms` VALUES (2093, '订单明细', 1, 2, 'order_item_list', 'order/item_list', NULL, 1, 0, 'C', '0', '0', NULL, 'chart', 'admin', '2024-04-06 18:58:06', '', NULL, '');

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` int(0) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50)  NOT NULL COMMENT '公告标题',
  `notice_type` char(1)  NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob COMMENT '公告内容',
  `status` char(1)  DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64)  DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64)  DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) 
) ;


-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50)  DEFAULT '' COMMENT '模块标题',
  `business_type` int(0) DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100)  DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10)  DEFAULT '' COMMENT '请求方式',
  `operator_type` int(0) DEFAULT 0 COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50)  DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50)  DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255)  DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128)  DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255)  DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000)  DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000)  DEFAULT '' COMMENT '返回参数',
  `status` int(0) DEFAULT 0 COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000)  DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime(0) DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint(0) DEFAULT 0 COMMENT '消耗时间',
  PRIMARY KEY (`oper_id`) ,
  INDEX `idx_sys_oper_log_bt`(`business_type`) ,
  INDEX `idx_sys_oper_log_s`(`status`) ,
  INDEX `idx_sys_oper_log_ot`(`oper_time`) 
) ;

-- ----------------------------
-- Table structure for sys_oss
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss`;
CREATE TABLE `sys_oss`  (
  `oss_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '文件id',
  `file_name` varchar(100)  DEFAULT '' COMMENT '文件名',
  `original_name` varchar(100)  DEFAULT '' COMMENT '原名',
  `file_suffix` varchar(100)  DEFAULT '' COMMENT '文件后缀名',
  `url` varchar(300)  DEFAULT '' COMMENT 'URL地址',
  `object_name` varchar(300)  DEFAULT '' COMMENT '对象名',
  `bucket` varchar(100)  DEFAULT '' COMMENT '桶名',
  `order_num` int(0) DEFAULT 0 COMMENT '显示顺序',
  `status` char(1)  DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `del_flag` char(1)  DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64)  DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64)  DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`oss_id`) 
) ;

-- ----------------------------
-- Records of sys_oss
-- ----------------------------

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64)  NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50)  NOT NULL COMMENT '岗位名称',
  `post_sort` int(0) NOT NULL COMMENT '显示顺序',
  `status` char(1)  NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64)  DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64)  DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) 
) ;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 'ceo', '董事长', 1, '0', 'admin', '2023-08-07 19:31:37', '', NULL, '');
INSERT INTO `sys_post` VALUES (2, 'se', '项目经理', 2, '0', 'admin', '2023-08-07 19:31:37', '', NULL, '');
INSERT INTO `sys_post` VALUES (3, 'hr', '人力资源', 3, '0', 'admin', '2023-08-07 19:31:37', '', NULL, '');
INSERT INTO `sys_post` VALUES (4, 'user', '普通员工', 4, '0', 'admin', '2023-08-07 19:31:37', '', NULL, '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30)  NOT NULL COMMENT '角色名称',
  `role_key` varchar(100)  NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(0) NOT NULL COMMENT '显示顺序',
  `data_scope` char(1)  DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` char(1)  NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1)  DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64)  DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64)  DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) 
) ;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2023-08-07 19:31:37', '', NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '2', 1, 1, '0', '0', 'admin', '2023-08-07 19:31:37', 'admin', '2024-04-15 10:29:55', '普通角色');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint(0) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(0) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) 
) ;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES (2, 100);
INSERT INTO `sys_role_dept` VALUES (2, 101);
INSERT INTO `sys_role_dept` VALUES (2, 105);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint(0) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(0) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) 
) ;

-- ----------------------------
-- Table structure for sys_task
-- ----------------------------
DROP TABLE IF EXISTS `sys_task`;
CREATE TABLE `sys_task`  (
  `id` int(0) NOT NULL,
  `task_name` varchar(255)  DEFAULT NULL,
  `cron` varchar(255)  DEFAULT NULL,
  `method` varchar(255)  DEFAULT NULL,
  `remark` varchar(255)  DEFAULT NULL,
  `create_time` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`id`) 
) ;

-- ----------------------------
-- Records of sys_task
-- ----------------------------
INSERT INTO `sys_task` VALUES (1, '更新淘宝订单', '-', NULL, '定时更新淘宝订单', '2024-03-07 09:52:40');
INSERT INTO `sys_task` VALUES (2, '更新京东订单', '-', NULL, '定时更新京东订单', '2024-03-07 09:23:36');

-- ----------------------------
-- Table structure for sys_task_logs
-- ----------------------------
DROP TABLE IF EXISTS `sys_task_logs`;
CREATE TABLE `sys_task_logs`  (
  `id` bigint(0) NOT NULL COMMENT '主键ID',
  `task_id` int(0) DEFAULT NULL COMMENT '任务ID',
  `result` varchar(500)  DEFAULT NULL COMMENT '结果',
  `start_time` datetime(0) DEFAULT NULL COMMENT '开始运行时间',
  `end_time` datetime(0) DEFAULT NULL COMMENT '结束时间',
  `status` int(0) DEFAULT NULL COMMENT '状态1运行中，2已结束',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) 
) ;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint(0) DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30)  NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30)  NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2)  DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50)  DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11)  DEFAULT '' COMMENT '手机号码',
  `sex` char(1)  DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100)  DEFAULT '' COMMENT '头像地址',
  `password` varchar(100)  DEFAULT '' COMMENT '密码',
  `status` char(1)  DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1)  DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128)  DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime(0) DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64)  DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64)  DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500)  DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) 
) ;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 100, 'admin', '启航', '00', '280645618@qq.com', '15888888888', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2024-05-07 14:57:58', 'admin', '2023-08-07 19:31:37', '', '2024-05-07 14:57:58', '管理员');
INSERT INTO `sys_user` VALUES (2, 101, 'qihang', 'qihang', '00', 'qihang@qq.com', '15666666666', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2023-08-07 19:31:37', 'admin', '2023-08-07 19:31:37', 'admin', '2024-01-05 18:29:55', '测试员');
INSERT INTO `sys_user` VALUES (100, NULL, 'admin11', 'aa', '00', '', '', '1', '', '$2a$10$VD49q2rn1ATpQDZJJrmJjuG52b4EkOTTZ0MPbRRmcqEYLmB5mAMsG', '0', '2', '', NULL, 'admin', '2024-04-24 11:06:27', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `post_id` bigint(0) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `post_id`) 
) ;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (1, 1);
INSERT INTO `sys_user_post` VALUES (2, 2);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
  `role_id` bigint(0) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) 
) ;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2);
