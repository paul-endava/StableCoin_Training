package com.stablecoin.training.chainadapter.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.stablecoin.training.chainadapter.config.EthRpcProperties;
import com.stablecoin.training.chainadapter.eth.EthereumRpcClient;
import com.stablecoin.training.chainadapter.eth.RpcException;
import com.stablecoin.training.chainadapter.api.BlockResponse;
import com.stablecoin.training.chainadapter.api.TransactionResponse;
import org.springframework.stereotype.Service;

@Service
public class ChainQueryService {
    private final EthereumRpcClient rpcClient;
    private final TxStatusResolver txStatusResolver;
    private final EthRpcProperties ethRpcProperties;

    public ChainQueryService(EthereumRpcClient rpcClient, TxStatusResolver txStatusResolver, EthRpcProperties ethRpcProperties) {
        this.rpcClient = rpcClient;
        this.txStatusResolver = txStatusResolver;
        this.ethRpcProperties = ethRpcProperties;
    }

    public BlockResponse latestBlock() {
        JsonNode blockNode = rpcClient.getBlockByTag("latest");
        if (blockNode == null || blockNode.isNull()) {
            throw new RpcException("Latest block not found");
        }
        return mapBlock(blockNode);
    }

    public BlockResponse blockByNumber(long number) {
        JsonNode blockNode = rpcClient.getBlockByNumber(number);
        if (blockNode == null || blockNode.isNull()) {
            throw new RpcException("Block %d not found".formatted(number));
        }
        return mapBlock(blockNode);
    }

    public TransactionResponse transactionByHash(String txHash) {
        JsonNode txNode = rpcClient.getTransactionByHash(txHash);

        if (txNode == null || txNode.isNull()) {
            return new TransactionResponse(txHash, null, null, null, null, null, TxStatus.PENDING, 0);
        }

        String blockNumberHex = EthereumRpcClient.text(txNode, "blockNumber");
        Long blockNumber = blockNumberHex == null ? null : EthereumRpcClient.hexToLong(blockNumberHex);

        long head = rpcClient.getLatestBlockNumber();
        TxStatus status = txStatusResolver.resolve(blockNumber, head, ethRpcProperties.minConfirmations());
        int confirmations = blockNumber == null ? 0 : (int) Math.max(0, head - blockNumber);

        return new TransactionResponse(
            EthereumRpcClient.text(txNode, "hash"),
            EthereumRpcClient.text(txNode, "from"),
            EthereumRpcClient.text(txNode, "to"),
            EthereumRpcClient.text(txNode, "value"),
            EthereumRpcClient.text(txNode, "nonce"),
            blockNumber,
            status,
            confirmations
        );
    }

    private static BlockResponse mapBlock(JsonNode blockNode) {
        return new BlockResponse(
            EthereumRpcClient.hexToLong(EthereumRpcClient.text(blockNode, "number")),
            EthereumRpcClient.text(blockNode, "hash"),
            EthereumRpcClient.text(blockNode, "parentHash"),
            EthereumRpcClient.text(blockNode, "timestamp"),
            EthereumRpcClient.txCount(blockNode)
        );
    }
}
