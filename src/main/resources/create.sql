DROP TABLE IF EXISTS user_accounts CASCADE;
DROP TABLE IF EXISTS topics CASCADE;
DROP TABLE IF EXISTS posts;

CREATE TABLE user_accounts
(
    id       BIGSERIAL PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(255) NOT NULL
);


CREATE TABLE topics
(
    id          BIGSERIAL PRIMARY KEY,
    topicName   VARCHAR(255)                         NOT NULL UNIQUE,
    createdAt   TIMESTAMP,
    description TEXT,
    creator_id  BIGINT REFERENCES user_accounts (id) NOT NULL,
    isArchived  BOOLEAN,
    FOREIGN KEY (creator_id) REFERENCES user_accounts (id) ON DELETE CASCADE
);


CREATE TABLE posts
(
    id         BIGSERIAL PRIMARY KEY,
    postName   VARCHAR(255)                                           NOT NULL UNIQUE,
    createdAt  TIMESTAMP,
    content    TEXT                                                   NOT NULL,
    creator_id BIGINT REFERENCES user_accounts (id) ON DELETE CASCADE NOT NULL,
    topic_id   BIGINT REFERENCES topics (id) ON DELETE CASCADE        NOT NULL,
    postType   VARCHAR(255)                                           NOT NULL
);



