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
('4b6154137f3159b3bcb6dc3057822b02', 'ROLE_INDEX_VIEW', '首页'),
('14d52b495aeb5df6bac2086ba3bb54ad', 'ROLE_GOODS_VIEW', '商品管理'),
('ad65a248d79b50dab1126186ee193728', 'ROLE_ORDER_VIEW', '订单管理'),
('66e200123510563f8d91a917c3e11096', 'ROLE_MCH_VIEW', '商家管理'),
('1cc1cd8a0a815620ae7d0b2d5eb2ce68', 'ROLE_USER_VIEW', '会员管理'),
('3d7d59dafae256b78663560d5a351f6a', 'ROLE_ACTIVE_VIEW', '活动营销'),
('e4b908614e21575da3f3fe06659dd63d', 'ROLE_COUNT_VIEW', '统计管理'),
('fcda66867f375cf1b1727b30ec79f788', 'ROLE_MSG_VIEW', '财务管理/消息管理'),
('3214e9c7d4dd5e519b48968f55e34cea', 'ROLE_CIRCLE_VIEW', '信息管理'),
('1d22676076f05a7a842a256ffdf19215', 'ROLE_DTBT_VIEW', '配送管理'),
('7ae385469a5c53bf930aa2fbf00a86b1', 'ROLE_RIDER_VIEW', '骑手管理'),
('9a9b1844f0c859bfa986710f96476a90', 'ROLE_SYS_VIEW', '系统管理'),

-- 5.2 首页看板
('3282b19f5aaf50c6ac0c46fd02b29d22', 'ROLE_ORDER_COUNT', '订单计数'),
('0249c5c8a51f5595bb06b3c18dde2a99', 'ROLE_GOODS_COUNT', '商品计数'),
('70af5a6767ee5650b61b3f5f1376d35c', 'ROLE_USER_COUNT', '用户计数'),
('92e2bd7895cf53f0bf0a3a6820775ffc', 'ROLE_TODO_LIST', '待办事项'),
('dba5ae14b0535636b84cad7f119da598', 'ROLE_TODO_LISTS', '待办事项列表'),
('e78f35a20c825373a4d3c271845583b3', 'ROLE_BUSINESS_STATUS', '营业状态'),
('329b2e0cb30b5b26aa320da7e6079a5e', 'ROLE_BUSINESS_DATA', '营业数据'),
('bf2051bef84c5200adee732482e7a059', 'ROLE_USER_DATA', '用户数据'),
('63a2342b7b2c55558d8a95a0b93385d4', 'ROLE_RANKING_DATA', '排行榜数据'),

-- 5.3 商品管理
('bc250f98ff5652068a1e2e0d8d372a36', 'ROLE_GOODS_TYPE', '分类管理'),
('df0967082d8e59edbe2319c45ca76bf5', 'ROLE_GOODS_TYPE_ADD', '新增类目'),
('930ea21449605cb8b04de29971607dee', 'ROLE_GOODS_TYPE_UPDATE', '修改类目'),
('2ebfde93ef775ffe80d86ad5a76cd833', 'ROLE_GOODS_TYPE_DELETE', '删除类目'),
('d00ac66c03685150a69c4b7fd5eab156', 'ROLE_ORDER', '商品列表'),
('b8bc0ca8e07a51aeaf35662b1d5ef159', 'ROLE_GOODS_IN', '搜索商品'),
('99ae8cceb7715f41b709166f13a16388', 'ROLE_GOODS_ADD', '新增商品'),
('fb5e39266b4d56fa9cfb435c5d145699', 'ROLE_GOODS_UPDATE', '修改商品'),
('41f93ed9134351d79471eb7a62595a9f', 'ROLE_GOODS_DELETE', '删除商品'),
('9942cdf05c6f55e4b9e1cf1bbb83e7b3', 'ROLE_GOODS_ONLINE', '上架/下架'),
('51be2f04807f5a40a0d13eb11020bff6', 'ROLE_GOODS_OUT', '导出商品'),
('24b4580ca9a25d2f9cae738745304eb6', 'ROLE_GOODS_TEMPLATE', '商品模板'),

-- 5.4 订单管理
('3740e4fef0835a6f948ef65258bb43c4', 'ROLE_ORDER_SELECT', '查询订单'),
('f329570fa2a8534b85ff5b9a375aa220', 'ROLE_ORDER_DETAILS', '查看订单详情'),
('2f3b35e88c8b5f8a9791e3c46402ccf2', 'ROLE_ORDER_PRINT', '打印订单'),
('6e4d667f779658e9b636d3996de49d1d', 'ROLE_ORDER_PRINT_ALL', '批量打印'),
('3b466c8ed5505457b3330e82f83caaf7', 'ROLE_ORDER_ADD', '新增订单'),
('70854c95e5d75c38a74f4f02e7d3ac38', 'ROLE_ORDER_HANDLE', '处理订单'),
('50a046373353529d880099e3ecd9923d', 'ROLE_ORDER_DELETE', '删除订单'),

-- 5.4.1 售后管理
('f3e4d38f4c0b537fba62b4f60ecee7fd', 'ROLE_AFTER_SALES_ADD', '新增售后'),
('e34c5eff326753cb9847e60b5a4819b3', 'ROLE_AFTER_SALES_UPDATE', '修改售后'),
('4e965f80cfb855de93efe54fc94bc29e', 'ROLE_AFTER_SALES_DELETE', '删除售后'),

