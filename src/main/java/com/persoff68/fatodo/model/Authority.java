package com.persoff68.fatodo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document(collection = "ftd_authority")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Authority {

    @Id
    @NotNull
    private String name;

}
