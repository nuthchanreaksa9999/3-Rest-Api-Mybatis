package co.istad.mobilebanking.api.notification;

import co.istad.mobilebanking.api.notification.web.CreateNotificationDto;
import co.istad.mobilebanking.api.notification.web.NotificationDto;

public interface NotificationService {

    boolean pushNotification(CreateNotificationDto notificationDto);

}
