package com.persoff68.fatodo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Info {

    private String firstname;

    private String lastname;

    private String imageFilename;

    private String language;

    private String timezone;

}
