CREATE TYPE fruit_machine_type AS ENUM ('RANDOM');

CREATE TABLE fruit_machine (
    id BIGINT PRIMARY KEY,
    colours TEXT[] NOT NULL,
    number_of_slots SMALLINT NOT NULL,
    spin_type fruit_machine_type NOT NULL,
    float_in_pence BIGINT NOT NULL
);

INSERT INTO fruit_machine (id, colours, number_of_slots, spin_type, float_in_pence)
VALUES (
    1,
    array['BLACK', 'WHITE', 'GREEN', 'YELLOW'],
    4,
    'RANDOM',
    10000
);