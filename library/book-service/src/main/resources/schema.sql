-- src/test/resources/schema.sql


CREATE TABLE books (
                       id               SERIAL PRIMARY KEY,
                       title            VARCHAR(255) NOT NULL,
                       author           VARCHAR(255) NOT NULL,
                       isbn             VARCHAR(50)  NOT NULL UNIQUE,
                       genre            VARCHAR(100),
                       total_copies     INT          NOT NULL,
                       available_copies INT          NOT NULL
);
