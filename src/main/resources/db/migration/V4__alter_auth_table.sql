CREATE TABLE if NOT EXISTS whopuppy.s3_url(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    image_url VARCHAR(255) NOT NULL,
    is_posted TINYINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
)default character set utf8 collate utf8_general_ci;

CREATE TABLE if NOT EXISTS whopuppy.region(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    region VARCHAR(30) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
)default character set utf8 collate utf8_general_ci;

