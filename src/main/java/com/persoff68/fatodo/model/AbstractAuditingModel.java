package com.persoff68.fatodo.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Data
public abstract class AbstractAuditingModel implements AbstractModel {

    @Id
    protected String id;

    @CreatedBy
    protected String createdBy;

    @CreatedDate
    protected Instant createdDate;

    @LastModifiedBy
    protected String lastModifiedBy;

    @LastModifiedDate
    protected Instant lastModifiedDate;

}

