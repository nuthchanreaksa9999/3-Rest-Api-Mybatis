package co.istad.mobilebanking.api.notification.web;

import co.istad.mobilebanking.api.notification.NotificationService;
import co.istad.mobilebanking.api.notification.NotificationServiceImpl;
import co.istad.mobilebanking.base.BaseRest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.Notification;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationRestController {

    private final NotificationService notificationService;

    @PostMapping
    public BaseRest<?> pushNotification(@RequestBody CreateNotificationDto notificationDto) {
        boolean success = notificationService.pushNotification(notificationDto);
        return BaseRest.builder()
                .status(true)
                .code(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .message("Notification sent")
                .data(success)
                .build();
    }


}
