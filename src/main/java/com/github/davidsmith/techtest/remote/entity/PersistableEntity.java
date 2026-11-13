package com.github.davidsmith.techtest.remote.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class PersistableEntity<T> implements Persistable<T> {

    @Transient
    private boolean newEntity = false;

    @Override
    public boolean isNew() {
        return newEntity;
    }

}