-- 5.4.2 退款管理
('e8eddbbcebe15e57bda70ff6c38c3ad4', 'ROLE_ORDER_REFUND_ADD', '新增退款'),
('8288cff09c195f8dadb3d185ce0290e4', 'ROLE_ORDER_REFUND_HANDLE', '处理退款'),
('15891709bca95d87839c6f8e53f1a7a1', 'ROLE_ORDER_REFUND_DELETE', '删除退款'),
('797f2c7c22345732932ac4ba21f62627', 'ROLE_ORDER_REFUND_SELECT', '查询退款'),
('27f60d9ad1045e4ebf780f82571ec5bf', 'ROLE_ORDER_REFUND_DETAILS', '退款详情'),
('9aa76d5a91d2518885aa96d2f4ca3005', 'ROLE_ORDER_REFUND_PRINT', '打印退款单'),
('7fc0ec9130bb5f30ac217c0a1cee814e', 'ROLE_ORDER_REFUND_PRINT_ALL', '批量打印退款单'),

-- 5.4.3 关单管理
('d2838ba0f4775b8e8a2856efd467bd52', 'ROLE_ORDER_CLOSE_SELECT', '查询关单'),
('c462ef0d75c0562696b8a332b2e87a8a', 'ROLE_ORDER_CLOSE_DETAILS', '关单详情'),

-- 5.5 商家管理
('3f0f43371d68523fbb8644da7242c9cf', 'ROLE_MCH_ADD', '商家列表'),
('c3987f71df8c5298af5a34599bf4d220', 'ROLE_MCH_SELECT', '查询商家'),
('84b4c86183f659cc951b61aa9bab1348', 'ROLE_MCH_DETAILS', '商家详情'),
('dcb5485352ea54078f50d1c8d3c97af3', 'ROLE_MCH_ENABLE', '启用/禁用商家'),
('bb54683a69485d6c9b1c2fa0969ad113', 'ROLE_MCH_DELETE', '删除商家'),
('9eca783f95a859969ccb27b04420962d', 'ROLE_MCH_OUT_LIST', '导出商家列表'),
('bfa7711ee13e546286529c489ee516b6', 'ROLE_MCH_EXA', '商家审核'),
('2b24e4bcbf385444b2b3d10b75e60ec5', 'ROLE_MCH_EXA_SELECT', '查询审核'),
('124a3a81362252569edb2019f0969207', 'ROLE_MCH_EXA_DETAILS', '审核详情'),
('176504a8c4a458cab3ba3ca9d3c09b60', 'ROLE_MCH_EXA_ENABLE', '审核通过/拒绝'),
('8634e504aae3534aa4c982a53a8046b5', 'ROLE_MCH_EXA_OUT_LIST', '导出审核列表'),

-- 5.6 会员管理
('994b5621bd855c0ca4e43aa82ae9806c', 'ROLE_USER', '会员列表'),
('2eed270c56105b398b092efce61c22dd', 'ROLE_USER_SELECT', '查询会员'),
('4f901f89577e51358c23c0eb9776804b', 'ROLE_USER_DETAILS', '会员详情'),
('f4642f730a9055b5b161d11098e0262a', 'ROLE_USER_ENABLE', '启用/禁用会员'),
('5e63c08470365628b6fa6f1b2ee16ce6', 'ROLE_USER_ADD', '新增会员'),
('d6cc52b5f0b25771bbaca62c508a1855', 'ROLE_USER_DELETE', '删除会员'),
('cef65323b9ad570da2268466fdf486bb', 'ROLE_USER_OUT', '导出会员'),
('e686f9cc98a45f8aa7f24d7608438c34', 'ROLE_USER_EXAMINE', '会员审核'),
('9b9912e81d7c5d95b70b6a469417d4d4', 'ROLE_USER_DELETE_LIST', '删除列表'),
('9f78dc2b451a57e182ee4111c3198c02', 'ROLE_USER_SELECT_SETHARD', '查询冻结'),
('2a62247d8c0f5a29bafdc4978c152e6f', 'ROLE_USER_LEVEL', '会员升级审核'),
('869094d9f5e9532b8b9cbc76495b3663', 'ROLE_USER_LEVEL_AUDIT_VIEW', '查看会员升级审核'),
('a49e261bd9d65428b5e456f2e6b4a6bf', 'ROLE_USER_AUTH_AUDIT', '实名认证审核'),
('df49b086a2d55828b594dbbde02fbd81', 'ROLE_USER_AUTH_AUDIT_VIEW', '查看实名认证审核'),
('ee791906350454abb85c365ed1eeefc9', 'ROLE_USER_LEVEL_SET', '等级管理'),
('7576de7e2eca51179dc85dcf90cda392', 'ROLE_USER_LEVEL_DELETE', '删除等级'),
('c96d5390cca35bafa4c390b26d25f4e5', 'ROLE_USER_LEVEL_UPDATE', '修改等级'),
('8092b9abc8b35a3c90c18ee7c8b27c61', 'ROLE_USER_UPGRADE_TERM', '升级条件设置'),
('c6700b59b8075e6db02af57e1bd0d01e', 'ROLE_USER_UPGRADE_SAVE', '保存升级条件'),
('7998b209e3055f68995d63b4dd6acdd5', 'ROLE_USER_BONUS_SET', '分红设置'),
('c8ee3bdc11a45dcb8ff505e05fd7f177', 'ROLE_USER_BONUS_SAVE', '保存分红设置'),

