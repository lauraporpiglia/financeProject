CREATE TABLE TRANSACTIONS
(
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(255),
    amount      INT,
    description VARCHAR(10),
    type        VARCHAR(15),
    date        VARCHAR(100)
);

