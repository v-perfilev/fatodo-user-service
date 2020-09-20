package com.persoff68.fatodo.model.dto;

import lombok.Data;

import java.time.Instant;

@Data
public abstract class AbstractAuditingDTO implements AbstractDTO {

    protected String id;
    protected String createdBy;
    protected Instant createdDate;
    protected String lastModifiedBy;
    protected Instant lastModifiedDate;

}