-- 5.7 活动营销
('3a41db08a3f856bd8f990dc6a2a9f90b', 'ROLE_COUPON_MAIYA', '平台优惠券'),
('971f8bb5685d54469d8a0c5c1b84927a', 'ROLE_COUPON_MCH', '商家优惠券'),
('68a27e06c7c153cb9c470191a3e0a45e', 'ROLE_LOOP_PICS', '轮播图'),
('6841a9aba7e95755a6fbfc6bb95d4032', 'ROLE_LOOP_PICS_ADD', '新增轮播图'),
('37b0c8cd9d1651988e16ab7038c06783', 'ROLE_LOOP_PICS_DELETE', '删除轮播图'),
('ea898b94eab95f378a1b050505d7e041', 'ROLE_LOOP_PICS_SELECT', '查询轮播图'),
('17a77140a5b15ed1851ab7454a1f6311', 'ROLE_LOOP_PICS_ENABLE', '开启/关闭轮播图'),
('10583c29bb645152a5459cee452d84b1', 'ROLE_LOOP_PICS_DETAILS', '轮播图详情'),
('7d15fc18d0965abd9a385136005754bb', 'ROLE_LOOP_PICS_DELETE_LIST', '批量删除轮播图'),

-- 5.7.1 限时抢购
('c6c49703c6365cbc86b9663c5b7bf433', 'ROLE_FLASH_SALE_ADD', '新增抢购'),
('a273c406bd515630aeef753060243e9f', 'ROLE_FLASH_SALE_DELETE', '删除抢购'),
('7605c8fccbbd5f94b5794c9dbecc9487', 'ROLE_FLASH_SALE_DETAILS', '抢购详情'),
('1f4a45f8d7e1551a8b6486df4a93dc32', 'ROLE_FLASH_SALE_SELECT', '查询抢购'),
('fc471cb90e8d53b98f4edeb1253c27ff', 'ROLE_FLASH_SALE_ENABLE', '启用/禁用抢购'),
('f5078406ca395b3f9172e0bf84f2415f', 'ROLE_FLASH_SALE_PARTAKE', '参与记录'),
('54a184f8ddff53ff8725d1f3ad0e0bd9', 'ROLE_FLASH_SALE_EXAMINE', '审核抢购'),

-- 5.7.2 团购
('33b2d2673501511095e646506ed7daea', 'ROLE_GROUP_SALE_ADD', '新增团购'),
('4e117d23b02e5815a0082c26300424d1', 'ROLE_GROUP_SALE_SELECT', '查询团购'),
('dbcf0fa436545a7c8b946d1e3bbf7dde', 'ROLE_GROUP_SALE_ENABLE', '启用/禁用团购'),
('46dd54eaf9b95d688632786b0ac7b83d', 'ROLE_GROUP_SALE_EXAMINE', '审核团购'),
('dcc904deaeda5975bb288a7973777104', 'ROLE_GROUP_SALE_DETAILS', '团购详情'),
('b3c32f9279a157f69c7b3fcaf95dd6c6', 'ROLE_GROUP_SALE_PARTAKE', '参与记录'),

-- 5.7.3 起价了(QJL)
('1e6402bf74405fd0a7ed9c50a678b29d', 'ROLE_QJL_SALE_ADD', '新增活动'),
('b79985c803e255aabd4c7bfd2d5b29ca', 'ROLE_QJL_SALE_SELECT', '查询活动'),
('ca3aca336b605fdebbaebd5b2c24302f', 'ROLE_QJL_SALE_ENABLE', '启用/禁用'),
('04a08b98fcfb5792b3d597b56ecc015e', 'ROLE_QJL_SALE_EXAMINE', '审核活动'),
('90851e900a1f5d7f89685cab4561bf51', 'ROLE_QJL_SALE_DETAILS', '活动详情'),
('265253a590a35150bb5f90cf32562fe8', 'ROLE_QJL_SALE_PARTAKE', '参与记录'),

-- 5.8 数据统计
('64d633a458ba54cd9e6b5d138898f5fe', 'ROLE_COUNT_ORDER', '订单统计'),
('1c302578134450b4b0e2078701b582ea', 'ROLE_COUNT_ORDER_SELECT', '查询订单统计'),
('e474c9e0df3355ba9c062486ed37aee0', 'ROLE_COUNT_ORDER_OUT', '导出订单统计'),
('466bf779cfd157328759a99463e4e17e', 'ROLE_COUNT_GOODS_SALE', '商品销量统计'),
('bd6f9b36b5b451768dea02ba00ff60ad', 'ROLE_COUNT_GOODS_SALE_SELECT', '查询销量统计'),
('83c001c4d9355a64ba735464f1d0160b', 'ROLE_COUNT_GOODS_SALE_OUT', '导出销量统计'),
('c04a3081febb57778b5f2082fa03d08f', 'ROLE_COUNT_USER', '会员统计'),
('a2ab49f0bf4a5d4197c94f82198e4212', 'ROLE_COUNT_USER_SELECT', '查询会员统计'),
('7f8bbba7e12254df8b174d0e9cfaa1c6', 'ROLE_COUNT_USER_OUT', '导出会员统计'),
('2c49e9f4e5b75e93aa527dbaec96686b', 'ROLE_COUNT_USER_SETHARD', '冻结会员统计'),

