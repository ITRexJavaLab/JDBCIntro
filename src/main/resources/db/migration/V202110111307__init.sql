CREATE TABLE IF NOT EXISTS user (
    id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(20),
    email varchar(50),
    date_of_birth timestamp,
    CONSTRAINT UNIQUE_EMAIL UNIQUE (email)
);

INSERT INTO user(name, email, date_of_birth) VALUES ('Dmitry', 'some@email.com', '1970-01-01');

COMMIT;