-- テーブル名: roles
CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- テーブル名: users
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    deleted_at DATETIME,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- テーブル名: files
CREATE TABLE files (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    deleted_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- テーブル名: ideas
CREATE TABLE ideas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    file_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    pos_x INT NOT NULL DEFAULT 0,
    pos_y INT NOT NULL DEFAULT 0,
    node_type VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    deleted_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (file_id) REFERENCES files(id)
);

-- テーブル名: tags
CREATE TABLE tags (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    deleted_at DATETIME
);

-- テーブル名: idea_tags (中間テーブル)
CREATE TABLE idea_tags (
    idea_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (idea_id, tag_id),
    FOREIGN KEY (idea_id) REFERENCES ideas(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
);

-- テーブル名: idea_connections (中間テーブル)
CREATE TABLE idea_connections (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    from_idea_id BIGINT NOT NULL,
    to_idea_id BIGINT NOT NULL,
    FOREIGN KEY (from_idea_id) REFERENCES ideas(id) ON DELETE CASCADE,
    FOREIGN KEY (to_idea_id) REFERENCES ideas(id) ON DELETE CASCADE
);

-- 初期データの挿入
INSERT INTO roles (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');