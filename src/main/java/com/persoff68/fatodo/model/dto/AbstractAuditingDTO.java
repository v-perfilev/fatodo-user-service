package com.persoff68.fatodo.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
public abstract class AbstractAuditingDTO implements Serializable {

    private String id;
    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;

}
