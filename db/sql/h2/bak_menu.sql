-- H2 2.1.214;
;             
CREATE USER IF NOT EXISTS "QIHANG" SALT '6e9f191e65201f0e' HASH '4b332f89206a24bfff3c5f30cf7112c4bec37cec267b4b060fb232051ae9da19' ADMIN;     
CREATE CACHED TABLE "PUBLIC"."SYS_MENU" COMMENT U&'\83dc\5355\6743\9650\8868'(
    "MENU_ID" BIGINT GENERATED BY DEFAULT AS IDENTITY(START WITH 2000 RESTART WITH 2030) DEFAULT ON NULL COMMENT U&'\83dc\5355ID' NOT NULL,
    "MENU_NAME" CHARACTER VARYING(50) COMMENT U&'\83dc\5355\540d\79f0' NOT NULL,
    "PARENT_ID" BIGINT DEFAULT 0 COMMENT U&'\7236\83dc\5355ID',
    "ORDER_NUM" INTEGER DEFAULT 0 COMMENT U&'\663e\793a\987a\5e8f',
    "PATH" CHARACTER VARYING(200) DEFAULT '' COMMENT U&'\8def\7531\5730\5740',
    "COMPONENT" CHARACTER VARYING(255) DEFAULT NULL COMMENT U&'\7ec4\4ef6\8def\5f84',
    "QUERY" CHARACTER VARYING(255) DEFAULT NULL COMMENT U&'\8def\7531\53c2\6570',
    "IS_FRAME" INTEGER DEFAULT 1 COMMENT U&'\662f\5426\4e3a\5916\94fe\ff080\662f 1\5426\ff09',
    "IS_CACHE" INTEGER DEFAULT 0 COMMENT U&'\662f\5426\7f13\5b58\ff080\7f13\5b58 1\4e0d\7f13\5b58\ff09',
    "MENU_TYPE" CHARACTER(1) DEFAULT '' COMMENT U&'\83dc\5355\7c7b\578b\ff08M\76ee\5f55 C\83dc\5355 F\6309\94ae\ff09',
    "VISIBLE" CHARACTER(1) DEFAULT 0 COMMENT U&'\83dc\5355\72b6\6001\ff080\663e\793a 1\9690\85cf\ff09',
    "STATUS" CHARACTER(1) DEFAULT 0 COMMENT U&'\83dc\5355\72b6\6001\ff080\6b63\5e38 1\505c\7528\ff09',
    "PERMS" CHARACTER VARYING(100) DEFAULT NULL COMMENT U&'\6743\9650\6807\8bc6',
    "ICON" CHARACTER VARYING(100) DEFAULT '#' COMMENT U&'\83dc\5355\56fe\6807',
    "CREATE_BY" CHARACTER VARYING(64) DEFAULT '' COMMENT U&'\521b\5efa\8005',
    "CREATE_TIME" TIMESTAMP COMMENT U&'\521b\5efa\65f6\95f4',
    "UPDATE_BY" CHARACTER VARYING(64) DEFAULT '' COMMENT U&'\66f4\65b0\8005',
    "UPDATE_TIME" TIMESTAMP COMMENT U&'\66f4\65b0\65f6\95f4',
    "REMARK" CHARACTER VARYING(500) DEFAULT '' COMMENT U&'\5907\6ce8'
);   
ALTER TABLE "PUBLIC"."SYS_MENU" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_A50D" PRIMARY KEY("MENU_ID");             
-- 111 +/- SELECT COUNT(*) FROM PUBLIC.SYS_MENU;              
INSERT INTO "PUBLIC"."SYS_MENU" VALUES
(1, U&'\7cfb\7edf\7ba1\7406', 0, 99, 'system', NULL, '', 1, 0, 'M', '0', '0', '', 'system', 'admin', TIMESTAMP '2023-12-27 15:00:27.045487', 'admin', TIMESTAMP '2023-12-29 09:07:42.856856', U&'\7cfb\7edf\7ba1\7406\76ee\5f55'),
(2, U&'\7cfb\7edf\76d1\63a7', 0, 100, 'monitor', NULL, '', 1, 0, 'M', '0', '0', '', 'monitor', 'admin', TIMESTAMP '2023-12-27 15:00:27.045487', 'admin', TIMESTAMP '2023-12-29 09:07:51.123192', U&'\7cfb\7edf\76d1\63a7\76ee\5f55'),
(3, U&'\7cfb\7edf\5de5\5177', 0, 101, 'tool', NULL, '', 1, 0, 'M', '0', '0', '', 'tool', 'admin', TIMESTAMP '2023-12-27 15:00:27.046486', 'admin', TIMESTAMP '2023-12-29 09:08:06.918539', U&'\7cfb\7edf\5de5\5177\76ee\5f55'),
(4, U&'\91c7\8d2d\7ba1\7406', 0, 4, 'scm', NULL, '', 1, 0, 'M', '0', '0', '', 'server', 'admin', TIMESTAMP '2023-12-27 15:00:27.046486', 'admin', TIMESTAMP '2023-12-29 13:28:33.639556', U&'\81f3\7b80\5b98\7f51\5730\5740'),
(100, U&'\7528\6237\7ba1\7406', 1, 1, 'user', 'system/user/index', '', 1, 0, 'C', '0', '0', 'system:user:list', 'user', 'admin', TIMESTAMP '2023-12-27 15:00:27.047487', '', NULL, U&'\7528\6237\7ba1\7406\83dc\5355'),
(101, U&'\89d2\8272\7ba1\7406', 1, 2, 'role', 'system/role/index', '', 1, 0, 'C', '0', '0', 'system:role:list', 'peoples', 'admin', TIMESTAMP '2023-12-27 15:00:27.048487', '', NULL, U&'\89d2\8272\7ba1\7406\83dc\5355'),
(102, U&'\83dc\5355\7ba1\7406', 1, 3, 'menu', 'system/menu/index', '', 1, 0, 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', TIMESTAMP '2023-12-27 15:00:27.048487', '', NULL, U&'\83dc\5355\7ba1\7406\83dc\5355'),
(103, U&'\90e8\95e8\7ba1\7406', 1, 4, 'dept', 'system/dept/index', '', 1, 0, 'C', '0', '0', 'system:dept:list', 'tree', 'admin', TIMESTAMP '2023-12-27 15:00:27.049486', '', NULL, U&'\90e8\95e8\7ba1\7406\83dc\5355'),
(104, U&'\5c97\4f4d\7ba1\7406', 1, 5, 'post', 'system/post/index', '', 1, 0, 'C', '0', '0', 'system:post:list', 'post', 'admin', TIMESTAMP '2023-12-27 15:00:27.049486', '', NULL, U&'\5c97\4f4d\7ba1\7406\83dc\5355'),
(105, U&'\5b57\5178\7ba1\7406', 1, 6, 'dict', 'system/dict/index', '', 1, 0, 'C', '0', '0', 'system:dict:list', 'dict', 'admin', TIMESTAMP '2023-12-27 15:00:27.050486', '', NULL, U&'\5b57\5178\7ba1\7406\83dc\5355'),
(106, U&'\53c2\6570\8bbe\7f6e', 1, 7, 'config', 'system/config/index', '', 1, 0, 'C', '0', '0', 'system:config:list', 'edit', 'admin', TIMESTAMP '2023-12-27 15:00:27.050486', '', NULL, U&'\53c2\6570\8bbe\7f6e\83dc\5355'),
(107, U&'\901a\77e5\516c\544a', 1, 8, 'notice', 'system/notice/index', '', 1, 0, 'C', '0', '0', 'system:notice:list', 'message', 'admin', TIMESTAMP '2023-12-27 15:00:27.051489', '', NULL, U&'\901a\77e5\516c\544a\83dc\5355'),
(108, U&'\65e5\5fd7\7ba1\7406', 1, 9, 'log', '', '', 1, 0, 'M', '0', '0', '', 'log', 'admin', TIMESTAMP '2023-12-27 15:00:27.051489', '', NULL, U&'\65e5\5fd7\7ba1\7406\83dc\5355'),
(109, U&'\5728\7ebf\7528\6237', 2, 1, 'online', 'monitor/online/index', '', 1, 0, 'C', '0', '0', 'monitor:online:list', 'online', 'admin', TIMESTAMP '2023-12-27 15:00:27.051489', '', NULL, U&'\5728\7ebf\7528\6237\83dc\5355'),
(110, U&'\5b9a\65f6\4efb\52a1', 2, 2, 'job', 'monitor/job/index', '', 1, 0, 'C', '0', '0', 'monitor:job:list', 'job', 'admin', TIMESTAMP '2023-12-27 15:00:27.052488', '', NULL, U&'\5b9a\65f6\4efb\52a1\83dc\5355'),
(111, U&'\6570\636e\76d1\63a7', 2, 3, 'druid', 'monitor/druid/index', '', 1, 0, 'C', '0', '0', 'monitor:druid:list', 'druid', 'admin', TIMESTAMP '2023-12-27 15:00:27.052488', '', NULL, U&'\6570\636e\76d1\63a7\83dc\5355'),
(112, U&'\670d\52a1\76d1\63a7', 2, 4, 'server', 'monitor/server/index', '', 1, 0, 'C', '0', '0', 'monitor:server:list', 'server', 'admin', TIMESTAMP '2023-12-27 15:00:27.052488', '', NULL, U&'\670d\52a1\76d1\63a7\83dc\5355'),
(113, U&'\7f13\5b58\76d1\63a7', 2, 5, 'cache', 'monitor/cache/index', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis', 'admin', TIMESTAMP '2023-12-27 15:00:27.053488', '', NULL, U&'\7f13\5b58\76d1\63a7\83dc\5355'),
(114, U&'\7f13\5b58\5217\8868', 2, 6, 'cacheList', 'monitor/cache/list', '', 1, 0, 'C', '0', '0', 'monitor:cache:list', 'redis-list', 'admin', TIMESTAMP '2023-12-27 15:00:27.053488', '', NULL, U&'\7f13\5b58\5217\8868\83dc\5355');    
INSERT INTO "PUBLIC"."SYS_MENU" VALUES
(115, U&'\8868\5355\6784\5efa', 3, 1, 'build', 'tool/build/index', '', 1, 0, 'C', '0', '0', 'tool:build:list', 'build', 'admin', TIMESTAMP '2023-12-27 15:00:27.054486', '', NULL, U&'\8868\5355\6784\5efa\83dc\5355'),
(116, U&'\4ee3\7801\751f\6210', 3, 2, 'gen', 'tool/gen/index', '', 1, 0, 'C', '0', '0', 'tool:gen:list', 'code', 'admin', TIMESTAMP '2023-12-27 15:00:27.054486', '', NULL, U&'\4ee3\7801\751f\6210\83dc\5355'),
(117, U&'\7cfb\7edf\63a5\53e3', 3, 3, 'swagger', 'tool/swagger/index', '', 1, 0, 'C', '0', '0', 'tool:swagger:list', 'swagger', 'admin', TIMESTAMP '2023-12-27 15:00:27.054486', '', NULL, U&'\7cfb\7edf\63a5\53e3\83dc\5355'),
(500, U&'\64cd\4f5c\65e5\5fd7', 108, 1, 'operlog', 'monitor/operlog/index', '', 1, 0, 'C', '0', '0', 'monitor:operlog:list', 'form', 'admin', TIMESTAMP '2023-12-27 15:00:27.054486', '', NULL, U&'\64cd\4f5c\65e5\5fd7\83dc\5355'),
(501, U&'\767b\5f55\65e5\5fd7', 108, 2, 'logininfor', 'monitor/logininfor/index', '', 1, 0, 'C', '0', '0', 'monitor:logininfor:list', 'logininfor', 'admin', TIMESTAMP '2023-12-27 15:00:27.055486', '', NULL, U&'\767b\5f55\65e5\5fd7\83dc\5355'),
(1000, U&'\7528\6237\67e5\8be2', 100, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.055486', '', NULL, ''),
(1001, U&'\7528\6237\65b0\589e', 100, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.055486', '', NULL, ''),
(1002, U&'\7528\6237\4fee\6539', 100, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.056485', '', NULL, ''),
(1003, U&'\7528\6237\5220\9664', 100, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.056485', '', NULL, ''),
(1004, U&'\7528\6237\5bfc\51fa', 100, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.056485', '', NULL, ''),
(1005, U&'\7528\6237\5bfc\5165', 100, 6, '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.056485', '', NULL, ''),
(1006, U&'\91cd\7f6e\5bc6\7801', 100, 7, '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.057488', '', NULL, ''),
(1007, U&'\89d2\8272\67e5\8be2', 101, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.057488', '', NULL, ''),
(1008, U&'\89d2\8272\65b0\589e', 101, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.057488', '', NULL, ''),
(1009, U&'\89d2\8272\4fee\6539', 101, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.057488', '', NULL, ''),
(1010, U&'\89d2\8272\5220\9664', 101, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.057488', '', NULL, ''),
(1011, U&'\89d2\8272\5bfc\51fa', 101, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.058487', '', NULL, ''),
(1012, U&'\83dc\5355\67e5\8be2', 102, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.058487', '', NULL, ''),
(1013, U&'\83dc\5355\65b0\589e', 102, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.058487', '', NULL, ''),
(1014, U&'\83dc\5355\4fee\6539', 102, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.059486', '', NULL, ''),
(1015, U&'\83dc\5355\5220\9664', 102, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.059486', '', NULL, ''),
(1016, U&'\90e8\95e8\67e5\8be2', 103, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.059486', '', NULL, ''),
(1017, U&'\90e8\95e8\65b0\589e', 103, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.063495', '', NULL, '');         
INSERT INTO "PUBLIC"."SYS_MENU" VALUES
(1018, U&'\90e8\95e8\4fee\6539', 103, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.063495', '', NULL, ''),
(1019, U&'\90e8\95e8\5220\9664', 103, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.064489', '', NULL, ''),
(1020, U&'\5c97\4f4d\67e5\8be2', 104, 1, '', '', '', 1, 0, 'F', '0', '0', 'system:post:query', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.064489', '', NULL, ''),
(1021, U&'\5c97\4f4d\65b0\589e', 104, 2, '', '', '', 1, 0, 'F', '0', '0', 'system:post:add', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.064489', '', NULL, ''),
(1022, U&'\5c97\4f4d\4fee\6539', 104, 3, '', '', '', 1, 0, 'F', '0', '0', 'system:post:edit', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.065485', '', NULL, ''),
(1023, U&'\5c97\4f4d\5220\9664', 104, 4, '', '', '', 1, 0, 'F', '0', '0', 'system:post:remove', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.067486', '', NULL, ''),
(1024, U&'\5c97\4f4d\5bfc\51fa', 104, 5, '', '', '', 1, 0, 'F', '0', '0', 'system:post:export', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.068485', '', NULL, ''),
(1025, U&'\5b57\5178\67e5\8be2', 105, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.068485', '', NULL, ''),
(1026, U&'\5b57\5178\65b0\589e', 105, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.068485', '', NULL, ''),
(1027, U&'\5b57\5178\4fee\6539', 105, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.068485', '', NULL, ''),
(1028, U&'\5b57\5178\5220\9664', 105, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.06949', '', NULL, ''),
(1029, U&'\5b57\5178\5bfc\51fa', 105, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.06949', '', NULL, ''),
(1030, U&'\53c2\6570\67e5\8be2', 106, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.06949', '', NULL, ''),
(1031, U&'\53c2\6570\65b0\589e', 106, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.06949', '', NULL, ''),
(1032, U&'\53c2\6570\4fee\6539', 106, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.070485', '', NULL, ''),
(1033, U&'\53c2\6570\5220\9664', 106, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.070485', '', NULL, ''),
(1034, U&'\53c2\6570\5bfc\51fa', 106, 5, '#', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.070485', '', NULL, ''),
(1035, U&'\516c\544a\67e5\8be2', 107, 1, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:query', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.071486', '', NULL, ''),
(1036, U&'\516c\544a\65b0\589e', 107, 2, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:add', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.071486', '', NULL, ''),
(1037, U&'\516c\544a\4fee\6539', 107, 3, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:edit', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.071486', '', NULL, ''),
(1038, U&'\516c\544a\5220\9664', 107, 4, '#', '', '', 1, 0, 'F', '0', '0', 'system:notice:remove', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.071486', '', NULL, ''),
(1039, U&'\64cd\4f5c\67e5\8be2', 500, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:query', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.072486', '', NULL, ''),
(1040, U&'\64cd\4f5c\5220\9664', 500, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:remove', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.073486', '', NULL, ''),
(1041, U&'\65e5\5fd7\5bfc\51fa', 500, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:operlog:export', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.073486', '', NULL, ''),
(1042, U&'\767b\5f55\67e5\8be2', 501, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:query', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.073486', '', NULL, '');           
INSERT INTO "PUBLIC"."SYS_MENU" VALUES
(1043, U&'\767b\5f55\5220\9664', 501, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:remove', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.074485', '', NULL, ''),
(1044, U&'\65e5\5fd7\5bfc\51fa', 501, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:export', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.074485', '', NULL, ''),
(1045, U&'\8d26\6237\89e3\9501', 501, 4, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:logininfor:unlock', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.074485', '', NULL, ''),
(1046, U&'\5728\7ebf\67e5\8be2', 109, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:query', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.074485', '', NULL, ''),
(1047, U&'\6279\91cf\5f3a\9000', 109, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:batchLogout', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.075485', '', NULL, ''),
(1048, U&'\5355\6761\5f3a\9000', 109, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:online:forceLogout', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.075485', '', NULL, ''),
(1049, U&'\4efb\52a1\67e5\8be2', 110, 1, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:query', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.075485', '', NULL, ''),
(1050, U&'\4efb\52a1\65b0\589e', 110, 2, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:add', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.076485', '', NULL, ''),
(1051, U&'\4efb\52a1\4fee\6539', 110, 3, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:edit', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.077487', '', NULL, ''),
(1052, U&'\4efb\52a1\5220\9664', 110, 4, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:remove', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.077487', '', NULL, ''),
(1053, U&'\72b6\6001\4fee\6539', 110, 5, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:changeStatus', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.079484', '', NULL, ''),
(1054, U&'\4efb\52a1\5bfc\51fa', 110, 6, '#', '', '', 1, 0, 'F', '0', '0', 'monitor:job:export', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.079484', '', NULL, ''),
(1055, U&'\751f\6210\67e5\8be2', 116, 1, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.079484', '', NULL, ''),
(1056, U&'\751f\6210\4fee\6539', 116, 2, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.080489', '', NULL, ''),
(1057, U&'\751f\6210\5220\9664', 116, 3, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.081485', '', NULL, ''),
(1058, U&'\5bfc\5165\4ee3\7801', 116, 4, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.081485', '', NULL, ''),
(1059, U&'\9884\89c8\4ee3\7801', 116, 5, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.082486', '', NULL, ''),
(1060, U&'\751f\6210\4ee3\7801', 116, 6, '#', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', TIMESTAMP '2023-12-27 15:00:27.082486', '', NULL, ''),
(2000, U&'\5e02\573a\6d1e\5bdf', 0, 20, 'data', NULL, NULL, 1, 0, 'M', '0', '0', '', 'international', 'admin', TIMESTAMP '2023-12-22 16:20:02', 'admin', TIMESTAMP '2023-12-29 16:52:09.2174', ''),
(2001, U&'\70ed\641c\8bcd', 2000, 1, 'keyword/hot_keyword_list', 'data/keyword/hot_keyword', NULL, 1, 0, 'C', '0', '0', 'data:keyword:list', 'dict', 'admin', TIMESTAMP '2023-12-22 16:22:59', 'admin', TIMESTAMP '2023-12-26 11:55:45', ''),
(2002, U&'\63a8\8350\5546\54c1', 2000, 8, 'goods/goods_recommend', 'data/goods/recommend_list', NULL, 1, 0, 'C', '1', '0', 'data:goods:recommend', 'star', 'admin', TIMESTAMP '2023-12-25 16:57:48', 'admin', TIMESTAMP '2023-12-29 09:07:16.570167', ''),
(2003, U&'\5546\54c1\70ed\5356\699c', 2000, 9, 'goods/sale_hot_list', 'data/goods/sale_hot_list', NULL, 1, 0, 'C', '1', '0', 'data:goods:salehot', 'list', 'admin', TIMESTAMP '2023-12-25 17:08:24', 'admin', TIMESTAMP '2023-12-29 09:07:20.615237', ''),
(2004, U&'\5e02\573a\52a8\6001', 2000, 0, 'market_info', 'data/market/info', NULL, 1, 0, 'C', '1', '0', 'data:market:info', 'guide', 'admin', TIMESTAMP '2023-12-26 11:55:33', 'admin', TIMESTAMP '2023-12-29 09:07:09.697533', '');       
INSERT INTO "PUBLIC"."SYS_MENU" VALUES
(2005, U&'\4f9b\5e94\5546\7ba1\7406', 4, 9, 'supplier/list', 'scm/supplier/index', NULL, 1, 0, 'C', '0', '0', 'scm:supplier', 'qq', 'admin', TIMESTAMP '2023-12-29 09:14:02.280728', 'admin', TIMESTAMP '2023-12-29 09:17:27.514466', ''),
(2006, U&'\5546\54c1\7ba1\7406', 0, 9, 'goods', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'theme', 'admin', TIMESTAMP '2023-12-29 13:29:43.694022', '', NULL, ''),
(2007, U&'\5546\54c1\7ba1\7406', 2006, 1, 'goods_list', 'goods/index', NULL, 1, 0, 'C', '0', '0', 'goods:list', 'theme', 'admin', TIMESTAMP '2023-12-29 13:31:01.353022', 'admin', TIMESTAMP '2023-12-29 15:02:40.869685', ''),
(2008, U&'\5546\54c1\5206\7c7b', 2006, 88, 'goods_category', 'goods/category/index', NULL, 1, 0, 'C', '0', '0', 'goods:category', 'tree-table', 'admin', TIMESTAMP '2023-12-29 13:32:40.505406', 'admin', TIMESTAMP '2023-12-29 15:02:22.220534', ''),
(2009, U&'\54c1\724c\7ba1\7406', 2006, 99, 'goods/brand', 'goods/brand/index', NULL, 1, 0, 'C', '0', '0', 'goods:brand', 'clipboard', 'admin', TIMESTAMP '2023-12-29 13:34:49.159796', 'admin', TIMESTAMP '2023-12-29 13:58:43.724029', ''),
(2010, U&'\91c7\8d2d\8ba2\5355\7ba1\7406', 4, 1, 'purchase/order', 'scm/purchase/order', NULL, 1, 0, 'C', '0', '0', 'scm:purchase:order', 'button', 'admin', TIMESTAMP '2023-12-29 16:35:54.513586', 'admin', TIMESTAMP '2023-12-29 16:36:57.653118', ''),
(2011, U&'\91c7\8d2d\5408\540c\7ba1\7406', 4, 2, 'purchase/contract', 'scm/purchase/contract', NULL, 1, 0, 'C', '0', '1', 'scm:purchase:contract', 'clipboard', 'admin', TIMESTAMP '2023-12-29 16:39:42.838251', 'admin', TIMESTAMP '2023-12-30 18:07:58.363465', ''),
(2012, U&'\91c7\8d2d\7269\6d41\7ba1\7406', 4, 3, 'purchase/ship', 'scm/purchase/ship', NULL, 1, 0, 'C', '0', '0', 'scm:purchase:ship', 'component', 'admin', TIMESTAMP '2023-12-29 16:45:41.647453', 'admin', TIMESTAMP '2023-12-30 20:48:47.282509', ''),
(2013, U&'\91c7\8d2d\8d39\7528\7ba1\7406', 4, 4, 'purchase/cost', 'scm/purchase/cost', NULL, 1, 0, 'C', '0', '0', 'scm:purchase:cost', 'checkbox', 'admin', TIMESTAMP '2023-12-29 16:47:07.495126', '', NULL, ''),
(2014, U&'\5e97\94fa\7ba1\7406', 0, 1, 'shop', NULL, NULL, 1, 0, 'M', '0', '0', '', 'shopping', 'admin', TIMESTAMP '2023-12-29 16:53:02.928347', 'admin', TIMESTAMP '2023-12-31 17:27:12.107227', ''),
(2015, U&'\5e97\94fa\7ba1\7406', 2014, 10, 'list', 'shop/index', NULL, 1, 0, 'C', '0', '0', 'shop:list', 'example', 'admin', TIMESTAMP '2023-12-29 16:54:02.181095', 'admin', TIMESTAMP '2023-12-29 17:08:18.125544', ''),
(2016, U&'\5546\54c1\4e0a\67b6\7ba1\7406', 2014, 9, 'product_listing', 'shop/product', NULL, 1, 0, 'C', '0', '0', 'shop:product', 'color', 'admin', TIMESTAMP '2023-12-29 17:02:41.51547', 'admin', TIMESTAMP '2023-12-29 17:08:09.265063', ''),
(2017, U&'\5e97\94fa\6570\636e\7edf\8ba1', 2014, 8, 'data', 'shop/data', NULL, 1, 0, 'C', '0', '0', 'shop:data', 'chart', 'admin', TIMESTAMP '2023-12-29 17:04:07.872866', 'admin', TIMESTAMP '2023-12-29 17:08:01.183755', ''),
(2018, U&'\9500\552e\7ba1\7406', 0, 10, 'sales', NULL, NULL, 1, 0, 'M', '0', '0', '', 'email', 'admin', TIMESTAMP '2023-12-29 17:07:22.689795', 'admin', TIMESTAMP '2023-12-31 17:26:57.006459', ''),
(2019, U&'\8ba2\5355\7ba1\7406', 2018, 1, 'list', 'order/list', NULL, 1, 0, 'C', '0', '0', 'order:list', 'druid', 'admin', TIMESTAMP '2023-12-29 17:09:31.817322', '', NULL, ''),
(2021, U&'\521b\5efa\91c7\8d2d\8ba2\5355', 4, 0, 'purchase/order/create', 'scm/purchase/order/create', NULL, 1, 0, 'C', '0', '0', '', 'edit', 'admin', TIMESTAMP '2023-12-29 21:23:44.925439', 'admin', TIMESTAMP '2023-12-29 21:26:17.594875', ''),
(2025, U&'\91c7\8d2d\8ba2\5355\8be6\60c5', 4, 1, 'purchase/order/detail', 'scm/purchase/order/detail', NULL, 1, 0, 'C', '1', '0', '', 'button', 'admin', TIMESTAMP '2023-12-30 17:08:00.960527', 'admin', TIMESTAMP '2023-12-30 17:09:38.154348', ''),
(2026, U&'\4ed3\5e93\7ba1\7406', 0, 30, 'wms', NULL, NULL, 1, 0, 'M', '0', '0', NULL, 'lock', 'admin', TIMESTAMP '2023-12-31 12:14:32.883883', '', NULL, ''),
(2027, U&'\5165\5e93\5355\7ba1\7406', 2026, 0, 'stock_in_entry/list', 'wms/WmsStockInEntry', NULL, 1, 0, 'C', '0', '0', 'wms:stock_in_entry:list', 'documentation', 'admin', TIMESTAMP '2023-12-31 12:27:37.264388', '', NULL, '');            
INSERT INTO "PUBLIC"."SYS_MENU" VALUES
(2028, U&'\751f\6210\91c7\8d2d\5165\5e93\5355', 4, 3, 'purchase/ship/create_stock_in_entry', 'scm/purchase/ship/create_stock_in_entry', NULL, 1, 0, 'C', '1', '0', '', 'button', 'admin', TIMESTAMP '2023-12-31 12:31:31.961248', 'admin', TIMESTAMP '2023-12-31 12:37:39.956753', ''),
(2029, U&'\8ba2\5355\5904\7406', 2014, 1, 'order/handling', NULL, NULL, 1, 0, 'C', '0', '0', NULL, 'dashboard', 'admin', TIMESTAMP '2023-12-31 17:29:02.898263', '', NULL, '');              