CREATE TABLE user_spins (
    user_id BIGINT NOT NULL,
    machine_id BIGINT NOT NULL,

    PRIMARY KEY (user_id, machine_id)
);