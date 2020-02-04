package com.persoff68.fatodo.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document(collection = "ftd_authority")
@Data
public class Authority {

    @NotNull
    private String name;
}
