package com.stablecoin.training.chainadapter.api;

import com.stablecoin.training.chainadapter.service.TxStatus;

public record TransactionResponse(
    String hash,
    String from,
    String to,
    String value,
    String nonce,
    Long blockNumber,
    TxStatus status,
    int confirmations
) {
}
