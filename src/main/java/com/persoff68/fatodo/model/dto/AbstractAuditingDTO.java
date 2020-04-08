package com.persoff68.fatodo.model.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public abstract class AbstractAuditingDTO {

    private UUID id;
    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;

}
