package com.github.davidsmith.techtest.service.spins.spinner;

import com.github.davidsmith.techtest.exception.BadMachineException;
import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.Set;

class SlotSpinnerFactoryTest {

    final SlotSpinnerFactory slotSpinnerFactory = new SlotSpinnerFactory();

    @Nested
    class Create {
        @Test
        void createsExpected_whenRandom() {
            final MachineEntity machine = create(MachineEntity.SpinType.RANDOM);

            Assertions.assertThat(slotSpinnerFactory.create(machine))
                .isExactlyInstanceOf(RandomSlotSpinner.class);
        }

        @Test
        void throwsException_whenNull() {
            final MachineEntity machine = create(null);

            Assertions.assertThatThrownBy(() -> slotSpinnerFactory.create(machine))
                .isExactlyInstanceOf(BadMachineException.class);
        }


        private MachineEntity create(MachineEntity.SpinType spinType) {
            return MachineEntity.builder()
                .colours(Set.of("COLOUR"))
                .numberOfSlots(1)
                .spinType(spinType)
                .build();
        }
    }

}