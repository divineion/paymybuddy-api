-- -----------------------------------------------------
-- Schema paymybuddy
-- -----------------------------------------------------

DROP SCHEMA IF EXISTS paymybuddy CASCADE;

CREATE SCHEMA IF NOT EXISTS paymybuddy;

SET search_path TO paymybuddy, public;

-- -----------------------------------------------------
-- Table paymybuddy.app_user
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS paymybuddy.app_user (
    id SERIAL PRIMARY KEY,  -- SERIAL --> AUTO-INCREMENT
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    deleted_at TIMESTAMP NULL DEFAULT NULL,
    password VARCHAR(255) NOT NULL,
    balance DECIMAL(10,2) NOT NULL CHECK (balance >= 0),  -- UNSIGNED
    active_email VARCHAR(100) GENERATED ALWAYS AS (CASE WHEN deleted_at IS NULL THEN email ELSE NULL END) STORED,
    CONSTRAINT active_email_UNIQUE UNIQUE (active_email) 
);

-- -----------------------------------------------------
-- Table paymybuddy.transfer
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS paymybuddy.transfer (
    id SERIAL PRIMARY KEY,  -- Utilisation de SERIAL pour auto-incrémentation dans PostgreSQL
    sender INT NOT NULL,
    receiver INT NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount DECIMAL(10,2) NOT NULL CHECK (amount >= 0),  -- UNISGNED
    fees DECIMAL(10,3) NOT NULL DEFAULT 0.005,  
    total_amount DECIMAL(10,2) GENERATED ALWAYS AS (ROUND(amount * (1 + fees), 2)) STORED,  
    date TIMESTAMPTZ NOT NULL DEFAULT date_trunc('second', CURRENT_TIMESTAMP),  -- TIMESTAMP = SQL DATETIME ; TIMESTAMPTZ = SQL TIMESTAMP
    CONSTRAINT fk_transfer_sender FOREIGN KEY (sender) REFERENCES paymybuddy.app_user(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_transfer_receiver FOREIGN KEY (receiver) REFERENCES paymybuddy.app_user(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Table paymybuddy.user_beneficiary
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS paymybuddy.user_beneficiary (
    user_id INT NOT NULL,
    beneficiary_id INT NOT NULL,
    PRIMARY KEY (user_id, beneficiary_id),
    CONSTRAINT fk_userbeneficiary_user_id FOREIGN KEY (user_id) REFERENCES paymybuddy.app_user(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_userbeneficiary_beneficiary_id FOREIGN KEY (beneficiary_id) REFERENCES paymybuddy.app_user(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Data for table paymybuddy.app_user
-- -----------------------------------------------------
INSERT INTO paymybuddy.app_user (username, email, deleted_at, password, balance)
SELECT 'Georgia', 'georgia@email.com', NULL::timestamp, '$2a$10$Lc2JhT8glUB8.hfGoRYGVuuDnL7RM8XXSLAQTYlmv5hlNkkE14BQu', 100.00
WHERE NOT EXISTS (
	SELECT 1 FROM paymybuddy.app_user WHERE active_email = 'georgia@email.com'
);

INSERT INTO paymybuddy.app_user (username, email, deleted_at, password, balance)
SELECT 'Tanka', 'tanka@email.com', NULL::timestamp, '$2a$10$tOwIV5x8bXF/Xh5tkHkKmO153X8bSGkibFU21KK6oshF1R9mVS6KO', 100.00
WHERE NOT EXISTS (
	SELECT 1 FROM paymybuddy.app_user WHERE active_email = 'tanka@email.com'
); 

INSERT INTO paymybuddy.app_user (username, email, deleted_at, password, balance)
SELECT 'Bagheera', 'bagheera@email.com', NULL::timestamp, '$2a$10$1CLQi6XqmrfmafzzfeO/jOhGfnY6F4vIk5lbyQh7aSKN7VS.0mIdi', 100.00
WHERE NOT EXISTS (
	SELECT 1 FROM paymybuddy.app_user WHERE active_email = 'bagheera@email.com' 
);

INSERT INTO paymybuddy.app_user (username, email, deleted_at, password, balance)
SELECT 'Mania', 'mania@email.com', NULL::timestamp, '$2a$10$OrZrQGi2o7nb1eRzZfgWFOdm9LksYYirAfjb3Agdf9if30eNWhEom', 100.00
WHERE NOT EXISTS (
	SELECT 1 FROM paymybuddy.app_user WHERE active_email = 'mania@email.com' 
);

INSERT INTO paymybuddy.app_user (username, email, deleted_at, password, balance)
SELECT 'Jeena', 'jeena@email.com', NULL, '$2a$10$E3MsEXGQJfKhtRwBWCQjoeOHrGXH2AqN15RhOjQu1GirCdMcNRrTG', 100.00
WHERE NOT EXISTS (
	SELECT 1 FROM paymybuddy.app_user WHERE active_email = 'jeena@email.com' 
);

-- -----------------------------------------------------
-- Data for table paymybuddy.user_beneficiary
-- -----------------------------------------------------
INSERT INTO paymybuddy.user_beneficiary (user_id, beneficiary_id)
VALUES (1, 2);

INSERT INTO paymybuddy.user_beneficiary (user_id, beneficiary_id) 
VALUES (1, 3);

INSERT INTO paymybuddy.user_beneficiary (user_id, beneficiary_id) 
VALUES (3, 4);

-- -----------------------------------------------------
-- Insert data for table paymybuddy.transfer
-- -----------------------------------------------------
BEGIN;
INSERT INTO paymybuddy.transfer (sender, receiver, description, amount)
SELECT 1, 2, 'entrée parc aquatique', 18;

-- -----------------------------------------------------
-- Update app_user (sender) balance after transfer
-- -----------------------------------------------------
UPDATE paymybuddy.app_user u
SET balance = balance - 18 * (1 + 0.005)
WHERE u.id = 1;

-- -----------------------------------------------------
-- Update app_user (beneficiary) balance after transfer
-- -----------------------------------------------------
UPDATE paymybuddy.app_user u
SET balance = balance + 18
WHERE u.id = 2;

