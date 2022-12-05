package com.persoff68.fatodo.model.vm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoVM {

    @NotNull
    private String username;

    private String firstname;

    private String lastname;

    private String gender;

    private String imageFilename;

    private MultipartFile imageContent;

}
