-- x-form 表单系统数据库初始化
CREATE DATABASE IF NOT EXISTS x_form DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE x_form;

-- 表单定义表
DROP TABLE IF EXISTS form_definition;
CREATE TABLE form_definition (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    form_key VARCHAR(64) NOT NULL UNIQUE COMMENT '表单唯一标识',
    title VARCHAR(255) NOT NULL COMMENT '表单标题',
    description TEXT COMMENT '表单描述',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-草稿 2-已发布 3-已关闭',
    share_link VARCHAR(512) COMMENT '分享链接',
    created_by VARCHAR(64) COMMENT '创建人',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_form_key (form_key),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表单定义表';

-- 表单字段表
DROP TABLE IF EXISTS form_field;
CREATE TABLE form_field (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    form_id BIGINT NOT NULL COMMENT '表单ID',
    field_key VARCHAR(64) NOT NULL COMMENT '字段标识',
    field_label VARCHAR(128) NOT NULL COMMENT '字段标签',
    field_type VARCHAR(32) NOT NULL COMMENT '字段类型：text/textarea/number/select/radio/checkbox/date/email',
    required TINYINT NOT NULL DEFAULT 0 COMMENT '是否必填：0-否 1-是',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序序号',
    options_json TEXT COMMENT '选项JSON（select/radio/checkbox类型使用）',
    placeholder VARCHAR(255) COMMENT '占位提示',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_form_id (form_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表单字段表';

-- 表单提交数据表
DROP TABLE IF EXISTS form_submission;
CREATE TABLE form_submission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    form_id BIGINT NOT NULL COMMENT '表单ID',
    form_key VARCHAR(64) NOT NULL COMMENT '表单标识',
    submit_data JSON NOT NULL COMMENT '提交数据JSON',
    submitter_name VARCHAR(64) COMMENT '提交人姓名',
    submitter_phone VARCHAR(20) COMMENT '提交人手机号',
    ip_address VARCHAR(64) COMMENT '提交IP',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
    INDEX idx_form_id (form_id),
    INDEX idx_form_key (form_key),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='表单提交数据表';
