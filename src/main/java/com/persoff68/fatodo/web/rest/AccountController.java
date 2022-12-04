package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.mapper.UserMapper;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.model.vm.ChangeLanguageVM;
import com.persoff68.fatodo.model.vm.ChangePasswordVM;
import com.persoff68.fatodo.model.vm.UserVM;
import com.persoff68.fatodo.security.exception.UnauthorizedException;
import com.persoff68.fatodo.security.util.SecurityUtils;
import com.persoff68.fatodo.service.AccountService;
import com.persoff68.fatodo.service.UserService;
import com.persoff68.fatodo.web.rest.exception.InvalidFormException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping(AccountController.ENDPOINT)
@RequiredArgsConstructor
public class AccountController {
    static final String ENDPOINT = "/api/account";

    private final AccountService accountService;
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<UserDTO> getCurrentUser() {
        UUID id = SecurityUtils.getCurrentId().orElseThrow(UnauthorizedException::new);
        User user = userService.getById(id);
        UserDTO userDTO = userMapper.pojoToDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDTO> update(@ModelAttribute @Valid UserVM userVM) {
        User newUser = userMapper.vmToPojo(userVM);
        byte[] imageContent = getBytesFromMultipartFile(userVM.getImageContent());
        User user = accountService.update(newUser, imageContent);
        UserDTO userDTO = userMapper.pojoToDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping(value = "/password")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordVM changePasswordVM) {
        accountService.changePassword(changePasswordVM.getOldPassword(), changePasswordVM.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/language")
    public ResponseEntity<Void> changeLanguage(@RequestBody @Valid ChangeLanguageVM changeLanguageVM) {
        accountService.changeLanguage(changeLanguageVM.getLanguage());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAccountPermanently() {
        accountService.deleteAccountPermanently();
        return ResponseEntity.ok().build();
    }

    private byte[] getBytesFromMultipartFile(MultipartFile multipartFile) {
        try {
            return multipartFile != null && !multipartFile.isEmpty()
                    ? multipartFile.getBytes()
                    : null;
        } catch (IOException e) {
            throw new InvalidFormException();
        }
    }

}
