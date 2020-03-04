package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.config.constant.AppConstants;
import lombok.Data;

import java.time.Instant;

@Data
public abstract class AbstractAuditingDTO {
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;

}
