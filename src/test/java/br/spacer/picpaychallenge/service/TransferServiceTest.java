package br.spacer.picpaychallenge.service;

import br.spacer.picpaychallenge.entity.Transfer;
import br.spacer.picpaychallenge.entity.Wallet;
import br.spacer.picpaychallenge.exception.InsufficientBalanceException;
import br.spacer.picpaychallenge.exception.TransferNotAuthorizedException;
import br.spacer.picpaychallenge.exception.TransferNowAllowedException;
import br.spacer.picpaychallenge.repository.TransferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static br.spacer.picpaychallenge.helper.TestHelpers.createMerchantWalletType;
import static br.spacer.picpaychallenge.helper.TestHelpers.createTransferDto;
import static br.spacer.picpaychallenge.helper.TestHelpers.createUserWalletType;
import static br.spacer.picpaychallenge.helper.TestHelpers.createWalletWithBalance;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransferServiceTest {

    @InjectMocks
    private TransferService transferService;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private WalletService walletService;

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private NotificationService notificationService;

    @Captor
    private ArgumentCaptor<Wallet> walletCaptor;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test create transfer success")
    void testCreateTransferSuccess() {
        var userWallet = createUserWalletType();
        var sender = createWalletWithBalance(userWallet, BigDecimal.valueOf(80));

        var merchantWallet = createMerchantWalletType();
        var receiver = createWalletWithBalance(merchantWallet, BigDecimal.valueOf(100));

        var transfer = new Transfer(sender, receiver, BigDecimal.valueOf(30));

        when(walletService.findWalletById(anyLong())).thenReturn(sender, receiver);
        when(authorizationService.isAuthorized(any())).thenReturn(true);
        when(transferRepository.save(any())).thenReturn(transfer);

        Transfer transferCreated = transferService.transfer(createTransferDto(BigDecimal.valueOf(30)));

        assertNotNull(transferCreated);
        assertEquals(merchantWallet, transferCreated.getSender().getWalletType());
        assertEquals(userWallet, transferCreated.getReceiver().getWalletType());

        verify(walletService, times(2)).updateWalletBalance(walletCaptor.capture());

        var senderBalanceAfterDebit = walletCaptor.getAllValues().get(0).getBalance();
        assertEquals(BigDecimal.valueOf(50), senderBalanceAfterDebit);

        var receiverBalanceAfterCredit = walletCaptor.getAllValues().get(1).getBalance();
        assertEquals(BigDecimal.valueOf(130), receiverBalanceAfterCredit);

        verify(notificationService, times(1)).sendNotification(any());
    }

    @Test
    @DisplayName("Test transfer with insufficient balance")
    void testTransferWithInsufficientBalance() {
        var userWallet = createUserWalletType();
        var sender = createWalletWithBalance(userWallet, BigDecimal.ZERO);

        var merchantWallet = createMerchantWalletType();
        var receiver = createWalletWithBalance(merchantWallet, BigDecimal.TEN);

        when(walletService.findWalletById(anyLong())).thenReturn(sender, receiver);

        assertThrows(InsufficientBalanceException.class, () -> transferService.transfer(createTransferDto(BigDecimal.TEN)));

        verify(walletService, times(0)).updateWalletBalance(any());
        verify(notificationService, times(0)).sendNotification(any());
    }

    @Test
    @DisplayName("Test transfer wallet type not allowed")
    void testTransferWalletTypeNotAllowed() {
        var senderMerchantWallet = createMerchantWalletType();
        var sender = createWalletWithBalance(senderMerchantWallet, BigDecimal.ZERO);

        var merchantWallet = createMerchantWalletType();
        var receiver = createWalletWithBalance(merchantWallet, BigDecimal.TEN);

        when(walletService.findWalletById(anyLong())).thenReturn(sender, receiver);
        when(authorizationService.isAuthorized(any())).thenReturn(true);

        assertThrows(TransferNowAllowedException.class, () -> transferService.transfer(createTransferDto(BigDecimal.TEN)));

        verify(walletService, times(0)).updateWalletBalance(any());
        verify(notificationService, times(0)).sendNotification(any());
    }

    @Test
    @DisplayName("Test transfer Not Authorized")
    void testTransferNotAuthorized() {
        var userWallet = createUserWalletType();
        var sender = createWalletWithBalance(userWallet, BigDecimal.TEN);

        var merchantWallet = createMerchantWalletType();
        var receiver = createWalletWithBalance(merchantWallet, BigDecimal.TEN);

        when(walletService.findWalletById(anyLong())).thenReturn(sender, receiver);
        when(authorizationService.isAuthorized(any())).thenReturn(false);

        assertThrows(TransferNotAuthorizedException.class, () -> transferService.transfer(createTransferDto(BigDecimal.valueOf(5))));

        verify(walletService, times(0)).updateWalletBalance(any());
        verify(notificationService, times(0)).sendNotification(any());
    }


}
