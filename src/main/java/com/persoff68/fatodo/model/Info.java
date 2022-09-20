package com.persoff68.fatodo.model;

import com.persoff68.fatodo.model.constant.DateFormat;
import com.persoff68.fatodo.model.constant.Gender;
import com.persoff68.fatodo.model.constant.Language;
import com.persoff68.fatodo.model.constant.TimeFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Info {

    private String firstname;

    private String lastname;

    private String imageFilename;

    private Gender gender;

    private Language language = Language.EN;

    private String timezone;

    private TimeFormat timeFormat = TimeFormat.H24;

    private DateFormat dateFormat = DateFormat.YMD_DASH;

}
