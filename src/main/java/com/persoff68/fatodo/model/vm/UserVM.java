package com.persoff68.fatodo.model.vm;

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

    private String firstname;

    private String lastname;

    private String gender;

    @NotNull
    private String language;

    @NotNull
    private String timezone;

    @NotNull
    private String timeFormat;

    @NotNull
    private String dateFormat;

    private String imageFilename;

    private MultipartFile imageContent;

    private boolean emailReminders;

}
