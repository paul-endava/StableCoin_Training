package com.stablecoin.training.chainadapter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class TxStatusResolverTest {

    private final TxStatusResolver resolver = new TxStatusResolver();

    @Test
    void returnsPendingWhenBlockNumberMissing() {
        assertEquals(TxStatus.PENDING, resolver.resolve(null, 100, 12));
    }

    @Test
    void returnsMinedWhenBelowConfirmationThreshold() {
        assertEquals(TxStatus.MINED, resolver.resolve(99L, 100, 12));
    }

    @Test
    void returnsConfirmedWhenThresholdMet() {
        assertEquals(TxStatus.CONFIRMED, resolver.resolve(88L, 100, 12));
    }

    @Test
    void returnsReorgedWhenHeadBeforeBlock() {
        assertEquals(TxStatus.REORGED, resolver.resolve(101L, 100, 12));
    }
}
