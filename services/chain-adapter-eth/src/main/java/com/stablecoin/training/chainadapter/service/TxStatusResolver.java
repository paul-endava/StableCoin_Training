package com.stablecoin.training.chainadapter.service;

import org.springframework.stereotype.Component;

@Component
public class TxStatusResolver {

    public TxStatus resolve(Long blockNumber, long chainHead, int minConfirmations) {
        if (blockNumber == null) {
            return TxStatus.PENDING;
        }
        long depth = chainHead - blockNumber;
        if (depth < 0) {
            return TxStatus.REORGED;
        }
        return depth >= minConfirmations ? TxStatus.CONFIRMED : TxStatus.MINED;
    }
}
