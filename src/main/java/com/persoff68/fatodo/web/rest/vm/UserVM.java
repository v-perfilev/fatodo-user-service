package com.persoff68.fatodo.web.rest.vm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVM {

    @NotNull
    private UUID id;
    @NotNull
    private String username;
    @NotNull
    private String language;

    private String imageFilename;
    private MultipartFile imageContent;

}
