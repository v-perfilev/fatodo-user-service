package com.persoff68.fatodo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Info implements Serializable {

    private String firstname;

    private String lastname;

    private String imageFilename;

    private String language;

    private String timezone;

}