-- 5.8.1 财务统计
('7492249fca6c54d2bf1b10307a42768f', 'ROLE_USER_CASH_SELECT', '查询用户提现'),
('415b4695b4d453baad9041eb8dbfa835', 'ROLE_USER_CASH_OUT', '导出用户提现'),
('96a27fafaa205209bedc688103dee02e', 'ROLE_USER_CASH_EXAMINE', '审核用户提现'),
('e85f2edea0095c969b0dc417e2679082', 'ROLE_MCH_CASH_SELECT', '查询商家提现'),
('e0f378034092568da8b527b617ef7777', 'ROLE_MCH_CASH_OUT', '导出商家提现'),
('013830a7144b5f6db83db6fa30fbb4c4', 'ROLE_MCH_CASH_EXAMINE', '审核商家提现'),
('e9622a5ae3665ab2827a73020543712e', 'ROLE_MCH_CASH_DETAILS', '商家提现详情'),
('a85d42a4ca675377985ea7510fbd61d6', 'ROLE_USER_ACC_SELECT', '查询用户账单'),
('45da5e72028f5c079970e4d414f87a03', 'ROLE_USER_ACC_OUT', '导出用户账单'),
('044ad10dd354574ea4dcb3e6906abb9e', 'ROLE_USER_ACC_BALANCE', '用户余额'),
('f470f6c502c35ec68b2fa4ea36ccc2bf', 'ROLE_USER_ACC_LIST', '用户账单列表'),
('c14c96e3146e5cd69f0e606e62812030', 'ROLE_MCH_ACC_SELECT', '查询商家账单'),
('ef7a00d035a15700ba7be7622cf3f589', 'ROLE_MCH_ACC_OUT', '导出商家账单'),
('d3bba2b3937e5c00a2d82a046d0e5f72', 'ROLE_MCH_ACC_BALANCE', '商家余额'),
('b22645e88d3956578e0620e01b7967d0', 'ROLE_MCH_ACC_LIST', '商家账单列表'),
('00b66b569cec58e485e46fa130953ce2', 'ROLE_RIDER_ACC_SELECT', '查询骑手账单'),
('40496ac223e859528f99b76dfd80d686', 'ROLE_RIDER_ACC_OUT', '导出骑手账单'),
('5edd0f08077f5155a153681f813a4b5c', 'ROLE_RIDER_ACC_LIST', '骑手账单列表'),
('5f192d0e744655f1970ccc8a687fb714', 'ROLE_RIDER_ACC_BALANCE', '骑手余额'),
('ef61bba5562d5e1e8b5b9f74db4f53bf', 'ROLE_MAIYA_INCOME_SELECT', '查询平台收入'),
('cd9a0caff6955eaea2161e57168f59dc', 'ROLE_MAIYA_INCOME_OUT', '导出平台收入'),

-- 5.9 消息管理
('c84e033312aa5a14a2138b05c6028e76', 'ROLE_MSG_TYPE_ADDINFO', '新增消息'),
('1aadd95de56b5f6caf8adfa1ef400cda', 'ROLE_MSG_TYPE_DELETE', '删除消息'),
('8732c73991c7505d96751e1be4daf922', 'ROLE_MSG_TYPE_SELECT', '查询消息'),
('c0f8b6450e9f5c11a4626d16f15b2454', 'ROLE_MSG_TYPE_DETAILS', '消息详情'),
('c6e3c61638bb55e0ae4773349f9c2a06', 'ROLE_MSG_TYPE_ENABLE', '启用/禁用消息'),
('0d7edaaa0b4358b4855c2ddc25e5bd49', 'ROLE_MSG_TYPE_SEND', '发送消息'),
('a54d0002f2485b8e9f8f176046f009cc', 'ROLE_FEEDBACK_SELECT', '查询反馈'),
('02bf0a29ea975490be4188ab6d399640', 'ROLE_FEEDBACK_DELETE', '删除反馈'),
('28d33fc89bc559958859226ed88f260f', 'ROLE_FEEDBACK_DETAILS', '反馈详情'),
('7f2e1f2b3e145f0a960572120cf9c2be', 'ROLE_FEEDBACK_RE', '回复反馈'),
('def09f7300625f75a14606f252e7a31b', 'ROLE_COMPLAINT_SELECT', '查询投诉'),
('a0411969d23750758810887b6b4f189f', 'ROLE_COMPLAINT_HANDLE', '处理投诉'),
('e65ed6aea8205c7e9980b95596bae61d', 'ROLE_COMPLAINT_DETAILS', '投诉详情'),

