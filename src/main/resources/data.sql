INSERT INTO category (id, name, created_at) VALUES
  (1, 'Смартфоны', CURRENT_TIMESTAMP),
  (2, 'Аудиотехника', CURRENT_TIMESTAMP),
  (3, 'Портативные колонки', CURRENT_TIMESTAMP),
  (4, 'Наушники', CURRENT_TIMESTAMP),
  (5, 'Сопутствующие товары', CURRENT_TIMESTAMP),
  (6, 'Apple', CURRENT_TIMESTAMP),
  (7, 'Samsung', CURRENT_TIMESTAMP),
  (8, 'Чехлы', CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;
  INSERT INTO category_parent (child_id, parent_id) VALUES
    (3, 2),
    (4, 2),
    (4, 5),
    (5, 1),
    (6, 1),
    (7, 1),
    (8, 5)
ON CONFLICT DO NOTHING;
INSERT INTO product (id, name, description, image_url, base_price, discount_percent, stock_quantity, created_at) VALUES
  (1, 'Смартфон Apple iPhone 13', 'Система двух камер 12 Мп: широкоугольная и сверхширокоугольная · Широкоугольная: диафрагма ƒ/1.6 · Сверхширокоугольная: диафрагма ƒ/2.4 и угол обзора 120°.', NULL, 80999.00, NULL, 17, CURRENT_TIMESTAMP),
  (2, 'Наушники Apple AirPods Pro', 'внутриканальные беспроводные наушники с активным шумоподавлением и режимом прозрачности, которые обеспечивают высокое качество звука благодаря адаптивному эквалайзеру и чипу Apple H2', NULL, 20000.00, 10, 92, CURRENT_TIMESTAMP),
  (3, 'Умная колонка Яндекс Станция', 'умная колонка с голосовым помощником Алисой, которая выполняет множество задач: проигрывает музыку, отвечает на вопросы, управляет устройствами умного дома, рассказывает сказки и играет в игры', NULL, 14500.00, NULL, 73, CURRENT_TIMESTAMP),
  (4, 'Смартфон Apple iPhone 14', 'iPhone 14 — это смартфон с 6,1-дюймовым OLED-дисплеем Super Retina XDR, оснащенный процессором A15 Bionic, сдвоенной камерой на 12 Мп и аккумулятором с поддержкой быстрой и беспроводной зарядки MagSafe. Корпус защищен от влаги и пыли по стандарту IP68 и имеет стеклянную заднюю панель с покрытием Ceramic Shield на передней части.', NULL, 115999.00, NULL, 14, CURRENT_TIMESTAMP),
  (5, 'Чехол для Huawei P50', 'тонкая накладка BROSCORP выполнена из качественного силикона с матовым покрытием и бортиком вокруг камер, черная', NULL, 2500.00, 5, 9, CURRENT_TIMESTAMP),
  (6, 'Смартфон Huawei P50', 'Легкий и эргономичный корпус. Несмотря на большой экран и мощную батарею, смартфон HUAWEI P50 отличается компактным дизайном. · Красочный мир на ладони', NULL, 38000.00, 3, 0, CURRENT_TIMESTAMP),
  (7, 'Смартфон Samsung Galaxy S8', 'Встроенные датчики. акселерометр (G-sensor), барометр, геомагнитный датчик, датчик Холла, датчик освещенности, датчик приближения, датчик сердечного ритма', NULL, 21000.00, NULL, 98, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;
INSERT INTO product_category (product_id, category_id) VALUES
  (1, 6),
  (2, 6),
  (3, 5),
  (4, 4),
  (5, 8),
  (6, 3),
  (7, 1),
  (8, 7)
ON CONFLICT DO NOTHING;
INSERT INTO users (id, first_name, last_name, email, password, created_at, role) VALUES
  (1, 'Алексей', 'Смирнов', 'alex.smirnov@example.com', '$2a$12$apejHD6QeRSQS31JK9hnju.B.vHU8mCiMOLaeTYwrYWZpTVYBls3O', CURRENT_TIMESTAMP, 'ROLE_USER'),
  (2, 'Мария', 'Кузнецова', 'maria.kuzn@example.com', '$2a$12$apejHD6QeRSQS31JK9hnju.B.vHU8mCiMOLaeTYwrYWZpTVYBls3O', CURRENT_TIMESTAMP, 'ROLE_USER'),
  (3, 'Дмитрий', 'Попов', 'dmitry.popov@example.com', '$2a$12$apejHD6QeRSQS31JK9hnju.B.vHU8mCiMOLaeTYwrYWZpTVYBls3O', CURRENT_TIMESTAMP, 'ROLE_ADMIN'),
  (4, 'Сергей', 'Иванов', 'sergey.ivanov@example.com', '$2a$12$apejHD6QeRSQS31JK9hnju.B.vHU8mCiMOLaeTYwrYWZpTVYBls3O', CURRENT_TIMESTAMP, 'ROLE_USER'),
  (5, 'Ольга', 'Морозова', 'olga.moroz@example.com', '$2a$12$apejHD6QeRSQS31JK9hnju.B.vHU8mCiMOLaeTYwrYWZpTVYBls3O', CURRENT_TIMESTAMP, 'ROLE_USER')
ON CONFLICT (email) DO NOTHING;