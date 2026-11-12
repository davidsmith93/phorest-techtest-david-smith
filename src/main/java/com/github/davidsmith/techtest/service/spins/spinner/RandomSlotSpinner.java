package com.github.davidsmith.techtest.service.spins.spinner;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

@Slf4j
@RequiredArgsConstructor
class RandomSlotSpinner implements SlotSpinner {

    @NonNull
    private final List<String> colours;
    private final int numberOfSlots;
    @NonNull
    private final RandomGenerator colourPicker;

    @Override
    public List<String> spin() {
        log.info("Spinning random machine with {} colours and {} slots.", colours, numberOfSlots);

        final List<String> slots = new ArrayList<>();

        for(int i = 0; i < numberOfSlots; i++) {
            slots.add(colours.get(getNextNumber()));
        }

        return slots;
    }

    private synchronized int getNextNumber() {
        return colourPicker.nextInt(0, colours.size());
    }
}
