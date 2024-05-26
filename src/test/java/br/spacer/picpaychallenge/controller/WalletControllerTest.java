package br.spacer.picpaychallenge.controller;

import br.spacer.picpaychallenge.dto.CreateWalletDto;
import br.spacer.picpaychallenge.entity.WalletType;
import br.spacer.picpaychallenge.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static br.spacer.picpaychallenge.helper.TestHelpers.createWalletDto;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WalletController.class)
public class WalletControllerTest {

    @MockBean
    private WalletService walletService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testshouldCreateWallet200() throws Exception {
        var createWalletDto = createWalletDto();

        mockMvc.perform(post("/wallets")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createWalletDto)))
                        .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testShouldNotCreateWalletInvalidEmail(String fullName, String cpfCnpj, String email, String password, WalletType.Enum type) throws Exception {
        var createWalletDto = new CreateWalletDto(fullName, cpfCnpj, email, password, type);

        mockMvc.perform(post("/wallets")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createWalletDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.invalid-params.length()").value(5));
    }

    private static Stream<Arguments> provideParameters() {
        return Stream.of(
                Arguments.of("", "", "", "", null)
        );
    }

}
