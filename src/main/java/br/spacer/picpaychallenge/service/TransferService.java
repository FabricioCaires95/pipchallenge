package br.spacer.picpaychallenge.service;

import br.spacer.picpaychallenge.dto.TransferDto;
import br.spacer.picpaychallenge.entity.Transfer;
import br.spacer.picpaychallenge.entity.Wallet;
import br.spacer.picpaychallenge.exception.InsufficientBalanceException;
import br.spacer.picpaychallenge.exception.TransferNotAuthorizedException;
import br.spacer.picpaychallenge.exception.TransferNowAllowedException;
import br.spacer.picpaychallenge.repository.TransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
public class TransferService {

    private final AuthorizationService authorizationService;
    private final NotificationService notificationService;
    private final WalletService walletService;
    private final TransferRepository transferRepository;


    public TransferService(AuthorizationService authorizationService, NotificationService notificationService, WalletService walletService, TransferRepository transferRepository) {
        this.authorizationService = authorizationService;
        this.notificationService = notificationService;
        this.walletService = walletService;
        this.transferRepository = transferRepository;
    }

    @Transactional
    public Transfer transfer(TransferDto transferDto) {
        var sender = walletService.findWalletById(transferDto.payer());
        var receiver = walletService.findWalletById(transferDto.payee());

        validateTransfer(transferDto, sender);

        sender.debit(transferDto.value());
        receiver.credit(transferDto.value());

        var transfer = new Transfer(sender, receiver, transferDto.value());

        walletService.updateWalletBalance(sender);
        walletService.updateWalletBalance(receiver);
        var transferResult = transferRepository.save(transfer);

        CompletableFuture.runAsync(() -> notificationService.sendNotification(transferResult));

        return transferResult;
    }

    private void validateTransfer(TransferDto transferDto, Wallet sender) {
        if (!sender.isTransferAllowedForWalletType()) {
            throw new TransferNowAllowedException();
        }

        if (!sender.isBalanceEqualOrGreatherThan(transferDto.value())) {
            throw new InsufficientBalanceException();
        }

        if (!authorizationService.isAuthorized(transferDto)) {
            throw new TransferNotAuthorizedException();
        }
    }
}
