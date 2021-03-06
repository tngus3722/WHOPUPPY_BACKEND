CREATE TABLE if NOT EXISTS whopuppy.chat_room(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    create_user_id BIGINT NOT NULL,
    article_id BIGINT NOT NULL,
    member_count INT NOT NULL,
    name VARCHAR(50) NOT NULL,
    is_deleted TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE if NOT EXISTS whopuppy.chat_room_member(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    chat_room_id BIGINT NOT NULL,
    message_id BIGINT NOT NULL DEFAULT 0,
    user_id BIGINT NOT NULL,
    is_create TINYINT(1) DEFAULT 0,
    is_admin TINYINT(1) DEFAULT 0,
    is_deleted TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE if NOT EXISTS whopuppy.chat_message(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    chat_room_id BIGINT NOT NULL,
    send_user_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    read_count INT NOT NULL,
    is_deleted TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;


