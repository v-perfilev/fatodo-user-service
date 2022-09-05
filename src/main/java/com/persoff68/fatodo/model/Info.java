package com.persoff68.fatodo.model;

import com.persoff68.fatodo.config.constant.Language;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Info {

    private String firstname;

    private String lastname;

    private String imageFilename;

    private Gender gender;

    private Language language;

    private String timezone;

    public enum Gender {
        MALE,
        FEMALE,
        DIVERSE
    }

}
