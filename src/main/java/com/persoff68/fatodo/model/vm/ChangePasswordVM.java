package com.persoff68.fatodo.model.vm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordVM {

    private String oldPassword;
    private String newPassword;

}
