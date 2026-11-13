package com.github.davidsmith.techtest.service.spins.result;

import com.github.davidsmith.techtest.model.SpinOutcome;
import com.github.davidsmith.techtest.remote.entity.MachineEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class CompositeOutcomeEvaluatorTest {

    @Mock
    JackpotOutcomeEvaluator jackpotOutcomeEvaluator;

    @Mock
    FullHouseOutcomeEvaluator fullHouseOutcomeEvaluator;

    @Mock
    SmallPrizeOutcomeEvaluator smallPrizeOutcomeEvaluator;

    @InjectMocks
    CompositeOutcomeEvaluator compositeOutcomeEvaluator;

    @Nested
    class Evaluate {
        @Test
        void returnsResultFromJackpotOutcomeEvaluator_whenDoesNotReturnLoss() {
            final MachineEntity machineEntity = Mockito.mock();
            final List<String> slots = Mockito.mock();

            Mockito.doReturn(SpinOutcome.JACKPOT)
                .when(jackpotOutcomeEvaluator)
                .evaluate(machineEntity, slots);

            Assertions.assertThat(compositeOutcomeEvaluator.evaluate(machineEntity, slots))
                .isEqualTo(SpinOutcome.JACKPOT);

            Mockito.verifyNoInteractions(fullHouseOutcomeEvaluator, smallPrizeOutcomeEvaluator);
        }

        @Test
        void returnsResultFromFullHouseOutcomeEvaluator_whenDoesNotReturnLoss() {
            final MachineEntity machineEntity = Mockito.mock();
            final List<String> slots = Mockito.mock();

            Mockito.doReturn(SpinOutcome.LOSS)
                .when(jackpotOutcomeEvaluator)
                .evaluate(machineEntity, slots);

            Mockito.doReturn(SpinOutcome.FULL_HOUSE)
                .when(fullHouseOutcomeEvaluator)
                .evaluate(machineEntity, slots);

            Assertions.assertThat(compositeOutcomeEvaluator.evaluate(machineEntity, slots))
                .isEqualTo(SpinOutcome.FULL_HOUSE);

            Mockito.verifyNoInteractions(smallPrizeOutcomeEvaluator);
        }

        @Test
        void returnsResultFromSmallPrizeOutcomeEvaluator_whenDoesNotReturnLoss() {
            final MachineEntity machineEntity = Mockito.mock();
            final List<String> slots = Mockito.mock();

            Mockito.doReturn(SpinOutcome.LOSS)
                .when(jackpotOutcomeEvaluator)
                .evaluate(machineEntity, slots);

            Mockito.doReturn(SpinOutcome.LOSS)
                .when(fullHouseOutcomeEvaluator)
                .evaluate(machineEntity, slots);

            Mockito.doReturn(SpinOutcome.SMALL_PRIZE)
                .when(smallPrizeOutcomeEvaluator)
                .evaluate(machineEntity, slots);

            Assertions.assertThat(compositeOutcomeEvaluator.evaluate(machineEntity, slots))
                .isEqualTo(SpinOutcome.SMALL_PRIZE);
        }
    }

}