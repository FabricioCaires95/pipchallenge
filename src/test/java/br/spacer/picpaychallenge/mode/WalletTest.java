package br.spacer.picpaychallenge.mode;

import br.spacer.picpaychallenge.entity.Wallet;
import br.spacer.picpaychallenge.helper.TestHelpers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WalletTest {

    private Wallet wallet;

    @BeforeEach
    void setup() {
        wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(25));
        wallet.setWalletType(TestHelpers.createUserWalletType());
        wallet.setId(1L);
        wallet.setEmail("teste@spacerltd.com");
        wallet.setPassword("124@4asas4");
        wallet.setCpfCnpj("42578945512");
    }


    @Test
    void testShouldDebitBalance() {
        wallet.debit(new BigDecimal(10));

        assertEquals(15, wallet.getBalance().doubleValue());
    }

    @Test
    void testShouldCreditBalance() {
        wallet.credit(new BigDecimal(10));

        assertEquals(35, wallet.getBalance().doubleValue());
    }

    @Test
    void testTransferAllowedForUser() {
        assertTrue(wallet.isTransferAllowedForWalletType());
    }

    @Test
    void tesTransferAllowedNotForMerchant() {
        wallet.setWalletType(TestHelpers.createMerchantWalletType());
        assertFalse(wallet.isTransferAllowedForWalletType());
    }

    @Test
    void testTransferNotAllowedForBalanceLessThanValue() {
        wallet.setBalance(BigDecimal.ZERO);
        assertFalse(wallet.isBalanceEqualOrGreatherThan(BigDecimal.valueOf(15)));
    }

    @Test
    void testTransferAllowedForBalanceGreatherThanValue() {
        assertTrue(wallet.isBalanceEqualOrGreatherThan(BigDecimal.valueOf(15)));
    }
}