-- 5.10 信息管理(朋友圈)
('83692be983675a0ab005d283358a3eb2', 'ROLE_CIRCLE_SELECT', '查询信息'),
('9a37fa9300c65d2ab59a73e53379d287', 'ROLE_CIRCLE_DELETE', '删除信息'),
('8b8bc022fd4c5599a62cc1fae223d94b', 'ROLE_CIRCLE_DETAILS', '信息详情'),
('11065d4c59e657c88358c423b5bca27f', 'ROLE_CIRCLE_ADD', '新增信息'),
('21592eaaa70651f896270d87230f8409', 'ROLE_SHARE_SELECT', '查询分享'),
('1e5ffa036f8256aeb2d18ced9e2e0635', 'ROLE_SHARE_ADD', '新增分享'),
('88c7ce797d8b50d69d6b92522778513d', 'ROLE_SHARE_DELETE', '删除分享'),
('d3685da7b75452f0bbe6e3757fc0e0e7', 'ROLE_SHARE_UPDATE', '修改分享'),

-- 5.11 配送管理
('dae94e1aae745f3695e6bd9501085b99', 'ROLE_DTBT_STATION', '配送站点'),
('26e60371f35f55068cfaf3a30a521655', 'ROLE_DTBT_STATION_SELECT', '查询站点'),
('e238770e32af5c3a8aea6dcfd3f2c517', 'ROLE_DTBT_STATION_ADD', '新增站点'),
('70bce8b08e5656d2a8d78817d480571d', 'ROLE_DTBT_STATION_DELETE', '删除站点'),
('f488c0d1e945503895365dc23ceefdab', 'ROLE_DTBT_STATION_DETAILS', '站点详情'),
('b1e2d731ce37504e8ebed729a14c9fd5', 'ROLE_DTBT_STATION_SHIELD', '站点屏蔽'),
('36c5ad98cf485bb88fbc37919978fb7e', 'ROLE_DTBT_MODE', '配送方式'),
('e10d1a67b2c950b8a66d06588614b0d7', 'ROLE_DTBT_MODE_ENABLE', '启用/禁用配送方式'),
('4cd34782d08b5832ad5290bce0474b0f', 'ROLE_DTBT_MODE_SAVE', '保存配送方式'),
('2fd808f63f15541dbff2f5be8f6cb109', 'ROLE_DTBT_MODE_SETFEE', '设置配送费用'),
('71e0653a3ef95eb78d3e03b2498b4252', 'ROLE_DTBT_RANGE', '配送范围'),

-- 5.12 骑手管理
('1045bd6fb41c5ec28bb6370fe8b2e8f5', 'ROLE_RIDER', '骑手列表'),
('3abd31cbb4ab5166aabbeb4ce2e760b7', 'ROLE_RIDER_SELECT', '查询骑手'),
('bf49a36f29c05c6dbb1c10736363de63', 'ROLE_RIDER_DETAILS', '骑手详情'),
('497a0d9e4f015f17a873a4bf60070d9c', 'ROLE_RIDER_ADD', '新增骑手'),
('7cb61d6f19ec51dd841d6aa45cafdf77', 'ROLE_RIDER_DELETE', '删除骑手'),
('57d04de3647958d1935da3d49f1e1e53', 'ROLE_RIDER_EXAMINE', '审核骑手'),
('b6b5c6f0171356a8a1c8039e22a6d213', 'ROLE_RIDER_ALLOT', '骑手调拨'),
('2d9bdda9593d5606b6404c501f737e79', 'ROLE_RIDER_ALLOT_SELECT', '查询调拨'),
('e425cd8fd11b5f3ea165bc11d8bb446c', 'ROLE_RIDER_ALLOT_DETAILS', '调拨详情'),
('f754af1ba65354ef81a468ac23a96b45', 'ROLE_RIDER_SEVICE', '骑手申请注销'),
('72cc4bcd212d535580a70fbfbff37c4b', 'ROLE_RIDER_SEVICE_SELECT', '查询注销申请'),
('749c2fb572ce5a66bb469274bae06b1c', 'ROLE_RIDER_SEVICE_DETAILS', '注销申请详情'),

-- 5.13 系统管理
('46bd0b781c1b5d7193a679defde50f3b', 'ROLE_SYS_ROLE', '角色管理'),
('cb100c034c3b5970b502d044e2fdaf68', 'ROLE_SYS_ROLE_ADD', '新增角色'),
('acd93168334f53ee966b97eabbfe1e6a', 'ROLE_SYS_ROLE_DELETE', '删除角色'),
('2a74637f62ec53648fa8b3fe1e35a0e1', 'ROLE_SYS_ROLE_RIGHTS', '分配权限'),
('30bdc6a2af605c559fc041a9fd75e13b', 'ROLE_SYS_ROLE_UPDATE', '修改角色'),
('6580adfe7098531db433e9d04c22aff7', 'ROLE_SYS_ROLE_EXAMINE', '审核角色'),
('f106e5fd2bec5f9eaae6c66b1bec23e9', 'ROLE_SYS_ROLE_ENABLE', '启用/禁用角色'),
('9648d962373055a483152587a6b97464', 'ROLE_SYS_ROLE_LIST', '用户管理'),
('ed19b497990057b2a60347559e6ad045', 'ROLE_SYS_ROLE_LIST_ADD', '新增用户'),
('ea5b73ec33a35e51a014c73f9d31c332', 'ROLE_SYS_ROLE_LIST_DELETE', '删除用户'),
('13d14907b84d53f69742f71e20054414', 'ROLE_SYS_ROLE_LIST_UPDATE', '修改用户'),
('2672e359263d58448d765dbe75af1171', 'ROLE_SYS_ROLE_LIST_ENABLE', '启用/禁用用户'),
('969078ff427454d4a7760b3ca9f0b1e7', 'ROLE_MAIYA_INDEXPAGE', '平台配置'),
('a2931418feab50d5ad108dd8ffc40c5b', 'ROLE_MAIYA_VERSION', '版本管理'),
('0028bee9320f58a88de295e61a19769a', 'ROLE_MAIYA_BASEINFO', '基本设置'),
('c5de50c520605d9d83a1704ebfa42119', 'ROLE_MAIYA_SHARE', '分享配置')
ON DUPLICATE KEY UPDATE role_right = VALUES(role_right), name = VALUES(name);

