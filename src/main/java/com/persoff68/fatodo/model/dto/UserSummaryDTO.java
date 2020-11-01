package com.persoff68.fatodo.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserSummaryDTO extends AbstractDTO {

    private String username;

    private String firstname;

    private String lastname;

    private String imageFilename;

}
