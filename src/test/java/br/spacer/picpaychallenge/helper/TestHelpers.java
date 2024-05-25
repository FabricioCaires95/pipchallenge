package br.spacer.picpaychallenge.helper;

import br.spacer.picpaychallenge.dto.CreateWalletDto;
import br.spacer.picpaychallenge.dto.TransferDto;
import br.spacer.picpaychallenge.entity.Wallet;
import br.spacer.picpaychallenge.entity.WalletType;

import java.math.BigDecimal;

public final class TestHelpers {

    public static final String FULL_NAME = "Teste";
    public static final String EMAIL = "teste@picpay";
    public static final String CPF_CNPJ = "12345678901";
    public static final String PASSWORD = "123456";
    public static final WalletType ENUM_USER = WalletType.Enum.USER.get();
    public static final WalletType.Enum ENUM_TYPE_USER = WalletType.Enum.USER;
    public static final WalletType ENUM_MERCHANT = WalletType.Enum.MERCHANT.get();
    public static final BigDecimal VALUE = BigDecimal.valueOf(20);
    public static final BigDecimal BALANCE = BigDecimal.valueOf(80);

    public static final Long PAYER_ID = 2L;
    public static final Long PAYEE_ID = 3L;


    private TestHelpers() {}


    public static Wallet createWallet(WalletType walletType) {
        return new Wallet(FULL_NAME, CPF_CNPJ, EMAIL, PASSWORD, walletType);
    }

    public static Wallet createWalletWithBalance(WalletType walletType, BigDecimal balance) {
        var wallet = new Wallet(FULL_NAME, CPF_CNPJ, EMAIL, PASSWORD, walletType);
        wallet.setBalance(balance);
        return wallet;
    }

    public static WalletType createUserWalletType() {
        return WalletType.Enum.USER.get();
    }

    public static WalletType createMerchantWalletType() {
        return WalletType.Enum.MERCHANT.get();
    }

    public static CreateWalletDto createWalletDto() {
        return new CreateWalletDto(FULL_NAME, CPF_CNPJ, EMAIL, PASSWORD, ENUM_TYPE_USER);
    }

    public static TransferDto createTransferDto(BigDecimal value) {
        return new TransferDto(value, PAYER_ID, PAYEE_ID);
    }

}