-- ============================================================
-- 6. 插入角色数据
-- ============================================================
INSERT INTO role_info (role_id, role, role_name, role_status, create_time) VALUES
('940bfb18c5c45f26bc292bbc2458d4f1', 'ROLE_SUPERADMIN', '超级管理员', 1, UNIX_TIMESTAMP() * 1000),
('ab8f2a722ae85338bd2144bcb2b77573', 'ROLE_ADMIN', '平台管理员', 1, UNIX_TIMESTAMP() * 1000),
('70fbd7f86d475d0faf72284ece8cfc43', 'ROLE_MCH', '商家', 1, UNIX_TIMESTAMP() * 1000)
ON DUPLICATE KEY UPDATE role_name = VALUES(role_name), role_status = VALUES(role_status);

-- ============================================================
-- 7. 分配权限
-- ============================================================

-- 7.1 超级管理员 (RL001) — 全部权限
INSERT INTO role_right_info (role_id, right_id)
SELECT '940bfb18c5c45f26bc292bbc2458d4f1', right_id FROM right_info
ON DUPLICATE KEY UPDATE right_id = VALUES(right_id);

-- 7.2 平台管理员 (RL002) — 平台运营相关权限（不含系统管理敏感权限）
INSERT IGNORE INTO role_right_info (role_id, right_id)
SELECT 'ab8f2a722ae85338bd2144bcb2b77573', right_id FROM right_info WHERE right_id IN (
  -- 菜单
  '4b6154137f3159b3bcb6dc3057822b02','14d52b495aeb5df6bac2086ba3bb54ad','ad65a248d79b50dab1126186ee193728','66e200123510563f8d91a917c3e11096','1cc1cd8a0a815620ae7d0b2d5eb2ce68','3d7d59dafae256b78663560d5a351f6a','e4b908614e21575da3f3fe06659dd63d','fcda66867f375cf1b1727b30ec79f788','3214e9c7d4dd5e519b48968f55e34cea','1d22676076f05a7a842a256ffdf19215','7ae385469a5c53bf930aa2fbf00a86b1',
  -- 首页看板
  '3282b19f5aaf50c6ac0c46fd02b29d22','0249c5c8a51f5595bb06b3c18dde2a99','70af5a6767ee5650b61b3f5f1376d35c','e78f35a20c825373a4d3c271845583b3','329b2e0cb30b5b26aa320da7e6079a5e','bf2051bef84c5200adee732482e7a059','63a2342b7b2c55558d8a95a0b93385d4',
  -- 商品管理
  'bc250f98ff5652068a1e2e0d8d372a36','df0967082d8e59edbe2319c45ca76bf5','930ea21449605cb8b04de29971607dee','2ebfde93ef775ffe80d86ad5a76cd833','d00ac66c03685150a69c4b7fd5eab156','b8bc0ca8e07a51aeaf35662b1d5ef159','99ae8cceb7715f41b709166f13a16388','fb5e39266b4d56fa9cfb435c5d145699','41f93ed9134351d79471eb7a62595a9f','9942cdf05c6f55e4b9e1cf1bbb83e7b3','51be2f04807f5a40a0d13eb11020bff6',
  -- 订单管理
  '3740e4fef0835a6f948ef65258bb43c4','f329570fa2a8534b85ff5b9a375aa220','2f3b35e88c8b5f8a9791e3c46402ccf2','6e4d667f779658e9b636d3996de49d1d','50a046373353529d880099e3ecd9923d',
  -- 售后/退款/关单
  'f3e4d38f4c0b537fba62b4f60ecee7fd','e34c5eff326753cb9847e60b5a4819b3','4e965f80cfb855de93efe54fc94bc29e','e8eddbbcebe15e57bda70ff6c38c3ad4','8288cff09c195f8dadb3d185ce0290e4','15891709bca95d87839c6f8e53f1a7a1','797f2c7c22345732932ac4ba21f62627','27f60d9ad1045e4ebf780f82571ec5bf','9aa76d5a91d2518885aa96d2f4ca3005','7fc0ec9130bb5f30ac217c0a1cee814e','d2838ba0f4775b8e8a2856efd467bd52','c462ef0d75c0562696b8a332b2e87a8a',
  -- 商家管理
  '3f0f43371d68523fbb8644da7242c9cf','c3987f71df8c5298af5a34599bf4d220','84b4c86183f659cc951b61aa9bab1348','dcb5485352ea54078f50d1c8d3c97af3','bb54683a69485d6c9b1c2fa0969ad113','9eca783f95a859969ccb27b04420962d','bfa7711ee13e546286529c489ee516b6','2b24e4bcbf385444b2b3d10b75e60ec5','124a3a81362252569edb2019f0969207','176504a8c4a458cab3ba3ca9d3c09b60','8634e504aae3534aa4c982a53a8046b5',
  -- 会员管理
  '994b5621bd855c0ca4e43aa82ae9806c','2eed270c56105b398b092efce61c22dd','4f901f89577e51358c23c0eb9776804b','f4642f730a9055b5b161d11098e0262a','5e63c08470365628b6fa6f1b2ee16ce6','d6cc52b5f0b25771bbaca62c508a1855','cef65323b9ad570da2268466fdf486bb','e686f9cc98a45f8aa7f24d7608438c34','2a62247d8c0f5a29bafdc4978c152e6f','869094d9f5e9532b8b9cbc76495b3663','a49e261bd9d65428b5e456f2e6b4a6bf','df49b086a2d55828b594dbbde02fbd81',
  -- 活动营销
  '3a41db08a3f856bd8f990dc6a2a9f90b','68a27e06c7c153cb9c470191a3e0a45e','6841a9aba7e95755a6fbfc6bb95d4032','37b0c8cd9d1651988e16ab7038c06783','ea898b94eab95f378a1b050505d7e041','17a77140a5b15ed1851ab7454a1f6311','10583c29bb645152a5459cee452d84b1','7d15fc18d0965abd9a385136005754bb',
  'c6c49703c6365cbc86b9663c5b7bf433','a273c406bd515630aeef753060243e9f','7605c8fccbbd5f94b5794c9dbecc9487','1f4a45f8d7e1551a8b6486df4a93dc32','fc471cb90e8d53b98f4edeb1253c27ff','f5078406ca395b3f9172e0bf84f2415f','54a184f8ddff53ff8725d1f3ad0e0bd9',
  '33b2d2673501511095e646506ed7daea','4e117d23b02e5815a0082c26300424d1','dbcf0fa436545a7c8b946d1e3bbf7dde','46dd54eaf9b95d688632786b0ac7b83d',
  '1e6402bf74405fd0a7ed9c50a678b29d','b79985c803e255aabd4c7bfd2d5b29ca','ca3aca336b605fdebbaebd5b2c24302f','04a08b98fcfb5792b3d597b56ecc015e',
  -- 数据统计
  '64d633a458ba54cd9e6b5d138898f5fe','1c302578134450b4b0e2078701b582ea','e474c9e0df3355ba9c062486ed37aee0','466bf779cfd157328759a99463e4e17e','bd6f9b36b5b451768dea02ba00ff60ad','83c001c4d9355a64ba735464f1d0160b','c04a3081febb57778b5f2082fa03d08f','a2ab49f0bf4a5d4197c94f82198e4212','7f8bbba7e12254df8b174d0e9cfaa1c6','2c49e9f4e5b75e93aa527dbaec96686b',
  -- 财务统计
  '7492249fca6c54d2bf1b10307a42768f','415b4695b4d453baad9041eb8dbfa835','96a27fafaa205209bedc688103dee02e','e85f2edea0095c969b0dc417e2679082','e0f378034092568da8b527b617ef7777','013830a7144b5f6db83db6fa30fbb4c4','e9622a5ae3665ab2827a73020543712e','a85d42a4ca675377985ea7510fbd61d6','45da5e72028f5c079970e4d414f87a03','044ad10dd354574ea4dcb3e6906abb9e','f470f6c502c35ec68b2fa4ea36ccc2bf',
  'c14c96e3146e5cd69f0e606e62812030','ef7a00d035a15700ba7be7622cf3f589','d3bba2b3937e5c00a2d82a046d0e5f72','b22645e88d3956578e0620e01b7967d0','ef61bba5562d5e1e8b5b9f74db4f53bf','cd9a0caff6955eaea2161e57168f59dc',
  -- 消息管理
  'c84e033312aa5a14a2138b05c6028e76','1aadd95de56b5f6caf8adfa1ef400cda','a54d0002f2485b8e9f8f176046f009cc','02bf0a29ea975490be4188ab6d399640','28d33fc89bc559958859226ed88f260f','7f2e1f2b3e145f0a960572120cf9c2be','def09f7300625f75a14606f252e7a31b','a0411969d23750758810887b6b4f189f','e65ed6aea8205c7e9980b95596bae61d',
  -- 信息管理
  '83692be983675a0ab005d283358a3eb2','9a37fa9300c65d2ab59a73e53379d287','8b8bc022fd4c5599a62cc1fae223d94b','21592eaaa70651f896270d87230f8409','1e5ffa036f8256aeb2d18ced9e2e0635','88c7ce797d8b50d69d6b92522778513d','d3685da7b75452f0bbe6e3757fc0e0e7',
  -- 配送管理
  'dae94e1aae745f3695e6bd9501085b99','26e60371f35f55068cfaf3a30a521655','e238770e32af5c3a8aea6dcfd3f2c517','70bce8b08e5656d2a8d78817d480571d','f488c0d1e945503895365dc23ceefdab','b1e2d731ce37504e8ebed729a14c9fd5','36c5ad98cf485bb88fbc37919978fb7e','e10d1a67b2c950b8a66d06588614b0d7','4cd34782d08b5832ad5290bce0474b0f','2fd808f63f15541dbff2f5be8f6cb109','71e0653a3ef95eb78d3e03b2498b4252',
  -- 骑手管理
  '1045bd6fb41c5ec28bb6370fe8b2e8f5','3abd31cbb4ab5166aabbeb4ce2e760b7','bf49a36f29c05c6dbb1c10736363de63','497a0d9e4f015f17a873a4bf60070d9c','7cb61d6f19ec51dd841d6aa45cafdf77','57d04de3647958d1935da3d49f1e1e53','b6b5c6f0171356a8a1c8039e22a6d213','2d9bdda9593d5606b6404c501f737e79','e425cd8fd11b5f3ea165bc11d8bb446c','f754af1ba65354ef81a468ac23a96b45','72cc4bcd212d535580a70fbfbff37c4b','749c2fb572ce5a66bb469274bae06b1c'
);

