package com.stablecoin.training.chainadapter.eth;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class EthereumRpcClient {
    private final RestClient restClient;

    public EthereumRpcClient(RestClient ethRestClient) {
        this.restClient = ethRestClient;
    }

    public JsonNode call(String method, List<Object> params) {
        Map<String, Object> payload = Map.of(
            "jsonrpc", "2.0",
            "id", 1,
            "method", method,
            "params", params
        );

        final JsonNode response;
        try {
            response = restClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .body(JsonNode.class);
        } catch (Exception ex) {
            throw new RpcException("Failed JSON-RPC call to Ethereum node: " + ex.getMessage());
        }

        if (response == null) {
            throw new RpcException("Empty JSON-RPC response");
        }

        JsonNode error = response.get("error");
        if (error != null && !error.isNull()) {
            throw new RpcException("JSON-RPC error: " + error);
        }

        JsonNode result = response.get("result");
        if (result == null) {
            throw new RpcException("JSON-RPC response missing result");
        }
        return result;
    }

    public long getLatestBlockNumber() {
        JsonNode result = call("eth_blockNumber", List.of());
        return hexToLong(result.asText());
    }

    public JsonNode getBlockByTag(String tag) {
        return call("eth_getBlockByNumber", List.of(tag, true));
    }

    public JsonNode getBlockByNumber(long number) {
        return getBlockByTag("0x" + Long.toHexString(number));
    }

    public JsonNode getTransactionByHash(String txHash) {
        return call("eth_getTransactionByHash", List.of(txHash));
    }

    public static long hexToLong(String hexValue) {
        if (hexValue == null || hexValue.isBlank()) {
            return 0L;
        }
        String normalized = hexValue.startsWith("0x") ? hexValue.substring(2) : hexValue;
        if (normalized.isBlank()) {
            return 0L;
        }
        return Long.parseUnsignedLong(normalized, 16);
    }

    public static String text(JsonNode node, String field) {
        JsonNode value = node.get(field);
        return value == null || value.isNull() ? null : value.asText();
    }

    public static int txCount(JsonNode blockNode) {
        JsonNode txs = blockNode.get("transactions");
        return txs == null || !txs.isArray() ? 0 : txs.size();
    }
}
