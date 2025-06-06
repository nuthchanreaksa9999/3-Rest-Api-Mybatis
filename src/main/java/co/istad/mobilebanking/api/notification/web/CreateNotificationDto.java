package co.istad.mobilebanking.api.notification.web;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateNotificationDto(

        @JsonProperty("included_segments")
        String[] includedSegments,

        ContentDto contents

) {
}
