CREATE SCHEMA IF NOT EXISTS whopuppy;

CREATE TABLE if NOT EXISTS whopuppy.user(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account VARCHAR(50) NOT NULL UNIQUE,
    password TEXT NOT NULL,
    nickname VARCHAR(50) NOT NULL UNIQUE,
    phone_number VARCHAR(30) NOT NULL  UNIQUE,
    salt VARCHAR(255) DEFAULT 0,
    profile_image_url VARCHAR(255),
    is_deleted TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

create table if NOT EXISTS whopuppy.auth(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account VARCHAR(50) NOT NULL,
    phone_number VARCHAR(30) NOT NULL,
    flag INT NOT NULL,
    is_authed TINYINT DEFAULT 0,
    secret VARCHAR(8) NOT NULL,
    ip VARCHAR(20) NOT NULL,
    is_deleted tinyint default 0,
    expired_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)default character set utf8 collate utf8_general_ci;

CREATE TABLE if NOT EXISTS whopuppy.admin(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    admin_authority_id BIGINT,
    admin_role_id BIGINT NOT NULL  DEFAULT 3,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)default character set utf8 collate utf8_general_ci;

CREATE TABLE if NOT EXISTS whopuppy.admin_role(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role VARCHAR(30) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)default character set utf8 collate utf8_general_ci;


CREATE TABLE if NOT EXISTS whopuppy.admin_authority(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    authority VARCHAR(20) NOT NULL
)default character set utf8 collate utf8_general_ci;

CREATE TABLE if NOT EXISTS whopuppy.board(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    board VARCHAR(20) NOT NULL UNIQUE
)default character set utf8 collate utf8_general_ci;

CREATE TABLE if NOT EXISTS whopuppy.article(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    board_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    title VARCHAR(50),
    content VARCHAR(500),
    region VARCHAR(100),
    thumbnail VARCHAR(255),
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)default character set utf8 collate utf8_general_ci;

CREATE TABLE if NOT EXISTS whopuppy.article_image(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    article_id BIGINT,
    image_url VARCHAR(255) NOT NULL UNIQUE,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
)default character set utf8 collate utf8_general_ci;

CREATE TABLE if NOT EXISTS  whopuppy.article_comment(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    article_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content VARCHAR(200) NOT NULL,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)default character set utf8 collate utf8_general_ci;

CREATE TABLE if NOT EXISTS whopuppy.report_type(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(20) NOT NULL UNIQUE
)default character set utf8 collate utf8_general_ci;

CREATE TABLE if NOT EXISTS  whopuppy.report(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    target_id BIGINT NOT NULL,
    report_type_id BIGINT NOT NULL,
    report_division_id TINYINT NOT NULL,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)default character set utf8 collate utf8_general_ci;

CREATE TABLE if NOT EXISTS whopuppy.snack(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(50),
    content VARCHAR(5000),
    thumbnail VARCHAR(255),
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)default character set utf8 collate utf8_general_ci;

CREATE TABLE if NOT EXISTS  whopuppy.snack_comment(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    snack_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content VARCHAR(200) NOT NULL,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)default character set utf8 collate utf8_general_ci;


CREATE TABLE if NOT EXISTS whopuppy.report_division(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    division VARCHAR(30) NOT NULL UNIQUE
)default character set utf8 collate utf8_general_ci;

CREATE TABLE if NOT EXISTS whopuppy.snack_image(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    snack_id BIGINT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
)default character set utf8 collate utf8_general_ci;

