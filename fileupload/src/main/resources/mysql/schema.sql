CREATE TABLE IF NOT EXISTS FileInfo (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    article_id BIGINT NOT NULL,
    original_name VARCHAR(255) NOT NULL,
    uuid_name CHAR(36) NOT NULL,
    file_type ENUM('IMAGE', 'VIDEO', 'OTHER') NOT NULL,
    file_status ENUM('PENDING', 'COMPLETED', 'DELETED') NOT NULL,
    url VARCHAR(255) NOT NULL,
    created_at DATETIME,
    updated_at DATETIME
);