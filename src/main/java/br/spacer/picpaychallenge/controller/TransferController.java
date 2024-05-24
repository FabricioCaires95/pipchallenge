package br.spacer.picpaychallenge.controller;

import br.spacer.picpaychallenge.dto.TransferDto;
import br.spacer.picpaychallenge.entity.Transfer;
import br.spacer.picpaychallenge.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transfer> transfer(@RequestBody @Valid TransferDto transferDto) {
        return ResponseEntity.ok(transferService.transfer(transferDto));
    }
}
