CREATE TABLE groups (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE students (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          group_id BIGINT,
                          FOREIGN KEY (group_id) REFERENCES groups(id)
);