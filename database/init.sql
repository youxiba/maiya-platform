-- ============================================================
-- 麦芽优选 (ny.shop.youxuan) 数据库初始化脚本
-- 生成时间: 2026-07-08
-- 说明: 每个微服务对应独立 database，按需执行
-- ============================================================

-- ============================================================
-- 1. USER SERVICE - maiya_user
-- ============================================================
CREATE DATABASE IF NOT EXISTS maiya_user DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE maiya_user;

CREATE TABLE user_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    uid VARCHAR(64) NOT NULL UNIQUE,
    username VARCHAR(64) DEFAULT '',
    password VARCHAR(128) DEFAULT '',
    creator_uid VARCHAR(64) DEFAULT '',
    mid VARCHAR(64) DEFAULT '',
    realname VARCHAR(64) DEFAULT '',
    nickname VARCHAR(64) DEFAULT '',
    avatar VARCHAR(200) DEFAULT '',
    apple_id VARCHAR(128) DEFAULT '',
    qqid VARCHAR(128) DEFAULT '',
    union_id VARCHAR(128) DEFAULT '',
    open_id VARCHAR(128) DEFAULT '',
    wx_nickname VARCHAR(128) DEFAULT '',
    wx_avatar VARCHAR(200) DEFAULT '',
    telephone VARCHAR(32) DEFAULT '',
    invite_code VARCHAR(32) DEFAULT '',
    super_uid VARCHAR(64) DEFAULT '',
    super_code VARCHAR(32) DEFAULT '',
    big_group_uid VARCHAR(64) DEFAULT '',
    part_group_uid VARCHAR(64) DEFAULT '',
    level INT DEFAULT 0,
    roles VARCHAR(128) DEFAULT '',
    create_time BIGINT DEFAULT 0,
    regtime BIGINT DEFAULT 0,
    last_password_reset_date BIGINT DEFAULT 0,
    birthday BIGINT DEFAULT 0,
    phone_binding_flag TINYINT(1) DEFAULT 0,
    app_binding_flag TINYINT(1) DEFAULT 0,
    enable TINYINT(1) DEFAULT 1,
    reg_source VARCHAR(32) DEFAULT '',
    address_id VARCHAR(64) DEFAULT '',
    account_name VARCHAR(64) DEFAULT '',
    bank_name VARCHAR(64) DEFAULT '',
    bank_branch VARCHAR(128) DEFAULT '',
    bank_card_num VARCHAR(64) DEFAULT '',
    bank_phone VARCHAR(32) DEFAULT '',
    alipay_name VARCHAR(64) DEFAULT '',
    alipay_num VARCHAR(64) DEFAULT '',
    wxpay_name VARCHAR(64) DEFAULT '',
    wxpay_url VARCHAR(200) DEFAULT '',
    KEY idx_telephone (telephone),
    KEY idx_invite_code (invite_code),
    KEY idx_union_id (union_id),
    KEY idx_super_uid (super_uid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 2. MERCHANT SERVICE - maiya_merchant
-- ============================================================
CREATE DATABASE IF NOT EXISTS maiya_merchant DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE maiya_merchant;

CREATE TABLE merchant_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    uid VARCHAR(64) NOT NULL UNIQUE,
    mid VARCHAR(64) NOT NULL UNIQUE,
    username VARCHAR(128) DEFAULT '',
    telephone VARCHAR(32) DEFAULT '',
    mch_name VARCHAR(128) DEFAULT '',
    store_name VARCHAR(128) DEFAULT '',
    province VARCHAR(64) DEFAULT '',
    city VARCHAR(64) DEFAULT '',
    district VARCHAR(64) DEFAULT '',
    landmark_name VARCHAR(128) DEFAULT '',
    landmark_address VARCHAR(128) DEFAULT '',
    address VARCHAR(128) DEFAULT '',
    lon DOUBLE DEFAULT 120.0,
    lat DOUBLE DEFAULT 30.0,
    store_type INT DEFAULT 0,
    goods_category_id VARCHAR(64) DEFAULT '',
    goods VARCHAR(256) DEFAULT '',
    coupons VARCHAR(256) DEFAULT '',
    logo VARCHAR(200) DEFAULT '',
    bs_license_pic VARCHAR(200) DEFAULT '',
    food_license_pic VARCHAR(200) DEFAULT '',
    card_face VARCHAR(200) DEFAULT '',
    card_back VARCHAR(200) DEFAULT '',
    card_num VARCHAR(64) DEFAULT '',
    account_name VARCHAR(64) DEFAULT '',
    bank_name VARCHAR(64) DEFAULT '',
    bank_branch VARCHAR(128) DEFAULT '',
    bank_card_num VARCHAR(64) DEFAULT '',
    station_id VARCHAR(64) DEFAULT '',
    station_flag TINYINT(1) DEFAULT 0,
    dtbt_type INT DEFAULT 0,
    dtbt_id VARCHAR(64) DEFAULT '',
    audit_status INT DEFAULT 0,
    auditor VARCHAR(64) DEFAULT '',
    audit_remark VARCHAR(128) DEFAULT '',
    audit_time BIGINT DEFAULT 0,
    online TINYINT(1) DEFAULT 0,
    ratio INT DEFAULT 0,
    points INT DEFAULT 0,
    bond DECIMAL(19,2) DEFAULT 0.00,
    start_price INT DEFAULT 0,
    dtbt_start_price DECIMAL(19,2) DEFAULT 0.00,
    jinxuan_flag TINYINT(1) DEFAULT 0,
    jinxuan_status INT DEFAULT 0,
    jinxuan_end_time BIGINT DEFAULT 0,
    jinxuan_serial INT DEFAULT 0,
    create_time BIGINT DEFAULT 0,
    apply_time BIGINT DEFAULT 0,
    KEY idx_mid (mid),
    KEY idx_uid (uid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE merchant_hours (
    id INT AUTO_INCREMENT PRIMARY KEY,
    uid VARCHAR(64) NOT NULL,
    work_day VARCHAR(128) DEFAULT '',
    start_time1 BIGINT DEFAULT 0,
    end_time1 BIGINT DEFAULT 0,
    start_time2 BIGINT DEFAULT 0,
    end_time2 BIGINT DEFAULT 0,
    start_time3 BIGINT DEFAULT 0,
    end_time3 BIGINT DEFAULT 0,
    work_status TINYINT(1) DEFAULT 1,
    open_flag TINYINT(1) DEFAULT 1,
    KEY idx_uid (uid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 3. GOODS SERVICE - maiya_goods
-- ============================================================
CREATE DATABASE IF NOT EXISTS maiya_goods DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE maiya_goods;

CREATE TABLE goods_category (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category_id VARCHAR(64) NOT NULL,
    category_name VARCHAR(64) DEFAULT '',
    level INT DEFAULT 0,
    sort INT DEFAULT 0,
    UNIQUE KEY uk_category_id (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE goods_type (
    id INT AUTO_INCREMENT PRIMARY KEY,
    type_id VARCHAR(64) NOT NULL,
    category_id VARCHAR(64) DEFAULT '',
    type_name VARCHAR(64) DEFAULT '',
    sort INT DEFAULT 0,
    KEY idx_category (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE goods_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    mid VARCHAR(64) NOT NULL,
    goods_info_id VARCHAR(64) NOT NULL,
    goods_id VARCHAR(64) NOT NULL,
    goods_num VARCHAR(64) DEFAULT '',
    goods_name VARCHAR(128) DEFAULT '',
    store_name VARCHAR(128) DEFAULT '',
    category_id VARCHAR(64) DEFAULT '',
    goods_category VARCHAR(64) DEFAULT '',
    goods_type_id VARCHAR(64) DEFAULT '',
    goods_type VARCHAR(64) DEFAULT '',
    mch_inner_type_id VARCHAR(64) DEFAULT '',
    mch_inner_name VARCHAR(64) DEFAULT '',
    pics_name VARCHAR(2000) DEFAULT '',
    pics_url VARCHAR(1000) DEFAULT '',
    goods_video VARCHAR(1000) DEFAULT '',
    goods_desc TEXT,
    goods_cost DECIMAL(19,2) DEFAULT 0.00,
    market_price DECIMAL(19,2) DEFAULT 0.00,
    goods_price DECIMAL(19,2) DEFAULT 0.00,
    active_price DECIMAL(19,2) DEFAULT 0.00,
    rebate DECIMAL(19,2) DEFAULT 0.00,
    rebonus DECIMAL(19,2) DEFAULT 0.00,
    sum INT DEFAULT 0,
    sales INT DEFAULT 0,
    score DOUBLE DEFAULT 5.0,
    sta_flag TINYINT(1) DEFAULT 0,
    spec_flag TINYINT(1) DEFAULT 0,
    attri_flag TINYINT(1) DEFAULT 0,
    spec_count INT DEFAULT 0,
    active_id VARCHAR(64) DEFAULT '',
    active_type INT DEFAULT 0,
    active_status INT DEFAULT 0,
    points INT DEFAULT 0,
    packaging_flag TINYINT(1) DEFAULT 0,
    packaging_num INT DEFAULT 0,
    packaging_fee DECIMAL(19,2) DEFAULT 0.00,
    start_send_flag TINYINT(1) DEFAULT 0,
    start_send_num INT DEFAULT 0,
    limit_purchase_flag TINYINT(1) DEFAULT 0,
    limit_purchase_num INT DEFAULT 0,
    recommend_flag TINYINT(1) DEFAULT 0,
    recommend_num INT DEFAULT 0,
    shelf_time BIGINT DEFAULT 0,
    telephone VARCHAR(32) DEFAULT '',
    del_at TINYINT(1) DEFAULT 0,
    KEY idx_mid (mid),
    KEY idx_goods_info_id (goods_info_id),
    KEY idx_category (category_id),
    KEY idx_active (active_id, active_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE goods_sku (
    id INT AUTO_INCREMENT PRIMARY KEY,
    goods_info_id VARCHAR(64) NOT NULL,
    goods_id VARCHAR(64) DEFAULT '',
    spec_info VARCHAR(512) DEFAULT '',
    goods_price DECIMAL(19,2) DEFAULT 0.00,
    market_price DECIMAL(19,2) DEFAULT 0.00,
    cost_price DECIMAL(19,2) DEFAULT 0.00,
    sum INT DEFAULT 0,
    spec_flag TINYINT(1) DEFAULT 0,
    stock_id VARCHAR(64) DEFAULT '',
    KEY idx_goods_info (goods_info_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE goods_spec (
    id INT AUTO_INCREMENT PRIMARY KEY,
    spec_id VARCHAR(64) NOT NULL,
    goods_info_id VARCHAR(64) NOT NULL,
    spec_name VARCHAR(64) DEFAULT '',
    sort INT DEFAULT 0,
    KEY idx_goods_info (goods_info_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE shopping_cart (
    id INT AUTO_INCREMENT PRIMARY KEY,
    uid VARCHAR(64) NOT NULL,
    mid VARCHAR(64) DEFAULT '',
    goods_info_id VARCHAR(64) DEFAULT '',
    goods_id VARCHAR(64) DEFAULT '',
    goods_name VARCHAR(128) DEFAULT '',
    goods_url VARCHAR(200) DEFAULT '',
    goods_price DECIMAL(19,2) DEFAULT 0.00,
    qty INT DEFAULT 0,
    specs_models VARCHAR(512) DEFAULT '',
    attri_collections VARCHAR(512) DEFAULT '',
    valid TINYINT(1) DEFAULT 1,
    create_time BIGINT DEFAULT 0,
    KEY idx_uid (uid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE goods_score (
    id INT AUTO_INCREMENT PRIMARY KEY,
    info_id VARCHAR(64) NOT NULL,
    goods_info_id VARCHAR(64) DEFAULT '',
    order_id VARCHAR(64) DEFAULT '',
    rater VARCHAR(64) DEFAULT '',
    score DOUBLE DEFAULT 5.0,
    remark VARCHAR(200) DEFAULT '',
    create_time BIGINT DEFAULT 0,
    KEY idx_goods_info (goods_info_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE store_score (
    id INT AUTO_INCREMENT PRIMARY KEY,
    info_id VARCHAR(64) NOT NULL,
    mid VARCHAR(64) DEFAULT '',
    order_id VARCHAR(64) DEFAULT '',
    rater VARCHAR(64) DEFAULT '',
    score DOUBLE DEFAULT 5.0,
    remark VARCHAR(200) DEFAULT '',
    create_time BIGINT DEFAULT 0,
    KEY idx_mid (mid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 4. ORDER SERVICE - maiya_order
-- ============================================================
CREATE DATABASE IF NOT EXISTS maiya_order DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE maiya_order;

CREATE TABLE order_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id VARCHAR(64) NOT NULL,
    sub_id VARCHAR(64) NOT NULL UNIQUE,
    group_id VARCHAR(64) DEFAULT '',
    transaction_id VARCHAR(128) DEFAULT '',
    uid VARCHAR(64) NOT NULL,
    username VARCHAR(64) DEFAULT '',
    nickname VARCHAR(64) DEFAULT '',
    tele_phone VARCHAR(32) DEFAULT '',
    mid VARCHAR(64) NOT NULL,
    store_name VARCHAR(128) DEFAULT '',
    category_id VARCHAR(64) DEFAULT '',
    goods_category VARCHAR(64) DEFAULT '',
    goods_type_id VARCHAR(64) DEFAULT '',
    mch_inner_type_id VARCHAR(64) DEFAULT '',
    goods_info_id VARCHAR(64) NOT NULL,
    goods_id VARCHAR(64) NOT NULL,
    goods_name VARCHAR(128) DEFAULT '',
    goods_url VARCHAR(200) DEFAULT '',
    specs_models TEXT,
    attri_collections TEXT,
    goods_cost DECIMAL(19,2) DEFAULT 0.00,
    goods_price DECIMAL(19,2) DEFAULT 0.00,
    market_price DECIMAL(19,2) DEFAULT 0.00,
    unit_price DECIMAL(19,2) DEFAULT 0.00,
    ahsr DECIMAL(19,2) DEFAULT 0.00,
    ahsr_sum DECIMAL(19,2) DEFAULT 0.00,
    total_fee DECIMAL(19,2) DEFAULT 0.00,
    total_fee_sum DECIMAL(19,2) DEFAULT 0.00,
    total_packing_fee DECIMAL(19,2) DEFAULT 0.00,
    maiya_fee DECIMAL(19,2) DEFAULT 0.00,
    maiya_fee_sum DECIMAL(19,2) DEFAULT 0.00,
    dtbt_fee DECIMAL(19,2) DEFAULT 0.00,
    mch_dtbt_fee DECIMAL(19,2) DEFAULT 0.00,
    refund_fee DECIMAL(19,2) DEFAULT 0.00,
    coupon_fee DECIMAL(19,2) DEFAULT 0.00,
    coupon_fee_sum DECIMAL(19,2) DEFAULT 0.00,
    subs_fee DECIMAL(19,2) DEFAULT 0.00,
    packaging_fee DECIMAL(19,2) DEFAULT 0.00,
    goods_packaging_fee DECIMAL(19,2) DEFAULT 0.00,
    rebate DECIMAL(19,2) DEFAULT 0.00,
    rebonus DECIMAL(19,2) DEFAULT 0.00,
    oabonus DECIMAL(19,2) DEFAULT 0.00,
    coupon_info VARCHAR(500) DEFAULT '',
    remark VARCHAR(128) DEFAULT '',
    landmark_name VARCHAR(128) DEFAULT '',
    address VARCHAR(128) DEFAULT '',
    lon DOUBLE DEFAULT 120.0,
    lat DOUBLE DEFAULT 30.0,
    addr_id VARCHAR(64) DEFAULT '',
    rid VARCHAR(64) DEFAULT '',
    dtbt_id VARCHAR(64) DEFAULT '',
    qty INT DEFAULT 0,
    qty_sum INT DEFAULT 0,
    sub_sum INT DEFAULT 0,
    points INT DEFAULT 0,
    print_sum INT DEFAULT 0,
    pay_type INT DEFAULT 0,
    dtbt_type INT DEFAULT 0,
    re_dtbt_type INT DEFAULT 0,
    order_type INT DEFAULT 0,
    order_close_type INT DEFAULT 0,
    store_type INT DEFAULT 0,
    order_status INT DEFAULT 0,
    ass_status INT DEFAULT 0,
    dtbt_status INT DEFAULT 0,
    order_source INT DEFAULT 0,
    create_time BIGINT DEFAULT 0,
    order_time BIGINT DEFAULT 0,
    pay_time BIGINT DEFAULT 0,
    refund_apply_time BIGINT DEFAULT 0,
    refundment_time BIGINT DEFAULT 0,
    refund_time BIGINT DEFAULT 0,
    close_time BIGINT DEFAULT 0,
    send_time BIGINT DEFAULT 0,
    sendend_time BIGINT DEFAULT 0,
    exp_time BIGINT DEFAULT 0,
    finish_time BIGINT DEFAULT 0,
    acc_time BIGINT DEFAULT 0,
    qtid VARCHAR(64) DEFAULT '',
    qt_name VARCHAR(64) DEFAULT '',
    refund_desc VARCHAR(200) DEFAULT '',
    refund_url VARCHAR(500) DEFAULT '',
    refundmy_url VARCHAR(500) DEFAULT '',
    reason VARCHAR(128) DEFAULT '',
    myreason VARCHAR(128) DEFAULT '',
    goods_score DOUBLE DEFAULT 5.0,
    sco_sum INT DEFAULT 0,
    goodsco_sum INT DEFAULT 0,
    group_id VARCHAR(64) DEFAULT '',
    group_sum INT DEFAULT 0,
    group_lim INT DEFAULT 0,
    KEY idx_order_id (order_id),
    KEY idx_sub_id (sub_id),
    KEY idx_uid (uid),
    KEY idx_mid (mid),
    KEY idx_order_status (order_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- CQRS 读模型
CREATE TABLE order_view (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id VARCHAR(64) NOT NULL,
    uid VARCHAR(64) DEFAULT '',
    username VARCHAR(64) DEFAULT '',
    nickname VARCHAR(64) DEFAULT '',
    mid VARCHAR(64) DEFAULT '',
    store_name VARCHAR(128) DEFAULT '',
    goods_info_id VARCHAR(64) DEFAULT '',
    goods_name VARCHAR(128) DEFAULT '',
    goods_url VARCHAR(200) DEFAULT '',
    ahsr_sum DECIMAL(19,2) DEFAULT 0.00,
    total_fee DECIMAL(19,2) DEFAULT 0.00,
    order_status INT DEFAULT 0,
    pay_type INT DEFAULT 0,
    create_time BIGINT DEFAULT 0,
    pay_time BIGINT DEFAULT 0,
    KEY idx_order_id (order_id),
    KEY idx_uid (uid),
    KEY idx_mid (mid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 5. PAYMENT SERVICE - maiya_payment
-- ============================================================
CREATE DATABASE IF NOT EXISTS maiya_payment DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE maiya_payment;

CREATE TABLE payment_record (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id VARCHAR(64) NOT NULL,
    transaction_id VARCHAR(128) DEFAULT '',
    pay_type VARCHAR(32) DEFAULT '',
    fee DECIMAL(19,2) DEFAULT 0.00,
    trade_status VARCHAR(32) DEFAULT 'PENDING',
    create_time BIGINT DEFAULT 0,
    notify_time BIGINT DEFAULT 0,
    notify_data TEXT,
    KEY idx_order_id (order_id),
    KEY idx_transaction_id (transaction_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 6. FINANCE SERVICE - maiya_finance
-- ============================================================
CREATE DATABASE IF NOT EXISTS maiya_finance DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE maiya_finance;

CREATE TABLE wallet_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    uid VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(64) DEFAULT '111111',
    money DECIMAL(19,2) DEFAULT 0.00,
    mch_money DECIMAL(19,2) DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE account_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    serial_num VARCHAR(64) NOT NULL,
    order_id VARCHAR(64) DEFAULT '',
    sub_id VARCHAR(64) DEFAULT '',
    uid VARCHAR(64) DEFAULT '',
    mid VARCHAR(64) DEFAULT '',
    trader_uid VARCHAR(64) DEFAULT '',
    goods_name VARCHAR(128) DEFAULT '',
    goods_url VARCHAR(200) DEFAULT '',
    remark VARCHAR(300) DEFAULT '',
    bal_type INT DEFAULT 0,
    fund_source INT DEFAULT 0,
    fund_flow INT DEFAULT 0,
    account_type INT DEFAULT 0,
    money DECIMAL(19,2) DEFAULT 0.00,
    balance DECIMAL(19,2) DEFAULT 0.00,
    create_time BIGINT DEFAULT 0,
    bill_time BIGINT DEFAULT 0,
    KEY idx_uid (uid),
    KEY idx_mid (mid),
    KEY idx_order_id (order_id),
    KEY idx_serial_num (serial_num)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE cefts_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    info_id VARCHAR(64) NOT NULL,
    uid VARCHAR(64) NOT NULL,
    telephone VARCHAR(32) DEFAULT '',
    wallet_type INT DEFAULT 0,
    audit_status INT DEFAULT 0,
    fee DECIMAL(19,2) DEFAULT 0.00,
    acc_time BIGINT DEFAULT 0,
    remark VARCHAR(128) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE level_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ztsum INT DEFAULT 0,
    jtsum INT DEFAULT 0,
    income DECIMAL(19,2) DEFAULT 0.00,
    consum DECIMAL(19,2) DEFAULT 0.00,
    mgrsum INT DEFAULT 0,
    mgincome DECIMAL(19,2) DEFAULT 0.00,
    mgconsum DECIMAL(19,2) DEFAULT 0.00,
    my_ratio INT DEFAULT 0,
    mch_ratio INT DEFAULT 0,
    re_ratio INT DEFAULT 0,
    zt_ratio INT DEFAULT 0,
    yj_ratio INT DEFAULT 0,
    yw_ratio INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 7. DELIVERY SERVICE - maiya_delivery
-- ============================================================
CREATE DATABASE IF NOT EXISTS maiya_delivery DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE maiya_delivery;

CREATE TABLE dtbt_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    info_id VARCHAR(64) NOT NULL,
    order_id VARCHAR(64) DEFAULT '',
    sub_id VARCHAR(64) DEFAULT '',
    uid VARCHAR(64) DEFAULT '',
    mid VARCHAR(64) DEFAULT '',
    rid VARCHAR(64) DEFAULT '',
    station_id VARCHAR(64) DEFAULT '',
    dtbt_order_type INT DEFAULT 0,
    dtbt_status INT DEFAULT 0,
    dtbt_fee DECIMAL(19,2) DEFAULT 0.00,
    store_name VARCHAR(128) DEFAULT '',
    store_address VARCHAR(256) DEFAULT '',
    store_phone VARCHAR(32) DEFAULT '',
    store_lon DOUBLE DEFAULT 0.0,
    store_lat DOUBLE DEFAULT 0.0,
    user_name VARCHAR(64) DEFAULT '',
    user_address VARCHAR(256) DEFAULT '',
    user_phone VARCHAR(32) DEFAULT '',
    user_lon DOUBLE DEFAULT 0.0,
    user_lat DOUBLE DEFAULT 0.0,
    send_time BIGINT DEFAULT 0,
    sendend_time BIGINT DEFAULT 0,
    create_time BIGINT DEFAULT 0,
    KEY idx_order_id (order_id),
    KEY idx_rid (rid),
    KEY idx_mid (mid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE dtbt_station (
    id INT AUTO_INCREMENT PRIMARY KEY,
    station_id VARCHAR(64) NOT NULL,
    station_name VARCHAR(128) DEFAULT '',
    city VARCHAR(64) DEFAULT '',
    district VARCHAR(64) DEFAULT '',
    lon DOUBLE DEFAULT 0.0,
    lat DOUBLE DEFAULT 0.0,
    manager_name VARCHAR(64) DEFAULT '',
    manager_phone VARCHAR(32) DEFAULT '',
    UNIQUE KEY uk_station_id (station_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE rider_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    rid VARCHAR(64) NOT NULL,
    uid VARCHAR(64) DEFAULT '',
    realname VARCHAR(64) DEFAULT '',
    telephone VARCHAR(32) DEFAULT '',
    station_id VARCHAR(64) DEFAULT '',
    audit_status INT DEFAULT 0,
    online TINYINT(1) DEFAULT 0,
    KEY idx_rid (rid),
    KEY idx_station (station_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE rider_score (
    id INT AUTO_INCREMENT PRIMARY KEY,
    info_id VARCHAR(64) NOT NULL,
    rid VARCHAR(64) DEFAULT '',
    order_id VARCHAR(64) DEFAULT '',
    rater VARCHAR(64) DEFAULT '',
    score DOUBLE DEFAULT 5.0,
    remark VARCHAR(200) DEFAULT '',
    create_time BIGINT DEFAULT 0,
    KEY idx_rid (rid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 8. NOTIFICATION SERVICE - maiya_notification
-- ============================================================
CREATE DATABASE IF NOT EXISTS maiya_notification DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE maiya_notification;

CREATE TABLE message_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    info_id VARCHAR(64) NOT NULL,
    uid VARCHAR(64) DEFAULT '',
    msg_type INT DEFAULT 0,
    msg_title VARCHAR(128) DEFAULT '',
    order_id VARCHAR(64) DEFAULT '',
    msg_status INT DEFAULT 0,
    push_type INT DEFAULT 0,
    msg_text TEXT,
    msg_url VARCHAR(200) DEFAULT '',
    goods_url VARCHAR(200) DEFAULT '',
    create_time BIGINT DEFAULT 0,
    KEY idx_uid (uid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE printer_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    info_id VARCHAR(64) NOT NULL,
    uid VARCHAR(64) DEFAULT '',
    mid VARCHAR(64) DEFAULT '',
    name VARCHAR(64) DEFAULT '',
    machine_code VARCHAR(64) DEFAULT '',
    printer_id VARCHAR(64) DEFAULT '',
    api_key VARCHAR(128) DEFAULT '',
    api_secret VARCHAR(128) DEFAULT '',
    enable TINYINT(1) DEFAULT 0,
    auto TINYINT(1) DEFAULT 0,
    mch_paper_flag TINYINT(1) DEFAULT 0,
    KEY idx_mid (mid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE print_token (
    id INT AUTO_INCREMENT PRIMARY KEY,
    set_time BIGINT DEFAULT 0,
    token VARCHAR(300) DEFAULT '',
    refresh_token VARCHAR(300) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 9. MARKETING SERVICE - maiya_marketing
-- ============================================================
CREATE DATABASE IF NOT EXISTS maiya_marketing DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE maiya_marketing;

CREATE TABLE flash_sale (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fs_id VARCHAR(64) NOT NULL,
    fs_name VARCHAR(64) DEFAULT '',
    fs_desc VARCHAR(256) DEFAULT '',
    fs_mchs INT DEFAULT 0,
    mchs INT DEFAULT 0,
    fs_qty INT DEFAULT 0,
    start_date BIGINT DEFAULT 0,
    end_date BIGINT DEFAULT 0,
    start_time BIGINT DEFAULT 0,
    end_time BIGINT DEFAULT 0,
    enable TINYINT(1) DEFAULT 0,
    UNIQUE KEY uk_fs_id (fs_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE flash_sale_mch (
    id INT AUTO_INCREMENT PRIMARY KEY,
    info_id VARCHAR(64) NOT NULL,
    fs_id VARCHAR(64) DEFAULT '',
    mid VARCHAR(64) DEFAULT '',
    KEY idx_fs_id (fs_id),
    KEY idx_mid (mid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE flash_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id VARCHAR(64) NOT NULL,
    fs_id VARCHAR(64) NOT NULL,
    uid VARCHAR(64) NOT NULL,
    goods_info_id VARCHAR(64) DEFAULT '',
    flash_price DECIMAL(19,2) DEFAULT 0.00,
    qty INT DEFAULT 1,
    order_status TINYINT DEFAULT 0,
    create_time BIGINT DEFAULT 0,
    pay_time BIGINT DEFAULT 0,
    UNIQUE KEY uk_order_id (order_id),
    KEY idx_uid_fs (uid, fs_id),
    KEY idx_status_time (order_status, create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE active_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    active_id VARCHAR(64) NOT NULL,
    active_name VARCHAR(64) DEFAULT '',
    active_type INT DEFAULT 0,
    active_status INT DEFAULT 0,
    start_date BIGINT DEFAULT 0,
    end_date BIGINT DEFAULT 0,
    start_time BIGINT DEFAULT 0,
    end_time BIGINT DEFAULT 0,
    time_flag TINYINT(1) DEFAULT 0,
    enable TINYINT(1) DEFAULT 0,
    active_desc VARCHAR(256) DEFAULT '',
    limit_sum INT DEFAULT 0,
    pic VARCHAR(200) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE activity_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    activity_id VARCHAR(64) NOT NULL,
    activity_name VARCHAR(128) DEFAULT '',
    activity_status_type INT DEFAULT 0,
    activity_create_time BIGINT DEFAULT 0,
    activity_end_time BIGINT DEFAULT 0,
    activity_desc VARCHAR(512) DEFAULT '',
    share_title VARCHAR(128) DEFAULT '',
    share_letter VARCHAR(128) DEFAULT '',
    share_image VARCHAR(200) DEFAULT '',
    UNIQUE KEY uk_activity_id (activity_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE coupon_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    coupon_id VARCHAR(64) NOT NULL,
    coupon_name VARCHAR(64) DEFAULT '',
    coupon_type INT DEFAULT 0,
    par_value DECIMAL(19,2) DEFAULT 0.00,
    terms DECIMAL(19,2) DEFAULT 0.00,
    mid VARCHAR(64) DEFAULT '',
    category_id VARCHAR(64) DEFAULT '',
    start_time BIGINT DEFAULT 0,
    end_time BIGINT DEFAULT 0,
    total_num INT DEFAULT 0,
    received_num INT DEFAULT 0,
    valid TINYINT(1) DEFAULT 1,
    UNIQUE KEY uk_coupon_id (coupon_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE user_coupon (
    id INT AUTO_INCREMENT PRIMARY KEY,
    uid VARCHAR(64) NOT NULL,
    coupon_id VARCHAR(64) DEFAULT '',
    order_id VARCHAR(64) DEFAULT '',
    coupon_name VARCHAR(64) DEFAULT '',
    par_value DECIMAL(19,2) DEFAULT 0.00,
    coupon_type INT DEFAULT 0,
    coupon_status INT DEFAULT 0,
    start_time BIGINT DEFAULT 0,
    end_time BIGINT DEFAULT 0,
    use_time BIGINT DEFAULT 0,
    create_time BIGINT DEFAULT 0,
    mid VARCHAR(64) DEFAULT '',
    category_id VARCHAR(64) DEFAULT '',
    valid TINYINT(1) DEFAULT 1,
    KEY idx_uid (uid),
    KEY idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE prize_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    prize_id VARCHAR(64) NOT NULL,
    coupon_id VARCHAR(64) DEFAULT '',
    activity_id VARCHAR(64) DEFAULT '',
    prize_type INT DEFAULT 0,
    prize_name VARCHAR(128) DEFAULT '',
    prize_image VARCHAR(200) DEFAULT '',
    prize_numbers INT DEFAULT 0,
    write_offer_flag TINYINT(1) DEFAULT 0,
    probability DOUBLE DEFAULT 0.0,
    KEY idx_activity_id (activity_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE draw_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    draw_id VARCHAR(64) NOT NULL,
    draw_opportunity_id VARCHAR(64) NOT NULL,
    activity_id VARCHAR(64) NOT NULL,
    uid VARCHAR(64) NOT NULL,
    prize_id VARCHAR(64) DEFAULT '',
    prize_name VARCHAR(128) DEFAULT '',
    draw_status INT DEFAULT 0,
    draw_time BIGINT DEFAULT 0,
    write_offe_time BIGINT DEFAULT 0,
    write_offe_remark VARCHAR(128) DEFAULT '',
    KEY idx_uid (uid),
    KEY idx_activity (activity_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE draw_opportunity (
    id INT AUTO_INCREMENT PRIMARY KEY,
    draw_opportunity_id VARCHAR(64) NOT NULL,
    uid VARCHAR(64) NOT NULL,
    order_id VARCHAR(64) NOT NULL,
    activity_id VARCHAR(64) NOT NULL,
    draw_opportunity_status INT DEFAULT 0,
    KEY idx_uid (uid),
    KEY idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE falsedata_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    activity_id VARCHAR(64) NOT NULL,
    users_name VARCHAR(64) DEFAULT '',
    prizes_name VARCHAR(64) DEFAULT '',
    KEY idx_activity_id (activity_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE groupbuy_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    group_id VARCHAR(64) NOT NULL,
    store_id VARCHAR(64) DEFAULT '',
    goods_info_id VARCHAR(64) DEFAULT '',
    group_sum INT DEFAULT 0,
    group_lim INT DEFAULT 0,
    end_time BIGINT DEFAULT 0,
    group_type INT DEFAULT 0,
    group_status INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE rotation_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    info_id VARCHAR(64) NOT NULL,
    pic_url VARCHAR(200) DEFAULT '',
    link_url VARCHAR(200) DEFAULT '',
    sort INT DEFAULT 0,
    rotation_type INT DEFAULT 0,
    rotation_status INT DEFAULT 0,
    enable TINYINT(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
