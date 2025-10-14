INSERT INTO items (id, name, price, deleted)
VALUES (-1, 'test1', 1234, false),
       (-2, 'test2', 1234, false),
       (-3, 'test3', 1234, false),
       (-4, 'test4', 1234, false)
       ON CONFLICT (id) DO NOTHING;
INSERT INTO orders (id, user_id, status, creation_date)
VALUES (-1, -1, 'CREATED', now()),
       (-2, -1, 'CREATED', now()),
       (-3, -1, 'PENDING', now()),
       (-4, -1, 'COMPLETED', now())
       ON CONFLICT (id) DO NOTHING;
INSERT INTO order_items (id, order_id, item_id, quantity)
VALUES (-1, -1, -1, 12),
       (-2, -1, -2, 10),
       (-3, -1, -3, 4),
       (-4, -1, -4, 67)
       ON CONFLICT (id) DO NOTHING;

