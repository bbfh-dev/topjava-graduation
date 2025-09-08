DELETE FROM vote;
DELETE FROM dish;
DELETE FROM menu;
DELETE FROM restaurant;

INSERT INTO restaurant (id, name) VALUES (1, 'Example Restaurant #1');
INSERT INTO restaurant (id, name) VALUES (2, 'Пример Ресторана №2');
INSERT INTO restaurant (id, name) VALUES (3, 'Example Restaurant #3');
ALTER TABLE restaurant ALTER COLUMN id RESTART WITH 100;

INSERT INTO menu (id, relevancy_date, restaurant_id) VALUES (1, CURRENT_DATE - 1, 1);
INSERT INTO menu (id, relevancy_date, restaurant_id) VALUES (2, CURRENT_DATE, 1);
ALTER TABLE menu ALTER COLUMN id RESTART WITH 100;

INSERT INTO dish (id, name, price, menu_id) VALUES (1, 'Example Dish #1 of Menu #1', 1050, 1);
INSERT INTO dish (id, name, price, menu_id) VALUES (2, 'Example Dish #2 of Menu #1', 600, 1);
INSERT INTO dish (id, name, price, menu_id) VALUES (3, 'Example Dish #1 of Menu #2', 1600, 2);
ALTER TABLE dish ALTER COLUMN id RESTART WITH 100;

INSERT INTO vote (id, vote_date, user_id, menu_id) VALUES (1, '2025-01-01', 1, 1);
INSERT INTO vote (id, vote_date, user_id, menu_id) VALUES (2, '2025-01-01', 2, 1);
ALTER TABLE vote ALTER COLUMN id RESTART WITH 100;
