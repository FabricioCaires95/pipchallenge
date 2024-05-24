package br.spacer.picpaychallenge.dto;

import br.spacer.picpaychallenge.entity.Wallet;
import br.spacer.picpaychallenge.entity.WalletType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateWalletDto(@NotBlank(message = "Full name is required") String fullName,
                              @NotBlank(message = "Must not be blank" ) String cpfCnpj,
                              @NotBlank(message = "Must not be blank") String email,
                              @NotBlank(message = "Password is required") String password,
                              @NotNull(message = "Wallet type is required") WalletType.Enum walletType) {

    public Wallet toWallet() {
        return new Wallet(fullName, cpfCnpj, email, password, walletType.get());
    }
}
