package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.config.constant.AppConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class ResetPasswordDTO implements Serializable {
    protected static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

    @NotNull
    private String userId;

    @NotNull
    private String password;

}
