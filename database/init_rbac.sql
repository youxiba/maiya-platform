-- ============================================================
-- 麦芽优选 - 系统权限初始化脚本
-- 目标数据库：maiya_user
-- 说明：创建权限、角色、关联关系表，初始化默认管理员
-- ============================================================

USE maiya_user;

-- ============================================================
-- 1. 创建权限标识表
-- ============================================================
CREATE TABLE IF NOT EXISTS right_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    right_id VARCHAR(64) NOT NULL UNIQUE COMMENT '权限ID',
    role_right VARCHAR(128) NOT NULL COMMENT '权限标识（如 ROLE_INDEX_VIEW）',
    name VARCHAR(128) DEFAULT '' COMMENT '权限名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限标识表';

-- ============================================================
-- 2. 创建角色表
-- ============================================================
CREATE TABLE IF NOT EXISTS role_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    create_time BIGINT DEFAULT 0 COMMENT '创建时间',
    uid VARCHAR(64) DEFAULT '' COMMENT '创建者UID',
    role_id VARCHAR(64) NOT NULL UNIQUE COMMENT '角色ID',
    role VARCHAR(64) NOT NULL COMMENT '角色标识（如 ROLE_SUPERADMIN）',
    role_name VARCHAR(64) DEFAULT '' COMMENT '角色显示名称',
    role_status TINYINT(1) DEFAULT 1 COMMENT '状态：1启用 0禁用'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ============================================================
