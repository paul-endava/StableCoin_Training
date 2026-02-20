package com.stablecoin.training.chainadapter.api;

public record BlockResponse(
    long number,
    String hash,
    String parentHash,
    String timestamp,
    int transactionCount
) {
}
