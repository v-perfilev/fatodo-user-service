package com.persoff68.fatodo.model.vm;

import com.persoff68.fatodo.model.constant.EmailNotificationType;
import com.persoff68.fatodo.model.constant.PushNotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationsVM {

    @NotNull
    private Set<PushNotificationType> pushNotifications;

    @NotNull
    private Set<EmailNotificationType> emailNotifications;

}
