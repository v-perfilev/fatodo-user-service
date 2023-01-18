package com.persoff68.fatodo.model;

import com.persoff68.fatodo.model.constant.EmailNotificationType;
import com.persoff68.fatodo.model.constant.PushNotificationType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class Notifications {

    private Set<PushNotificationType> pushNotifications = Set.of(PushNotificationType.values());

    private Set<EmailNotificationType> emailNotifications = Set.of(EmailNotificationType.values());

}
