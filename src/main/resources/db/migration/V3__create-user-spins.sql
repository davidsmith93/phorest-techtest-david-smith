CREATE TABLE user_spins (
    user_id BIGINT NOT NULL,
    fruit_machine_id BIGINT NOT NULL,
    free_spins INT NOT NULL,

    PRIMARY KEY (user_id, fruit_machine_id),
    CONSTRAINT fk_users FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_fruit_machines FOREIGN KEY (fruit_machine_id) REFERENCES fruit_machines(id)
);