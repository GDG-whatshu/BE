CREATE TABLE IF NOT EXISTS cohorts (
    cohort_no TINYINT NOT NULL CHECK cohort_no > 0,
    organizer VARCHAR(10) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    PRIMARY KEY (cohort_no)
);