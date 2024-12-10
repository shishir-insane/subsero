package com.sr.subsero.notification;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NotificationDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String type;

    @NotNull
    @Size(max = 255)
    private String method;

    @NotNull
    private String message;

    @NotNull
    private OffsetDateTime reminderDate;

    @Size(max = 255)
    private String status;

    @Size(max = 255)
    private String priority;

    private OffsetDateTime createdAt;

    @NotNull
    private Long user;

    @NotNull
    private Long subscription;

}
