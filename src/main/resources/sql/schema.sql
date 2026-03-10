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
    PRIMARY KEY(account_id),
    CONSTRAINT uk_account_email UNIQUE (email) -- 'uk_account_email'라는 이름을 지어서 에러 찾기 편하게
);

CREATE TABLE IF NOT EXISTS members (
    member_id BIGINT AUTO_INCREMENT NOT NULL,
    cohort_no TINYINT NOT NULL CHECK cohort_no > 0,
    name VARCHAR(10) NOT NULL,
    role ENUM('CORE', 'MEMBER') DEFAULT 'MEMBER' NOT NULL,
    PRIMARY KEY (member_id),
    FOREIGN KEY (cohort_no) REFERENCES cohorts(cohort_no),
    CONSTRAINT uk_member_cohort_no_name UNIQUE (cohort_no, name)
);

-- 세션(Session) 테이블 생성
CREATE TABLE IF NOT EXISTS sessions (
                                        session_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        cohort_no TINYINT NOT NULL,
    -- ERD 명세대로 ENUM 적용
                                        type ENUM('GTL Weekly', 'GTL Monthly', 'Quarterly (온보딩)', 'Quarterly (수료식)', 'Quarterly') NOT NULL,
    title VARCHAR(255) NOT NULL,
    date DATE NOT NULL, -- 날짜만 저장 (시간 필요 시 DATETIME으로 협의 필요)
    description VARCHAR(255),
    FOREIGN KEY (cohort_no) REFERENCES cohorts(cohort_no)
    );

-- 출석(Attendance) 테이블 생성
CREATE TABLE IF NOT EXISTS attendances (
    -- 1. PK 및 자동 생성, NULL 허용 안 함
                                           attendance_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    -- 2. 외래키(FK), NULL 허용 안 함
                                           session_id BIGINT NOT NULL,
    -- 3. FK, NULL 허용 (게스트일 경우 NULL)
                                           member_id BIGINT,
    -- 4. VARCHAR(10), NULL 허용
                                           guest_name VARCHAR(10),
    -- 5. VARCHAR(255), NULL 허용
    comment VARCHAR(255),
    -- 6. ENUM 타입, 기본값 'ABSENT', NULL 허용 안 함
    status ENUM('PRESENT', 'ABSENT') NOT NULL DEFAULT 'ABSENT',
    -- 7. DATETIME 타입, NULL 허용 (출석 시점에 기록)
    attendance_time DATETIME,
    -- 외래키 연결 (참조 테이블 명칭은 프로젝트 상황에 맞게 조절)
    FOREIGN KEY (session_id) REFERENCES session(session_id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES account(account_id) ON DELETE CASCADE,
    -- [제약 조건 1] XOR 체크: 멤버 ID가 없으면 게스트 이름이 있어야 하고, 반대도 마찬가지
    CONSTRAINT chk_member_xor_guest CHECK (
(member_id IS NULL AND guest_name IS NOT NULL) OR
(member_id IS NOT NULL AND guest_name IS NULL)
    ),
    -- [제약 조건 2] 한 세션에 동일 멤버가 중복 출석 데이터 생성 방지
    -- MySQL에서는 member_id가 NULL인 경우(게스트) 여러 건의 중복을 허용하므로 의도대로 작동합니다.
    CONSTRAINT uk_session_member UNIQUE (session_id, member_id)
    );