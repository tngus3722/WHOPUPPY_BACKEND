CREATE TABLE if NOT EXISTS whopuppy.animal_comment(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    article_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content VARCHAR(200) NOT NULL,
    is_deleted TINYINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL DEFAULT NULL
)default character set utf8 collate utf8_general_ci;

CREATE TABLE if NOT EXISTS whopuppy.animal_list(
    idx INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    sexCd VARCHAR(45) NOT NULL,
    kindCd VARCHAR(45) NOT NULL,
    noticeNo VARCHAR(45) NOT NULL,
    careAddr VARCHAR(200) NOT NULL,
    processState VARCHAR(45) NOT NULL,
    noticeSdt VARCHAR(45) NOT NULL,
    weight VARCHAR(45) NOT NULL,
    chargeNm VARCHAR(45) NOT NULL,
    desertionNo VARCHAR(45) NOT NULL UNIQUE,
    careNm VARCHAR(45) NOT NULL,
    careTel VARCHAR(45) NOT NULL,
    happenPlace VARCHAR(45) NOT NULL,
    officetel VARCHAR(45) NOT NULL,
    orgNm VARCHAR(45) NOT NULL,
    filename VARCHAR(100) NOT NULL,
    popfile VARCHAR(100) NOT NULL,
    noticeEdt VARCHAR(45) NOT NULL,
    neuterYn VARCHAR(45) NOT NULL,
    specialMark VARCHAR(100) NOT NULL,
    colorCd VARCHAR(45) NOT NULL,
    happenDt VARCHAR(45) NOT NULL,
    age VARCHAR(45) NOT NULL
)default character set utf8 collate utf8_general_ci;