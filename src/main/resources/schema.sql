CREATE TABLE IF NOT EXISTS TRANSACTION
(
    id          VARCHAR(40) PRIMARY KEY,
    name        VARCHAR(255),
    amount      INT,
    description VARCHAR(10),
    type        VARCHAR(15),
    date        VARCHAR(100)
);

