CREATE TABLE IF NOT EXISTS cohorts (
    cohort_no TINYINT NOT NULL CHECK cohort_no > 0,
    organizer VARCHAR(10) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    PRIMARY KEY (cohort_no)
);

CREATE TABLE IF NOT EXISTS account(
    account_id BIGINT AUTO_INCREMENT NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('CORE','MEMBER') DEFAULT 'MEMBER' NOT NULL,
    PRIMARY KEY(account_id),
    CONSTRAINT uk_account_email UNIQUE (email) -- 'uk_account_email'라는 이름을 지어서 에러 찾기 편하게
);