-- 3. 创建角色-权限关联表
-- ============================================================
CREATE TABLE IF NOT EXISTS role_right_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role_id VARCHAR(64) NOT NULL COMMENT '角色ID',
    right_id VARCHAR(64) NOT NULL COMMENT '权限ID',
    UNIQUE KEY uk_role_right (role_id, right_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- ============================================================
-- 4. 创建用户-角色关联表
-- ============================================================
CREATE TABLE IF NOT EXISTS user_role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    uid VARCHAR(64) NOT NULL COMMENT '用户UID',
    role_id VARCHAR(64) NOT NULL COMMENT '角色ID',
    UNIQUE KEY uk_user_role (uid, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- ============================================================
-- 5. 插入权限数据
-- ============================================================
INSERT INTO right_info (right_id, role_right, name) VALUES
-- 5.1 菜单级权限
('R001', 'ROLE_INDEX_VIEW', '首页'),
('R002', 'ROLE_GOODS_VIEW', '商品管理'),
('R003', 'ROLE_ORDER_VIEW', '订单管理'),
('R004', 'ROLE_MCH_VIEW', '商家管理'),
('R005', 'ROLE_USER_VIEW', '会员管理'),
('R006', 'ROLE_ACTIVE_VIEW', '活动营销'),
('R007', 'ROLE_COUNT_VIEW', '统计管理'),
('R008', 'ROLE_MSG_VIEW', '财务管理/消息管理'),
('R009', 'ROLE_CIRCLE_VIEW', '信息管理'),
('R010', 'ROLE_DTBT_VIEW', '配送管理'),
('R011', 'ROLE_RIDER_VIEW', '骑手管理'),
('R012', 'ROLE_SYS_VIEW', '系统管理'),

-- 5.2 首页看板
('R013', 'ROLE_ORDER_COUNT', '订单计数'),
('R014', 'ROLE_GOODS_COUNT', '商品计数'),
('R015', 'ROLE_USER_COUNT', '用户计数'),
('R016', 'ROLE_TODO_LIST', '待办事项'),
('R017', 'ROLE_TODO_LISTS', '待办事项列表'),
('R018', 'ROLE_BUSINESS_STATUS', '营业状态'),
('R019', 'ROLE_BUSINESS_DATA', '营业数据'),
('R020', 'ROLE_USER_DATA', '用户数据'),
('R021', 'ROLE_RANKING_DATA', '排行榜数据'),

-- 5.3 商品管理
('R022', 'ROLE_GOODS_TYPE', '分类管理'),
('R023', 'ROLE_GOODS_TYPE_ADD', '新增类目'),
('R024', 'ROLE_GOODS_TYPE_UPDATE', '修改类目'),
('R025', 'ROLE_GOODS_TYPE_DELETE', '删除类目'),
('R026', 'ROLE_ORDER', '商品列表'),
('R027', 'ROLE_GOODS_IN', '搜索商品'),
('R028', 'ROLE_GOODS_ADD', '新增商品'),
('R029', 'ROLE_GOODS_UPDATE', '修改商品'),
('R030', 'ROLE_GOODS_DELETE', '删除商品'),
('R031', 'ROLE_GOODS_ONLINE', '上架/下架'),
('R032', 'ROLE_GOODS_OUT', '导出商品'),
('R033', 'ROLE_GOODS_TEMPLATE', '商品模板'),

-- 5.4 订单管理
('R034', 'ROLE_ORDER_SELECT', '查询订单'),
('R035', 'ROLE_ORDER_DETAILS', '查看订单详情'),
('R036', 'ROLE_ORDER_PRINT', '打印订单'),
('R037', 'ROLE_ORDER_PRINT_ALL', '批量打印'),
('R038', 'ROLE_ORDER_ADD', '新增订单'),
('R039', 'ROLE_ORDER_HANDLE', '处理订单'),
('R040', 'ROLE_ORDER_DELETE', '删除订单'),

-- 5.4.1 售后管理
('R041', 'ROLE_AFTER_SALES_ADD', '新增售后'),
('R042', 'ROLE_AFTER_SALES_UPDATE', '修改售后'),
('R043', 'ROLE_AFTER_SALES_DELETE', '删除售后'),

-- 5.4.2 退款管理
('R044', 'ROLE_ORDER_REFUND_ADD', '新增退款'),
('R045', 'ROLE_ORDER_REFUND_HANDLE', '处理退款'),
('R046', 'ROLE_ORDER_REFUND_DELETE', '删除退款'),
('R047', 'ROLE_ORDER_REFUND_SELECT', '查询退款'),
('R048', 'ROLE_ORDER_REFUND_DETAILS', '退款详情'),
('R049', 'ROLE_ORDER_REFUND_PRINT', '打印退款单'),
('R050', 'ROLE_ORDER_REFUND_PRINT_ALL', '批量打印退款单'),

-- 5.4.3 关单管理
('R051', 'ROLE_ORDER_CLOSE_SELECT', '查询关单'),
('R052', 'ROLE_ORDER_CLOSE_DETAILS', '关单详情'),

-- 5.5 商家管理
('R053', 'ROLE_MCH_ADD', '商家列表'),
('R054', 'ROLE_MCH_SELECT', '查询商家'),
('R055', 'ROLE_MCH_DETAILS', '商家详情'),
('R056', 'ROLE_MCH_ENABLE', '启用/禁用商家'),
('R057', 'ROLE_MCH_DELETE', '删除商家'),
('R058', 'ROLE_MCH_OUT_LIST', '导出商家列表'),
('R059', 'ROLE_MCH_EXA', '商家审核'),
('R060', 'ROLE_MCH_EXA_SELECT', '查询审核'),
('R061', 'ROLE_MCH_EXA_DETAILS', '审核详情'),
('R062', 'ROLE_MCH_EXA_ENABLE', '审核通过/拒绝'),
('R063', 'ROLE_MCH_EXA_OUT_LIST', '导出审核列表'),

-- 5.6 会员管理
('R064', 'ROLE_USER', '会员列表'),
('R065', 'ROLE_USER_SELECT', '查询会员'),
('R066', 'ROLE_USER_DETAILS', '会员详情'),
('R067', 'ROLE_USER_ENABLE', '启用/禁用会员'),
('R068', 'ROLE_USER_ADD', '新增会员'),
('R069', 'ROLE_USER_DELETE', '删除会员'),
('R070', 'ROLE_USER_OUT', '导出会员'),
('R071', 'ROLE_USER_EXAMINE', '会员审核'),
('R072', 'ROLE_USER_DELETE_LIST', '删除列表'),
('R073', 'ROLE_USER_SELECT_SETHARD', '查询冻结'),
('R074', 'ROLE_USER_LEVEL', '会员升级审核'),
('R075', 'ROLE_USER_LEVEL_AUDIT_VIEW', '查看会员升级审核'),
('R076', 'ROLE_USER_AUTH_AUDIT', '实名认证审核'),
('R077', 'ROLE_USER_AUTH_AUDIT_VIEW', '查看实名认证审核'),
('R078', 'ROLE_USER_LEVEL_SET', '等级管理'),
('R079', 'ROLE_USER_LEVEL_DELETE', '删除等级'),
('R080', 'ROLE_USER_LEVEL_UPDATE', '修改等级'),
('R081', 'ROLE_USER_UPGRADE_TERM', '升级条件设置'),
('R082', 'ROLE_USER_UPGRADE_SAVE', '保存升级条件'),
('R083', 'ROLE_USER_BONUS_SET', '分红设置'),
('R084', 'ROLE_USER_BONUS_SAVE', '保存分红设置'),

-- 5.7 活动营销
('R085', 'ROLE_COUPON_MAIYA', '平台优惠券'),
('R086', 'ROLE_COUPON_MCH', '商家优惠券'),
('R087', 'ROLE_LOOP_PICS', '轮播图'),
('R088', 'ROLE_LOOP_PICS_ADD', '新增轮播图'),
('R089', 'ROLE_LOOP_PICS_DELETE', '删除轮播图'),
('R090', 'ROLE_LOOP_PICS_SELECT', '查询轮播图'),
('R091', 'ROLE_LOOP_PICS_ENABLE', '开启/关闭轮播图'),
('R092', 'ROLE_LOOP_PICS_DETAILS', '轮播图详情'),
('R093', 'ROLE_LOOP_PICS_DELETE_LIST', '批量删除轮播图'),

-- 5.7.1 限时抢购
('R094', 'ROLE_FLASH_SALE_ADD', '新增抢购'),
('R095', 'ROLE_FLASH_SALE_DELETE', '删除抢购'),
('R096', 'ROLE_FLASH_SALE_DETAILS', '抢购详情'),
('R097', 'ROLE_FLASH_SALE_SELECT', '查询抢购'),
('R098', 'ROLE_FLASH_SALE_ENABLE', '启用/禁用抢购'),
('R099', 'ROLE_FLASH_SALE_PARTAKE', '参与记录'),
('R100', 'ROLE_FLASH_SALE_EXAMINE', '审核抢购'),

-- 5.7.2 团购
('R101', 'ROLE_GROUP_SALE_ADD', '新增团购'),
('R102', 'ROLE_GROUP_SALE_SELECT', '查询团购'),
('R103', 'ROLE_GROUP_SALE_ENABLE', '启用/禁用团购'),
('R104', 'ROLE_GROUP_SALE_EXAMINE', '审核团购'),
('R105', 'ROLE_GROUP_SALE_DETAILS', '团购详情'),
('R106', 'ROLE_GROUP_SALE_PARTAKE', '参与记录'),

-- 5.7.3 起价了(QJL)
('R107', 'ROLE_QJL_SALE_ADD', '新增活动'),
('R108', 'ROLE_QJL_SALE_SELECT', '查询活动'),
('R109', 'ROLE_QJL_SALE_ENABLE', '启用/禁用'),
('R110', 'ROLE_QJL_SALE_EXAMINE', '审核活动'),
('R111', 'ROLE_QJL_SALE_DETAILS', '活动详情'),
('R112', 'ROLE_QJL_SALE_PARTAKE', '参与记录'),

-- 5.8 数据统计
('R113', 'ROLE_COUNT_ORDER', '订单统计'),
('R114', 'ROLE_COUNT_ORDER_SELECT', '查询订单统计'),
('R115', 'ROLE_COUNT_ORDER_OUT', '导出订单统计'),
('R116', 'ROLE_COUNT_GOODS_SALE', '商品销量统计'),
('R117', 'ROLE_COUNT_GOODS_SALE_SELECT', '查询销量统计'),
('R118', 'ROLE_COUNT_GOODS_SALE_OUT', '导出销量统计'),
('R119', 'ROLE_COUNT_USER', '会员统计'),
('R120', 'ROLE_COUNT_USER_SELECT', '查询会员统计'),
('R121', 'ROLE_COUNT_USER_OUT', '导出会员统计'),
('R122', 'ROLE_COUNT_USER_SETHARD', '冻结会员统计'),

-- 5.8.1 财务统计
('R123', 'ROLE_USER_CASH_SELECT', '查询用户提现'),
('R124', 'ROLE_USER_CASH_OUT', '导出用户提现'),
('R125', 'ROLE_USER_CASH_EXAMINE', '审核用户提现'),
('R126', 'ROLE_MCH_CASH_SELECT', '查询商家提现'),
('R127', 'ROLE_MCH_CASH_OUT', '导出商家提现'),
('R128', 'ROLE_MCH_CASH_EXAMINE', '审核商家提现'),
('R129', 'ROLE_MCH_CASH_DETAILS', '商家提现详情'),
('R130', 'ROLE_USER_ACC_SELECT', '查询用户账单'),
('R131', 'ROLE_USER_ACC_OUT', '导出用户账单'),
('R132', 'ROLE_USER_ACC_BALANCE', '用户余额'),
('R133', 'ROLE_USER_ACC_LIST', '用户账单列表'),
('R134', 'ROLE_MCH_ACC_SELECT', '查询商家账单'),
('R135', 'ROLE_MCH_ACC_OUT', '导出商家账单'),
('R136', 'ROLE_MCH_ACC_BALANCE', '商家余额'),
('R137', 'ROLE_MCH_ACC_LIST', '商家账单列表'),
('R138', 'ROLE_RIDER_ACC_SELECT', '查询骑手账单'),
('R139', 'ROLE_RIDER_ACC_OUT', '导出骑手账单'),
('R140', 'ROLE_RIDER_ACC_LIST', '骑手账单列表'),
('R141', 'ROLE_RIDER_ACC_BALANCE', '骑手余额'),
('R142', 'ROLE_MAIYA_INCOME_SELECT', '查询平台收入'),
('R143', 'ROLE_MAIYA_INCOME_OUT', '导出平台收入'),

-- 5.9 消息管理
('R144', 'ROLE_MSG_TYPE_ADDINFO', '新增消息'),
('R145', 'ROLE_MSG_TYPE_DELETE', '删除消息'),
('R146', 'ROLE_MSG_TYPE_SELECT', '查询消息'),
('R147', 'ROLE_MSG_TYPE_DETAILS', '消息详情'),
('R148', 'ROLE_MSG_TYPE_ENABLE', '启用/禁用消息'),
('R149', 'ROLE_MSG_TYPE_SEND', '发送消息'),
('R150', 'ROLE_FEEDBACK_SELECT', '查询反馈'),
('R151', 'ROLE_FEEDBACK_DELETE', '删除反馈'),
('R152', 'ROLE_FEEDBACK_DETAILS', '反馈详情'),
('R153', 'ROLE_FEEDBACK_RE', '回复反馈'),
('R154', 'ROLE_COMPLAINT_SELECT', '查询投诉'),
('R155', 'ROLE_COMPLAINT_HANDLE', '处理投诉'),
('R156', 'ROLE_COMPLAINT_DETAILS', '投诉详情'),

-- 5.10 信息管理(朋友圈)
('R157', 'ROLE_CIRCLE_SELECT', '查询信息'),
('R158', 'ROLE_CIRCLE_DELETE', '删除信息'),
('R159', 'ROLE_CIRCLE_DETAILS', '信息详情'),
('R160', 'ROLE_CIRCLE_ADD', '新增信息'),
('R161', 'ROLE_SHARE_SELECT', '查询分享'),
('R162', 'ROLE_SHARE_ADD', '新增分享'),
('R163', 'ROLE_SHARE_DELETE', '删除分享'),
('R164', 'ROLE_SHARE_UPDATE', '修改分享'),

-- 5.11 配送管理
('R165', 'ROLE_DTBT_STATION', '配送站点'),
('R166', 'ROLE_DTBT_STATION_SELECT', '查询站点'),
('R167', 'ROLE_DTBT_STATION_ADD', '新增站点'),
('R168', 'ROLE_DTBT_STATION_DELETE', '删除站点'),
('R169', 'ROLE_DTBT_STATION_DETAILS', '站点详情'),
('R170', 'ROLE_DTBT_STATION_SHIELD', '站点屏蔽'),
('R171', 'ROLE_DTBT_MODE', '配送方式'),
('R172', 'ROLE_DTBT_MODE_ENABLE', '启用/禁用配送方式'),
('R173', 'ROLE_DTBT_MODE_SAVE', '保存配送方式'),
('R174', 'ROLE_DTBT_MODE_SETFEE', '设置配送费用'),
('R175', 'ROLE_DTBT_RANGE', '配送范围'),

-- 5.12 骑手管理
('R176', 'ROLE_RIDER', '骑手列表'),
('R177', 'ROLE_RIDER_SELECT', '查询骑手'),
('R178', 'ROLE_RIDER_DETAILS', '骑手详情'),
('R179', 'ROLE_RIDER_ADD', '新增骑手'),
('R180', 'ROLE_RIDER_DELETE', '删除骑手'),
('R181', 'ROLE_RIDER_EXAMINE', '审核骑手'),
('R182', 'ROLE_RIDER_ALLOT', '骑手调拨'),
('R183', 'ROLE_RIDER_ALLOT_SELECT', '查询调拨'),
('R184', 'ROLE_RIDER_ALLOT_DETAILS', '调拨详情'),
('R185', 'ROLE_RIDER_SEVICE', '骑手申请注销'),
('R186', 'ROLE_RIDER_SEVICE_SELECT', '查询注销申请'),
('R187', 'ROLE_RIDER_SEVICE_DETAILS', '注销申请详情'),

-- 5.13 系统管理
('R188', 'ROLE_SYS_ROLE', '角色管理'),
('R189', 'ROLE_SYS_ROLE_ADD', '新增角色'),
('R190', 'ROLE_SYS_ROLE_DELETE', '删除角色'),
('R191', 'ROLE_SYS_ROLE_RIGHTS', '分配权限'),
('R192', 'ROLE_SYS_ROLE_UPDATE', '修改角色'),
('R193', 'ROLE_SYS_ROLE_EXAMINE', '审核角色'),
('R194', 'ROLE_SYS_ROLE_ENABLE', '启用/禁用角色'),
('R195', 'ROLE_SYS_ROLE_LIST', '用户管理'),
('R196', 'ROLE_SYS_ROLE_LIST_ADD', '新增用户'),
('R197', 'ROLE_SYS_ROLE_LIST_DELETE', '删除用户'),
('R198', 'ROLE_SYS_ROLE_LIST_UPDATE', '修改用户'),
('R199', 'ROLE_SYS_ROLE_LIST_ENABLE', '启用/禁用用户'),
('R200', 'ROLE_MAIYA_INDEXPAGE', '平台配置'),
('R201', 'ROLE_MAIYA_VERSION', '版本管理'),
('R202', 'ROLE_MAIYA_BASEINFO', '基本设置'),
('R203', 'ROLE_MAIYA_SHARE', '分享配置')
ON DUPLICATE KEY UPDATE role_right = VALUES(role_right), name = VALUES(name);

-- ============================================================
-- 6. 插入角色数据
-- ============================================================
INSERT INTO role_info (role_id, role, role_name, role_status, create_time) VALUES
('RL001', 'ROLE_SUPERADMIN', '超级管理员', 1, UNIX_TIMESTAMP() * 1000),
('RL002', 'ROLE_ADMIN', '平台管理员', 1, UNIX_TIMESTAMP() * 1000),
('RL003', 'ROLE_MCH', '商家', 1, UNIX_TIMESTAMP() * 1000)
ON DUPLICATE KEY UPDATE role_name = VALUES(role_name), role_status = VALUES(role_status);

-- ============================================================
-- 7. 分配权限
-- ============================================================

-- 7.1 超级管理员 (RL001) — 全部权限
INSERT INTO role_right_info (role_id, right_id)
SELECT 'RL001', right_id FROM right_info
ON DUPLICATE KEY UPDATE right_id = VALUES(right_id);

-- 7.2 平台管理员 (RL002) — 平台运营相关权限（不含系统管理敏感权限）
INSERT IGNORE INTO role_right_info (role_id, right_id)
SELECT 'RL002', right_id FROM right_info WHERE right_id IN (
  -- 菜单
  'R001','R002','R003','R004','R005','R006','R007','R008','R009','R010','R011',
  -- 首页看板
  'R013','R014','R015','R018','R019','R020','R021',
  -- 商品管理
  'R022','R023','R024','R025','R026','R027','R028','R029','R030','R031','R032',
  -- 订单管理
  'R034','R035','R036','R037','R040',
  -- 售后/退款/关单
  'R041','R042','R043','R044','R045','R046','R047','R048','R049','R050','R051','R052',
  -- 商家管理
  'R053','R054','R055','R056','R057','R058','R059','R060','R061','R062','R063',
  -- 会员管理
  'R064','R065','R066','R067','R068','R069','R070','R071','R074','R075','R076','R077',
  -- 活动营销
  'R085','R087','R088','R089','R090','R091','R092','R093',
  'R094','R095','R096','R097','R098','R099','R100',
  'R101','R102','R103','R104',
  'R107','R108','R109','R110',
  -- 数据统计
  'R113','R114','R115','R116','R117','R118','R119','R120','R121','R122',
  -- 财务统计
  'R123','R124','R125','R126','R127','R128','R129','R130','R131','R132','R133',
  'R134','R135','R136','R137','R142','R143',
  -- 消息管理
  'R144','R145','R150','R151','R152','R153','R154','R155','R156',
  -- 信息管理
  'R157','R158','R159','R161','R162','R163','R164',
  -- 配送管理
  'R165','R166','R167','R168','R169','R170','R171','R172','R173','R174','R175',
  -- 骑手管理
  'R176','R177','R178','R179','R180','R181','R182','R183','R184','R185','R186','R187'
);

-- 7.3 商家 (RL003) — 仅商家相关权限
INSERT IGNORE INTO role_right_info (role_id, right_id)
SELECT 'RL003', right_id FROM right_info WHERE right_id IN (
  -- 菜单
  'R001','R002','R003','R006',
  -- 首页看板
  'R013','R014','R018','R019',
  -- 商品管理
  'R022','R023','R024','R025','R026','R027','R028','R029','R030','R031',
  -- 订单管理
  'R034','R035','R036','R037','R040',
  -- 售后/退款
  'R041','R042','R043','R044','R045','R046','R047','R048',
  -- 活动营销(商家优惠券)
  'R086',
  -- 配送管理
  'R165','R166','R171','R172','R173','R174','R175'
);

-- ============================================================
-- 8. 创建默认超级管理员
-- ============================================================
-- 密码 admin123 + 站点盐值 mkb 的 BCrypt 哈希
-- 使用 ON DUPLICATE KEY 避免与现有 admin 用户冲突
INSERT INTO user_info (uid, username, password, nickname, roles, level, enable, create_time)
SELECT UUID(), 'admin', '\$2a\$10\$Yi663lFtFnOWHozMCc8NWO9b7mPBh5/uaba92nRKKZMeDfLDXRMzG', '超级管理员', 'ROLE_SUPERADMIN', 0, 1, UNIX_TIMESTAMP() * 1000
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM user_info WHERE username = 'admin');

-- 更新已存在的 admin 用户的密码和角色
UPDATE user_info SET password = '\$2a\$10\$Yi663lFtFnOWHozMCc8NWO9b7mPBh5/uaba92nRKKZMeDfLDXRMzG',
  nickname = '超级管理员' WHERE username = 'admin';

-- 创建普通运营管理员
INSERT IGNORE INTO user_info (uid, username, password, nickname, roles, level, enable, create_time)
VALUES ('U002', 'operator', '\$2a\$10\$Yi663lFtFnOWHozMCc8NWO9b7mPBh5/uaba92nRKKZMeDfLDXRMzG', '运营管理员', 'ROLE_ADMIN', 0, 1, UNIX_TIMESTAMP() * 1000);

-- ============================================================
-- 9. 分配角色
-- ============================================================
-- 给 admin 用户分配超级管理员角色（无论 uid 是 UUID 还是 U001）
INSERT IGNORE INTO user_role (uid, role_id)
SELECT uid, 'RL001' FROM user_info WHERE username = 'admin';

INSERT IGNORE INTO user_role (uid, role_id) VALUES
('U002', 'RL002');  -- operator → 平台管理员
