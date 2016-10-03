CREATE TABLE IF NOT EXISTS stepcounter (
    started_date TEXT PRIMARY KEY,
    steps INTEGER DEFAULT 0,
    distance DOUBLE DEFAULT 0,
    updated_datetime TEXT NULL
);

CREATE TABLE IF NOT EXISTS footprint (
    started_date TEXT PRIMARY KEY,
    latitude DOUBLE DEFAULT 0,
    longitude DOUBLE DEFAULT 0,
    address TEXT NULL,
    updated_datetime TEXT NULL
);
