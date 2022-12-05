package com.persoff68.fatodo.model;

import com.persoff68.fatodo.model.constant.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Info {

    private String firstname;

    private String lastname;

    private Gender gender;

    private String imageFilename;

}
