package br.spacer.picpaychallenge.controller;

import br.spacer.picpaychallenge.dto.CreateWalletDto;
import br.spacer.picpaychallenge.entity.Wallet;
import br.spacer.picpaychallenge.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/wallets")
    public ResponseEntity<Wallet> saveWallet(@RequestBody @Valid CreateWalletDto request) {
        return ResponseEntity.ok(walletService.createWallet(request));
    }
}
