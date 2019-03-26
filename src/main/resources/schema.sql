DROP SCHEMA public CASCADE;

CREATE SEQUENCE seq_expense START WITH 1;
CREATE SEQUENCE seq_user START WITH 1;
CREATE SEQUENCE seq_category START WITH 1;
CREATE SEQUENCE seq_role START WITH 1;
CREATE SEQUENCE seq_user_role START WITH 1;



CREATE TABLE user (
  user_id BIGINT NOT NULL PRIMARY KEY ,
  username VARCHAR(20) NOT NULL UNIQUE ,
  password VARCHAR(100) NOT NULL
);


CREATE TABLE category (
  category_id BIGINT NOT NULL PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  user_id BIGINT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE no action ON UPDATE CASCADE
);

CREATE TABLE expense (
  expense_id BIGINT NOT NULL PRIMARY KEY,
  category_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  insertion_time DATETIME DEFAULT LOCALTIMESTAMP(0),
  comment VARCHAR(255) NULL,
  amount DOUBLE NOT NULL,
  FOREIGN KEY (category_id) REFERENCES category(category_id) ON DELETE NO ACTION ON UPDATE CASCADE ,
  FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE NO ACTION ON UPDATE CASCADE
);