-- 7.3 商家 (RL003) — 仅商家相关权限
INSERT IGNORE INTO role_right_info (role_id, right_id)
SELECT '70fbd7f86d475d0faf72284ece8cfc43', right_id FROM right_info WHERE right_id IN (
  -- 菜单
  '4b6154137f3159b3bcb6dc3057822b02','14d52b495aeb5df6bac2086ba3bb54ad','ad65a248d79b50dab1126186ee193728','3d7d59dafae256b78663560d5a351f6a',
  -- 首页看板
  '3282b19f5aaf50c6ac0c46fd02b29d22','0249c5c8a51f5595bb06b3c18dde2a99','e78f35a20c825373a4d3c271845583b3','329b2e0cb30b5b26aa320da7e6079a5e',
  -- 商品管理
  'bc250f98ff5652068a1e2e0d8d372a36','df0967082d8e59edbe2319c45ca76bf5','930ea21449605cb8b04de29971607dee','2ebfde93ef775ffe80d86ad5a76cd833','d00ac66c03685150a69c4b7fd5eab156','b8bc0ca8e07a51aeaf35662b1d5ef159','99ae8cceb7715f41b709166f13a16388','fb5e39266b4d56fa9cfb435c5d145699','41f93ed9134351d79471eb7a62595a9f','9942cdf05c6f55e4b9e1cf1bbb83e7b3',
  -- 订单管理
  '3740e4fef0835a6f948ef65258bb43c4','f329570fa2a8534b85ff5b9a375aa220','2f3b35e88c8b5f8a9791e3c46402ccf2','6e4d667f779658e9b636d3996de49d1d','50a046373353529d880099e3ecd9923d',
  -- 售后/退款
  'f3e4d38f4c0b537fba62b4f60ecee7fd','e34c5eff326753cb9847e60b5a4819b3','4e965f80cfb855de93efe54fc94bc29e','e8eddbbcebe15e57bda70ff6c38c3ad4','8288cff09c195f8dadb3d185ce0290e4','15891709bca95d87839c6f8e53f1a7a1','797f2c7c22345732932ac4ba21f62627','27f60d9ad1045e4ebf780f82571ec5bf',
  -- 活动营销(商家优惠券)
  '971f8bb5685d54469d8a0c5c1b84927a',
  -- 配送管理
  'dae94e1aae745f3695e6bd9501085b99','26e60371f35f55068cfaf3a30a521655','36c5ad98cf485bb88fbc37919978fb7e','e10d1a67b2c950b8a66d06588614b0d7','4cd34782d08b5832ad5290bce0474b0f','2fd808f63f15541dbff2f5be8f6cb109','71e0653a3ef95eb78d3e03b2498b4252'
);

