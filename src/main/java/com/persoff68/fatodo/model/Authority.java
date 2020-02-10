package com.persoff68.fatodo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document(collection = "ftd_authority")
@Data
@AllArgsConstructor
public class Authority {

    @Id
    @NotNull
    private String name;
}
