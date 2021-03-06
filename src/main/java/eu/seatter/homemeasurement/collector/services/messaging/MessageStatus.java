package eu.seatter.homemeasurement.collector.services.messaging;

import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 24/02/2020
 * Time: 23:22
 */
@Component
public class MessageStatus {
    private MessageStatusType currentStatus;
    private ZonedDateTime lastUpdateDate;
    private ZonedDateTime lastSuccessSentDate;

    public void update (MessageStatusType status) {
        currentStatus = status;
        lastUpdateDate = ZonedDateTime.now(ZoneId.of("Etc/UTC")).truncatedTo(ChronoUnit.MINUTES);
        if(status == MessageStatusType.GOOD) {
            lastSuccessSentDate = ZonedDateTime.now(ZoneId.of("Etc/UTC")).truncatedTo(ChronoUnit.MINUTES);
        }
    }

    public MessageStatusType getStatus() {
        return currentStatus;
    }

    public ZonedDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public ZonedDateTime getLastSuccessSentDate() {
        return lastSuccessSentDate;
    }

    public boolean isLastSentMessageToday() {
        return lastSuccessSentDate.truncatedTo(ChronoUnit.DAYS).equals(ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS));
    }
}
