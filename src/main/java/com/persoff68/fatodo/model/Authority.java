package com.persoff68.fatodo.model;

import com.persoff68.fatodo.config.constant.AppConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document(collection = "ftd_authority")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class Authority extends AbstractModel {
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

    @Id
    @NotNull
    private String name;

    public static Authority of(String name) {
        return new Authority(name);
    }
}
