package com.github.davidsmith.techtest.service.spins.spinner;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.random.RandomGenerator;

class RandomSlotSpinnerTest {

    private static final String BLACK = "BLACK";
    private static final String WHITE = "WHITE";
    private static final String GREEN = "GREEN";
    private static final String YELLOW = "YELLOW";

    @Nested
    class Spin {

        @Test
        void canReturnAllPossibleColours() {
            final List<String> colours = List.of(
                BLACK,
                WHITE,
                GREEN,
                YELLOW
            );

            RandomGenerator colourPicker = Mockito.mock();
            Mockito.doReturn(0)
                .doReturn(1)
                .doReturn(2)
                .doReturn(3)
                .when(colourPicker)
                .nextInt(0, 4);

            RandomSlotSpinner spinner = new RandomSlotSpinner(colours, 4, colourPicker);

            Assertions.assertThat(spinner.spin())
                .hasSize(4)
                .containsExactly(BLACK, WHITE, GREEN, YELLOW);
        }

        @Test
        void canReturnACombinationOfColours() {
            final List<String> colours = List.of(
                BLACK,
                WHITE,
                GREEN,
                YELLOW
            );

            RandomGenerator colourPicker = Mockito.mock();
            Mockito.doReturn(0)
                .doReturn(1)
                .doReturn(2)
                .doReturn(2)
                .doReturn(2)
                .doReturn(3)
                .when(colourPicker)
                .nextInt(0, 4);

            RandomSlotSpinner spinner = new RandomSlotSpinner(colours, 6, colourPicker);

            Assertions.assertThat(spinner.spin())
                .hasSize(6)
                .containsExactly(BLACK, WHITE, GREEN, GREEN, GREEN, YELLOW);
        }
    }


}