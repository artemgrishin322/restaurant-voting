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

INSERT INTO MENU_ITEM (NAME, SERVE_DATE, PRICE, RESTAURANT_ID)
VALUES ('Steak with potato', CURRENT_DATE, 1000, 1),
       ('Mushroom soup', CURRENT_DATE, 750, 1),
       ('Tiramisu', CURRENT_DATE, 620, 1),
       ('California', CURRENT_DATE, 700, 2),
       ('Unai Maki', CURRENT_DATE, 850, 2),
       ('Philadelphia', CURRENT_DATE, 1200, 2),
       ('Previous menu item', DATE '2019-02-10', 700, 1);

INSERT INTO VOTE (USER_ID, RESTAURANT_ID)
VALUES (2, 1);

INSERT INTO VOTE (USER_ID, RESTAURANT_ID, VOTE_DATE)
VALUES (1, 2, DATE '2021-12-31'),
       (1, 1, DATE '2021-10-25'),
       (1, 2, DATE '2021-08-15'),
       (3, 1, DATE '2020-02-13');