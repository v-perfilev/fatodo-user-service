package com.persoff68.fatodo.model;

import com.persoff68.fatodo.config.constant.AppConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class Info implements Serializable {
    @Serial
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

    private String firstname;

    private String lastname;

    private String imageFilename;

    private String language;

    private String timezone;

}
