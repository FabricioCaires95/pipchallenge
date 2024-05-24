package br.spacer.picpaychallenge.service;

import br.spacer.picpaychallenge.client.NotificationClient;
import br.spacer.picpaychallenge.entity.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationClient notificationClient;

    public NotificationService(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    public void sendNotification(Transfer transfer) {
        try {
            log.info("Sending notification");
            var response = notificationClient.sendNotification(transfer);

            if (response.getStatusCode().isError()) {
                log.error("Error sending notification, status code is not OK");
            }
        } catch (Exception e) {
            log.error("Error sending notification", e);
        }
    }
}
