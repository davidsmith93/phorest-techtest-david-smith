CREATE TABLE fruit_machines (
    id SERIAL PRIMARY KEY,
    colours TEXT[] NOT NULL,
    number_of_slots SMALLINT NOT NULL,
    spin_type VARCHAR NOT NULL,
    float_in_pence BIGINT NOT NULL,
    spin_price_in_pence BIGINT NOT NULL,
    small_prize_adjacency INT NOT NULL
);