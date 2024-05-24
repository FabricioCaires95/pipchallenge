package br.spacer.picpaychallenge.service;

import br.spacer.picpaychallenge.client.AuthorizationClient;
import br.spacer.picpaychallenge.dto.TransferDto;
import br.spacer.picpaychallenge.entity.Transfer;
import br.spacer.picpaychallenge.exception.PicPayException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final AuthorizationClient authorizationClient;

    public AuthorizationService(AuthorizationClient authorizationClient) {
        this.authorizationClient = authorizationClient;
    }

    public boolean isAuthorized(TransferDto transfer) {
        var response = authorizationClient.isAuthorized();

        if (!response.getStatusCode().isError()) {
            throw new PicPayException();
        }

        return response.getBody().authorized();
    }
}
