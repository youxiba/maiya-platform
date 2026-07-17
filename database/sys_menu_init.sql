-- ============================================================
-- 系统菜单表 + 初始化脚本
-- 基于前端菜单树结构，关联权限标识
-- ============================================================

-- -------- 菜单表 --------
CREATE TABLE IF NOT EXISTS `sys_menu` (
    `id`          INT          AUTO_INCREMENT PRIMARY KEY,
    `parent_id`   INT          DEFAULT 0        COMMENT '父菜单ID',
    `name`        VARCHAR(64)  NOT NULL          COMMENT '菜单名称',
    `path`        VARCHAR(128) DEFAULT ''        COMMENT '路由路径',
    `component`   VARCHAR(128) DEFAULT ''        COMMENT '前端组件',
    `icon`        VARCHAR(64)  DEFAULT ''        COMMENT '图标',
    `perm_code`   VARCHAR(128) DEFAULT ''        COMMENT '权限标识 (如: ROLE_ADMIN_ALL)',
    `sort_order`  INT          DEFAULT 0         COMMENT '排序号',
    `menu_type`   TINYINT      DEFAULT 1         COMMENT '1=目录 2=菜单 3=按钮',
    `visible`     TINYINT      DEFAULT 1         COMMENT '0=隐藏 1=显示',
    `status`      TINYINT      DEFAULT 1         COMMENT '0=禁用 1=启用',
    `create_time` BIGINT       DEFAULT 0,
    INDEX `idx_parent` (`parent_id`),
    INDEX `idx_perm` (`perm_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统菜单表';

-- -------- 菜单初始化数据 --------
-- parent_id=0 为顶级菜单

-- 一级菜单
INSERT INTO `sys_menu` (`parent_id`, `name`, `path`, `component`, `icon`, `perm_code`, `sort_order`, `menu_type`) VALUES
(0, '系统首页',    '/welcome',           'Welcome.vue',           'el-icon-s-home',     'ROLE_ADMIN_ALL', 1, 1),
(0, '商品档案',    '/goods',             'Layout.vue',            'el-icon-goods',      'ROLE_ADMIN_ALL', 2, 1),
(0, '订单管理',    '/order',             'Layout.vue',            'el-icon-s-order',    'ROLE_ADMIN_ALL', 3, 1),
(0, '商家管理',    '/shop',              'Layout.vue',            'el-icon-shop',       'ROLE_ADMIN_ALL', 4, 1),
(0, '会员管理',    '/member',            'Layout.vue',            'el-icon-user',       'ROLE_ADMIN_ALL', 5, 1),
(0, '营销管理',    '/marketing',         'Layout.vue',            'el-icon-present',    'ROLE_ADMIN_ALL', 6, 1),
(0, '统计报表',    '/statistics',        'Layout.vue',            'el-icon-s-data',     'ROLE_ADMIN_ALL', 7, 1),
(0, '财务管理',    '/finance',           'Cashout.vue',           'el-icon-money',      'ROLE_ADMIN_ALL', 8, 1),
(0, '消息管理',    '/message',           'Messagelist.vue',       'el-icon-message',    'ROLE_ADMIN_ALL', 9, 1),
(0, '内容管理',    '/content',           'Layout.vue',            'el-icon-document',   'ROLE_ADMIN_ALL',10, 1),
(0, '配送管理',    '/delivery',          'Layout.vue',            'el-icon-truck',      'ROLE_ADMIN_ALL',11, 1),
(0, '骑手管理',    '/rider',             'Layout.vue',            'el-icon-bicycle',    'ROLE_ADMIN_ALL',12, 1),
(0, '系统管理',    '/system',            'Layout.vue',            'el-icon-setting',    'ROLE_SUPERADMIN_ALL',13, 1);

-- 二级菜单（商品档案）
INSERT INTO `sys_menu` (`parent_id`, `name`, `path`, `component`, `perm_code`, `sort_order`, `menu_type`) VALUES
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='商品档案' LIMIT 1) AS t), '分类管理',      '/categories',        'Categories.vue',          'ROLE_ADMIN_ALL',       1, 2),
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='商品档案' LIMIT 1) AS t), '商品管理[商家]', '/goodslist',          'Goodslist.vue',            'ROLE_MERCHANT_ALL',    2, 2),
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='商品档案' LIMIT 1) AS t), '商品管理[平台]', '/maiyagoodslist',     'MaiyaGoodslist.vue',       'ROLE_ADMIN_ALL',       3, 2),
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='商品档案' LIMIT 1) AS t), '店内分类[商家]', '/storeCategory',      'StoreCategory.vue',        'ROLE_MERCHANT_ALL',    4, 2);

-- 二级菜单（订单管理）
INSERT INTO `sys_menu` (`parent_id`, `name`, `path`, `component`, `perm_code`, `sort_order`, `menu_type`) VALUES
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='订单管理' LIMIT 1) AS t), '订单列表',        '/orderlist',         'Orderlist.vue',            'ROLE_ADMIN_ALL',       1, 2),
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='订单管理' LIMIT 1) AS t), '退货/取消原因',    '/refoundreason',     'Refoundreason.vue',        'ROLE_ADMIN_ALL',       2, 2);

-- 二级菜单（商家管理）
INSERT INTO `sys_menu` (`parent_id`, `name`, `path`, `component`, `perm_code`, `sort_order`, `menu_type`) VALUES
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='商家管理' LIMIT 1) AS t), '商家列表[平台]',  '/shoplist',          'Shoplist.vue',             'ROLE_ADMIN_ALL',       1, 2),
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='商家管理' LIMIT 1) AS t), '商家入驻[平台]',  '/shopmanage',        'Shopmanage.vue',           'ROLE_ADMIN_ALL',       2, 2),
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='商家管理' LIMIT 1) AS t), '商家详情[商家]',  '/ownshop',           'Ownshop.vue',              'ROLE_MERCHANT_ALL',    3, 2);

-- 二级菜单（会员管理）
INSERT INTO `sys_menu` (`parent_id`, `name`, `path`, `component`, `perm_code`, `sort_order`, `menu_type`) VALUES
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='会员管理' LIMIT 1) AS t), '会员列表',        '/memberlist',        'Memberlist.vue',           'ROLE_ADMIN_ALL',       1, 2),
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='会员管理' LIMIT 1) AS t), '升级审核',        '/updatecheck',       'Updatecheck.vue',          'ROLE_ADMIN_ALL',       2, 2),
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='会员管理' LIMIT 1) AS t), '实名认证审核',     '/idencheck',         'Idencheck.vue',            'ROLE_ADMIN_ALL',       3, 2),
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='会员管理' LIMIT 1) AS t), '等级管理',        '/updateset',         'Updateset.vue',            'ROLE_SUPERADMIN_ALL',  4, 2);

-- 二级菜单（营销管理）
INSERT INTO `sys_menu` (`parent_id`, `name`, `path`, `component`, `perm_code`, `sort_order`, `menu_type`) VALUES
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='营销管理' LIMIT 1) AS t), '抽奖转盘',          '/lottery',              'Lottery.vue',             'ROLE_ADMIN_ALL',  1, 2),
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='营销管理' LIMIT 1) AS t), '精选好店',          '/goodstoreslist',       'Goodstoreslist.vue',      'ROLE_ADMIN_ALL',  2, 2),
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='营销管理' LIMIT 1) AS t), '优惠券[平台]',      '/platformcouponlist',   'Platformcouponlist.vue',  'ROLE_ADMIN_ALL',  3, 2),
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='营销管理' LIMIT 1) AS t), '优惠券[商家]',      '/shopcoulist',          'Shopcoulist.vue',         'ROLE_MERCHANT_ALL', 4, 2),
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='营销管理' LIMIT 1) AS t), '轮播图',           '/slideshowlist',        'Slideshowlist.vue',       'ROLE_ADMIN_ALL',  5, 2);

-- 二级菜单（统计报表）
INSERT INTO `sys_menu` (`parent_id`, `name`, `path`, `component`, `perm_code`, `sort_order`, `menu_type`) VALUES
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='统计报表' LIMIT 1) AS t), '订单统计',        '/orderchartlist',     'Orderchartlist.vue',       'ROLE_ADMIN_ALL',  1, 2),
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='统计报表' LIMIT 1) AS t), '商品销量统计',     '/salesvolume',        'Salesvolume.vue',          'ROLE_ADMIN_ALL',  2, 2),
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='统计报表' LIMIT 1) AS t), '配送统计',        '/sendtotaillist',     'Sendtotaillist.vue',       'ROLE_ADMIN_ALL',  3, 2),
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='统计报表' LIMIT 1) AS t), '会员收支',        '/membershiplist',     'Membershiplist.vue',       'ROLE_ADMIN_ALL',  4, 2);

-- 二级菜单（内容管理）
INSERT INTO `sys_menu` (`parent_id`, `name`, `path`, `component`, `perm_code`, `sort_order`, `menu_type`) VALUES
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='内容管理' LIMIT 1) AS t), '发现列表[商家]',  '/findpage',           'Findpage.vue',             'ROLE_MERCHANT_ALL',  1, 2),
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='内容管理' LIMIT 1) AS t), '发现审核[平台]',  '/findcheck',          'Findcheck.vue',            'ROLE_ADMIN_ALL',     2, 2);

-- 二级菜单（配送管理）
INSERT INTO `sys_menu` (`parent_id`, `name`, `path`, `component`, `perm_code`, `sort_order`, `menu_type`) VALUES
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='配送管理' LIMIT 1) AS t), '配送站点',        '/deliverystation',    'Deliverystation.vue',      'ROLE_ADMIN_ALL',     1, 2),
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='配送管理' LIMIT 1) AS t), '配送方式',        '/deliverytype',       'Deliverytype.vue',         'ROLE_ADMIN_ALL',     2, 2),
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='配送管理' LIMIT 1) AS t), '配送地图',        '/deliverymap',        'Deliverymap.vue',          'ROLE_ADMIN_ALL',     3, 2);

-- 二级菜单（骑手管理）
INSERT INTO `sys_menu` (`parent_id`, `name`, `path`, `component`, `perm_code`, `sort_order`, `menu_type`) VALUES
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='骑手管理' LIMIT 1) AS t), '骑手列表',        '/senderlist',         'Senderlist.vue',           'ROLE_ADMIN_ALL',     1, 2),
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='骑手管理' LIMIT 1) AS t), '骑手申请/退出',    '/senderapply',        'Senderapply.vue',          'ROLE_ADMIN_ALL',     2, 2),
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='骑手管理' LIMIT 1) AS t), '骑手调拨',        '/senderchange',       'Senderchange.vue',         'ROLE_ADMIN_ALL',     3, 2);

-- 二级菜单（系统管理）
INSERT INTO `sys_menu` (`parent_id`, `name`, `path`, `component`, `perm_code`, `sort_order`, `menu_type`) VALUES
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='系统管理' LIMIT 1) AS t), '角色管理',        '/rolemanage',         'Rolemanage.vue',           'ROLE_SUPERADMIN_ALL', 1, 2),
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='系统管理' LIMIT 1) AS t), '用户管理',        '/adduser',            'Adduser.vue',              'ROLE_SUPERADMIN_ALL', 2, 2),
((SELECT id FROM (SELECT id FROM sys_menu WHERE name='系统管理' LIMIT 1) AS t), '平台配置',        '/bootpage',           'Bootpage.vue',             'ROLE_SUPERADMIN_ALL', 3, 2);
