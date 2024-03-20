CREATE DATABASE task_service;

CREATE TABLE "user"
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100)       NOT NULL,
    role     VARCHAR(50)        NOT NULL
);

CREATE TABLE topic
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE programming_language
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE task
(
    id        SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    condition TEXT NOT NULL,
    topic     INT  NOT NULL REFERENCES topic (id),
    difficulty varchar(50)
);

CREATE TABLE task_language_accessibility
(
    task INT NOT NULL REFERENCES task (id),
    language INT NOT NULL REFERENCES programming_language (id),
    PRIMARY KEY(task, language)
);

CREATE TABLE solution (
    id SERIAL PRIMARY KEY,
    task INT NOT NULL REFERENCES task(id),
    "user" INT NOT NULL REFERENCES "user"(id),
    submission_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    execution_time INT,
    code TEXT NOT NULL
);

CREATE TABLE test_case (
    id SERIAL PRIMARY KEY,
    task INT NOT NULL REFERENCES task(id),
    input_data VARCHAR(100) NOT NULL,
    output_data VARCHAR(100) NOT NULL
);

CREATE TABLE comment (
    id SERIAL PRIMARY KEY,
    task INT NOT NULL REFERENCES task(id),
    "user" INT NOT NULL REFERENCES "user"(id),
    comment_text TEXT NOT NULL,
    post_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE comment_likes (
    comment INT NOT NULL REFERENCES comment(id),
    "user" INT NOT NULL REFERENCES "user"(id),
    PRIMARY KEY (comment, "user")
);

insert into "user" values (default,
                           'admin',
                           '$2a$10$nILkGNvzDqknzAJArq7XO.6au0y64qKi5HLZTHPR1CEDbA8Jr2vXq',
                           'ROLE_ADMIN');