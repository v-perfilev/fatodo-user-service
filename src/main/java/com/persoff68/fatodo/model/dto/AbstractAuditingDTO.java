package com.persoff68.fatodo.model.dto;

import lombok.Data;

import java.time.Instant;

@Data
public abstract class AbstractAuditingDTO {
    private static final long serialVersionUID = 1L;

    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;

}
