package com.stablecoin.training.chainadapter.api;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.stablecoin.training.chainadapter.service.ChainQueryService;
import com.stablecoin.training.chainadapter.service.TxStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ChainController.class)
class ChainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChainQueryService chainQueryService;

    @Test
    void returnsLatestBlock() throws Exception {
        when(chainQueryService.latestBlock())
            .thenReturn(new BlockResponse(100L, "0xabc", "0xdef", "0x1", 2));

        mockMvc.perform(get("/api/v1/chain/blocks/latest"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.number").value(100))
            .andExpect(jsonPath("$.hash").value("0xabc"));
    }

    @Test
    void returnsBadRequestForInvalidBlockNumber() throws Exception {
        mockMvc.perform(get("/api/v1/chain/blocks/not-a-number"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("BAD_REQUEST"));
    }

    @Test
    void returnsTransaction() throws Exception {
        when(chainQueryService.transactionByHash("0x123")).thenReturn(new TransactionResponse(
            "0x123",
            "0xfrom",
            "0xto",
            "0x10",
            "0x1",
            100L,
            TxStatus.MINED,
            2
        ));

        mockMvc.perform(get("/api/v1/chain/transactions/0x123"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.hash").value("0x123"))
            .andExpect(jsonPath("$.status").value("MINED"));
    }
}
