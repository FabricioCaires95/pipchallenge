package br.spacer.picpaychallenge.repository;

import br.spacer.picpaychallenge.entity.WalletType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletTypeRepository extends JpaRepository<WalletType, Long> {
}
