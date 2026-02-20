package com.stablecoin.training.chainadapter.api;

import com.stablecoin.training.chainadapter.service.ChainQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chain")
public class ChainController {
    private final ChainQueryService chainQueryService;

    public ChainController(ChainQueryService chainQueryService) {
        this.chainQueryService = chainQueryService;
    }

    @GetMapping("/blocks/latest")
    public BlockResponse latestBlock() {
        return chainQueryService.latestBlock();
    }

    @GetMapping("/blocks/{number}")
    public BlockResponse blockByNumber(@PathVariable long number) {
        return chainQueryService.blockByNumber(number);
    }

    @GetMapping("/transactions/{txHash}")
    public TransactionResponse transactionByHash(@PathVariable String txHash) {
        return chainQueryService.transactionByHash(txHash);
    }
}
