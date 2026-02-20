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
    role ENUM('CORE','MEMBER', 'ADMIN') DEFAULT 'MEMBER' NOT NULL,
    cohort_no TINYINT, --
    PRIMARY KEY(account_id),
    CONSTRAINT uk_account_email UNIQUE (email) -- 'uk_account_email'라는 이름을 지어서 에러 찾기 편하게
);

-- 세션(Session) 테이블 생성
CREATE TABLE IF NOT EXISTS session (
                                       session_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       title VARCHAR(100) NOT NULL,
    date DATETIME NOT NULL,
    type VARCHAR(50) NOT NULL,
    description TEXT,
    cohort_no TINYINT NOT NULL
    );

-- 출석(Attendance) 테이블 생성
CREATE TABLE IF NOT EXISTS attendance (
                                          attendance_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          session_id BIGINT NOT NULL,
                                          account_id BIGINT,
                                          status VARCHAR(20) NOT NULL,
    attended_at DATETIME,
    guest_name VARCHAR(50)
    );