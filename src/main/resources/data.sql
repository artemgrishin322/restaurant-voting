INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('User_1', 'user1@gmail.com', '{noop}user_1_password'),
       ('User_2', 'user2@yandex.ru', '{noop}user_2_password'),
       ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO USER_ROLES (USER_ID, ROLE)
VALUES (1, 'USER'),
       (2, 'USER'),
       (3, 'ADMIN'),
       (3, 'USER');

INSERT INTO RESTAURANT (NAME, ADDRESS, DESCRIPTION)
VALUES ('Mon blah', 'Moscow, Temiryazevskaya st, p. 64', 'French restaurant'),
       ('Yanomamo', 'New York, Broadway, p. 865', 'Sushi bar');

INSERT INTO DISH (NAME, PRICE, RESTAURANT_ID)
VALUES ('Steak with potato', 1000, 1),
       ('Mushroom soup', 750, 1),
       ('Tiramisu', 620, 1),
       ('California', 700, 2),
       ('Unai Maki', 850, 2),
       ('Philadelphia', 1200, 2);

INSERT INTO DISH (NAME, PRICE, REGISTERED, RESTAURANT_ID)
VALUES ('Previous dish', 700, parsedatetime('10-02-2019', 'dd-MM-yyyy'), 1);

INSERT INTO VOTE (USER_ID, RESTAURANT_ID)
VALUES (2, 1);
