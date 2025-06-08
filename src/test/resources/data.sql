---- -----------------------------------------------------
---- Data for table role
---- -----------------------------------------------------
INSERT INTO role (id, name)
VALUES (1, 'ROLE_USER');

INSERT INTO role (id, name)
VALUES (2, 'ROLE_ADMIN');

-- -----------------------------------------------------
-- Data for table app_user
-- -----------------------------------------------------
INSERT INTO app_user (username, email, deleted_at, password, balance, role) 
VALUES ('Georgia', 'georgia@email.com', NULL, '$2a$10$TJrrPqyC3sf42/uJY0cm1eRUrcx6vqC4HzHF31/kovkD1xXrcNAWu', 100.00, 1);

INSERT INTO app_user (username, email, deleted_at, password, balance, role)
VALUES ('Tanka', 'tanka@email.com', NULL, '$2a$10$/zKuR7vgF/WvDrjwFLFd5Orc0w6f1vukH.J4vnimvcwOPE0bUBI26', 100.00, 1);

INSERT INTO app_user (username, email, deleted_at, password, balance, role)
VALUES ('Bagheera', 'bagheera@email.com', NULL, '$2a$10$pyJVtTvaeRLpmpkAPy.Yn.BNNVEyqeMkLRbQSRvrtLM3MqI3uuMnq', 100.00, 1); 

INSERT INTO app_user (username, email, deleted_at, password, balance, role)
VALUES ('Mania', 'mania@email.com', NULL, '$2a$10$wkIRA/tTzGBF4Y2NN1jZkeoLxL.he5xKyhoqjNrv1areYQMBANSnq', 100.00, 1);

INSERT INTO app_user (username, email, deleted_at, password, balance, role)
VALUES ('Jeena', 'jeena@email.com', NULL, '$2a$10$2wTOCwSQuMvQ/uThhhP5cOwmQ2NUMBScx2SEqnh0KPj8Nzobjd9y.', 100.00, 1);

INSERT INTO app_user (username, email, deleted_at, password, balance, role)
VALUES ('admin', 'admin@email.com', NULL, '$2a$10$YrbnBHRI9YS7MTO98V9p2.g1.QyWfJOe3jpsrJI2varMrNoJxF0/a', 0, 2);

-- -----------------------------------------------------
-- Data for table user_beneficiary
-- -----------------------------------------------------
INSERT INTO user_beneficiary (user_id, beneficiary_id)
VALUES (1, 2);

-- -----------------------------------------------------
-- Data for table transfer
-- -----------------------------------------------------
INSERT INTO transfer (sender, receiver, description, amount, date) 
VALUES (1, 2, 'entrée parc aquatique', 18, NOW());

-- -----------------------------------------------------
-- Update table app_user
-- -----------------------------------------------------
UPDATE app_user
SET balance = balance - 18 * (1 + 0.005)
WHERE id = 1;

UPDATE app_user 
SET balance = balance + 18
WHERE id = 2;