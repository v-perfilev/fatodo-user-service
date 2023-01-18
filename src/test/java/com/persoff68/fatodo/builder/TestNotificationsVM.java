package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.constant.EmailNotificationType;
import com.persoff68.fatodo.model.constant.PushNotificationType;
import com.persoff68.fatodo.model.vm.NotificationsVM;
import lombok.Builder;

import java.util.Set;

public class TestNotificationsVM extends NotificationsVM {

    @Builder
    public TestNotificationsVM(Set<PushNotificationType> pushNotifications,
                               Set<EmailNotificationType> emailNotifications) {
        super(pushNotifications, emailNotifications);
    }

    public static TestNotificationsVMBuilder defaultBuilder() {
        return TestNotificationsVM.builder()
                .pushNotifications(Set.of(PushNotificationType.CHAT_MESSAGE_CREATE))
                .emailNotifications(Set.of(EmailNotificationType.REMINDER));
    }

}
