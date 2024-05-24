package br.spacer.picpaychallenge.service;

import br.spacer.picpaychallenge.dto.CreateWalletDto;
import br.spacer.picpaychallenge.entity.Wallet;
import br.spacer.picpaychallenge.exception.WalletAlreadyExistException;
import br.spacer.picpaychallenge.exception.WalletNotFoundException;
import br.spacer.picpaychallenge.repository.WalletRepository;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet createWallet(CreateWalletDto createWalletDto) {
        var wallert = walletRepository.findByCpfCnpjOrEmail(createWalletDto.cpfCnpj(), createWalletDto.email());

        if (wallert.isPresent()) {
            throw new WalletAlreadyExistException("CpfCnpj or Email already exists");
        }

        return walletRepository.save(createWalletDto.toWallet());
    }

    public Wallet findWalletById(Long id) {
        return walletRepository.findById(id).orElseThrow(() -> new WalletNotFoundException(id));
    }

    public void updateWalletBalance(Wallet wallet) {
        walletRepository.save(wallet);
    }

}
