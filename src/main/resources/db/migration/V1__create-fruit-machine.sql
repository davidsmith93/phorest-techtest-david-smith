CREATE TABLE fruit_machines (
    id BIGINT PRIMARY KEY,
    colours TEXT[] NOT NULL,
    number_of_slots SMALLINT NOT NULL,
    spin_type VARCHAR NOT NULL,
    float_in_pence BIGINT NOT NULL,
    spin_price_in_pence BIGINT NOT NULL,
    small_prize_adjacency INT NOT NULL
);

INSERT INTO fruit_machines (id, colours, number_of_slots, spin_type, float_in_pence, spin_price_in_pence, small_prize_adjacency)
VALUES (
    1,
    array['BLACK', 'WHITE', 'GREEN', 'YELLOW'],
    4,
    'RANDOM',
    10000,
    100,
    2
);