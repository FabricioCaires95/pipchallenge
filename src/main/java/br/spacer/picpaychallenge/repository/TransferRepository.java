package br.spacer.picpaychallenge.repository;

import br.spacer.picpaychallenge.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransferRepository extends JpaRepository<Transfer, UUID> {
}
