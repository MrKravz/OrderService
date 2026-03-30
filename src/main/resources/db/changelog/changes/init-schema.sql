--liquibase formatted sql

--changeset init:1
CREATE TABLE items
(
    id    BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name  VARCHAR,
    price BIGINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE orders
(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    status VARCHAR,
    total_price BIGINT,
    deleted BOOLEAN,
    user_id BIGINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE order_items
(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    order_id BIGINT REFERENCES orders(id),
    item_id BIGINT REFERENCES items(id),
    quantity BIGINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX order_items_idx ON order_items(order_id, item_id);