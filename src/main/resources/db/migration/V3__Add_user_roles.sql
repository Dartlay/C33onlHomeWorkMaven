CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE,
    description TEXT
);

INSERT INTO roles (name, description) VALUES
('user', 'Обычный пользователь'),
('moderator', 'Модератор контента'),
('admin', 'Администратор системы')
ON CONFLICT (name) DO NOTHING;

ALTER TABLE users
ALTER COLUMN role TYPE INTEGER USING (
    CASE role
        WHEN 'user' THEN 1
        WHEN 'moderator' THEN 2
        WHEN 'admin' THEN 3
        ELSE 1
    END
);

ALTER TABLE users
ADD CONSTRAINT fk_user_role
FOREIGN KEY (role) REFERENCES roles(id);


CREATE TABLE IF NOT EXISTS permissions (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
);


CREATE TABLE IF NOT EXISTS role_permissions (
    role_id INTEGER REFERENCES roles(id) ON DELETE CASCADE,
    permission_id INTEGER REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);