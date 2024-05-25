package br.spacer.picpaychallenge.service;

import br.spacer.picpaychallenge.exception.WalletAlreadyExistException;
import br.spacer.picpaychallenge.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static br.spacer.picpaychallenge.helper.TestHelpers.CPF_CNPJ;
import static br.spacer.picpaychallenge.helper.TestHelpers.EMAIL;
import static br.spacer.picpaychallenge.helper.TestHelpers.ENUM_USER;
import static br.spacer.picpaychallenge.helper.TestHelpers.FULL_NAME;
import static br.spacer.picpaychallenge.helper.TestHelpers.createUserWalletType;
import static br.spacer.picpaychallenge.helper.TestHelpers.createWallet;
import static br.spacer.picpaychallenge.helper.TestHelpers.createWalletDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WalletServiceTest {

    @InjectMocks
    private WalletService walletService;

    @Mock
    private WalletRepository walletRepository;

    @BeforeEach
    void setup () {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test create wallet success")
    void testCreateWallet() {
        var walletSaved = createWallet(createUserWalletType());

        when(walletRepository.findByCpfCnpjOrEmail(anyString(), anyString())).thenReturn(Optional.empty());
        when(walletRepository.save(any())).thenReturn(walletSaved);

        var wallet = walletService.createWallet(createWalletDto());

        assertNotNull(wallet);
        assertEquals(FULL_NAME, wallet.getFullName());
        assertEquals(EMAIL, wallet.getEmail());
        assertEquals(CPF_CNPJ, wallet.getCpfCnpj());
        assertEquals(ENUM_USER, wallet.getWalletType());
    }

    @Test
    @DisplayName("Test create wallet should fail because wallet already exists")
    void testCreateWalletFail() {
        var walletSaved = createWallet(createUserWalletType());

        when(walletRepository.findByCpfCnpjOrEmail(anyString(), anyString())).thenReturn(Optional.of(walletSaved));

        assertThrows(WalletAlreadyExistException.class, () -> walletService.createWallet(createWalletDto()));

        verify(walletRepository, times(0)).save(any());
    }

}
