package com.persoff68.fatodo.web.rest.vm;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChangePasswordVM {

    private String oldPassword;
    private String newPassword;

}
