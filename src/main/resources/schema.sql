CREATE TABLE IF NOT EXISTS alchemists (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    experience_level INT NOT NULL,
    specialty VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS potions (
    code VARCHAR(3) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    power_level INT NOT NULL,
    description VARCHAR(255),
    alchemist_id VARCHAR(255),
    FOREIGN KEY (alchemist_id) REFERENCES alchemists(id)
);