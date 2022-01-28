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

INSERT INTO MENU_ITEM (NAME, PRICE, RESTAURANT_ID)
VALUES ('Steak with potato', 1000, 1),
       ('Mushroom soup', 750, 1),
       ('Tiramisu', 620, 1),
       ('California', 700, 2),
       ('Unai Maki', 850, 2),
       ('Philadelphia', 1200, 2);

INSERT INTO MENU_ITEM (NAME, PRICE, SERVE_DATE, RESTAURANT_ID)
VALUES ('Previous menu item', 700, DATE '2019-02-10', 1);

INSERT INTO VOTE (USER_ID, RESTAURANT_ID)
VALUES (2, 1);

INSERT INTO VOTE (USER_ID, RESTAURANT_ID, REGISTERED)
VALUES (1, 2, DATE '2021-12-31'),
       (3, 1, DATE '2020-02-13');