-- ============================================================
-- 8. 创建默认超级管理员
-- ============================================================
-- 密码 admin123 + 站点盐值 mkb 的 BCrypt 哈希
INSERT IGNORE INTO user_info (uid, username, password, nickname, roles, level, enable, create_time)
VALUES ('15ae960323705d38bbcf186dbdbda26f', 'admin',
  '\$2a\$10\$Yi663lFtFnOWHozMCc8NWO9b7mPBh5/uaba92nRKKZMeDfLDXRMzG',
  '超级管理员', 'ROLE_SUPERADMIN', 0, 1, UNIX_TIMESTAMP() * 1000);

-- 更新已存在的 admin 用户的密码和角色
UPDATE user_info SET password = '\$2a\$10\$Yi663lFtFnOWHozMCc8NWO9b7mPBh5/uaba92nRKKZMeDfLDXRMzG',
  nickname = '超级管理员' WHERE username = 'admin';

-- 创建普通运营管理员
INSERT IGNORE INTO user_info (uid, username, password, nickname, roles, level, enable, create_time)
VALUES ('b511196a602b5d4cbdd5995c24b7b103', 'operator', '\$2a\$10\$Yi663lFtFnOWHozMCc8NWO9b7mPBh5/uaba92nRKKZMeDfLDXRMzG', '运营管理员', 'ROLE_ADMIN', 0, 1, UNIX_TIMESTAMP() * 1000);

-- ============================================================
-- 9. 分配角色
-- ============================================================
-- 给 admin 用户分配超级管理员角色
INSERT IGNORE INTO user_role (uid, role_id) VALUES
('15ae960323705d38bbcf186dbdbda26f', '940bfb18c5c45f26bc292bbc2458d4f1');

INSERT IGNORE INTO user_role (uid, role_id) VALUES
('b511196a602b5d4cbdd5995c24b7b103', 'ab8f2a722ae85338bd2144bcb2b77573');  -- operator → 平台管理员
