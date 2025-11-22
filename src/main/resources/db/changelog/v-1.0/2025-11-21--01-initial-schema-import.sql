-- liquibase formatted sql

--changeset Maltsev:create-types
CREATE TYPE experience_level AS ENUM ('trainer', 'junior', 'middle', 'senior', 'lead');
CREATE TYPE age_experience AS ENUM ('no_experience', 'min_experience', 'middle_experience', 'max_experience');
CREATE TYPE work_schedule AS ENUM ('full_time', 'part_time', 'flexible');
CREATE TYPE work_format AS ENUM ('office', 'remote', 'hybrid');


--changeset Maltsev:create-programmer-table
CREATE TABLE programmers
(
    id           SERIAL PRIMARY KEY,
    first_name   VARCHAR(40) NOT NULL,
    last_name    VARCHAR(40) NOT NULL,
    surname      VARCHAR(40) NOT NULL,
    age          INTEGER     NOT NULL CHECK (age >= 16),
    technologies TEXT        NOT NULL,
    experience   VARCHAR(40) NOT NULL,
    about_myself TEXT,
    contacts     TEXT,
    telegram     TEXT        NOT NULL,
    chat_id      TEXT
);

--changeset Maltsev:create-vacancy-table
CREATE TABLE vacancies
(
    id               SERIAL PRIMARY KEY,
    name             TEXT             NOT NULL,
    technologies     TEXT             NOT NULL,
    experience       age_experience   NOT NULL,
    about_company    TEXT             NOT NULL,
    contacts         TEXT,
    level            experience_level NOT NULL,
    telegram         TEXT             NOT NUll,
    salary           NUMERIC(10, 2)   NOT NULL,
    schedule         work_schedule    NOT NULL,
    work_format      work_format      NOT NULL,
    publication_date DATE             NOT NULL,
    city             VARCHAR(100)     NOT NULL,
    chat_id          TEXT
);

--changeset Maltsev:create-application-table
CREATE TABLE application
(
    id               SERIAL PRIMARY KEY,
    programmer_id    INTEGER NOT NULL REFERENCES programmers (id) ON DELETE CASCADE,
    vacancy_id       INTEGER NOT NULL REFERENCES vacancies (id) ON DELETE CASCADE,
    application_date DATE        DEFAULT CURRENT_DATE,
    status           VARCHAR(20) DEFAULT 'pending' CHECK (status IN ('pending', 'accepted', 'rejected', 'viewed')),
    UNIQUE (programmer_id, vacancy_id)
);