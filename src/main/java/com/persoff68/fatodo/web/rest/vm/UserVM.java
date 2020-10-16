package com.persoff68.fatodo.web.rest.vm;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class UserVM {

    @NotNull
    private String id;
    @NotNull
    private String username;
    @NotNull
    private String language;

    private String imageFilename;
    private MultipartFile imageContent;

}
