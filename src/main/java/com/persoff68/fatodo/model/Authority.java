package com.persoff68.fatodo.model;

import com.persoff68.fatodo.config.constant.AppConstants;
import com.persoff68.fatodo.model.constant.AuthorityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document(collection = "ftd_authority")
@Data
@AllArgsConstructor
public class Authority {
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

    @Id
    @NotNull
    private String name;

    public static Authority of(AuthorityType authorityType) {
        return new Authority(authorityType.getName());
    }
}
