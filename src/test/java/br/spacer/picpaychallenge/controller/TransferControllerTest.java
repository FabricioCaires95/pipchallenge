package br.spacer.picpaychallenge.controller;

import br.spacer.picpaychallenge.dto.TransferDto;
import br.spacer.picpaychallenge.service.TransferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static br.spacer.picpaychallenge.helper.TestHelpers.createTransferDto;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransferController.class)
public class TransferControllerTest {

    @MockBean
    private TransferService transferService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testShouldCreateTransfer200() throws Exception {
        var transferDto = createTransferDto(BigDecimal.TEN);

        mockMvc.perform(post("/transfer")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(transferDto)))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testShouldNotCreateTransferWallet(BigDecimal value, Long payerId, Long payeeId) throws Exception {
        var transferDto = new TransferDto(value, payerId, payeeId);

        mockMvc.perform(post("/transfer")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(transferDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.invalid-params.length()").value(3));
    }

    private static Stream<Arguments> provideParameters() {
        return Stream.of(
                Arguments.of(null, null, null)
        );
    }


}
