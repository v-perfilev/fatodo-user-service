package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.model.mapper.UserMapper;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.UserManagementDTO;
import com.persoff68.fatodo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(UserResource.ENDPOINT)
@RequiredArgsConstructor
public class UserResource {

    static final String ENDPOINT = "/api/users";

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<UserManagementDTO>> getAll() {
        List<User> userList = userService.getAll();
        List<UserManagementDTO> userManagementDTOList = userList.stream()
                .map(userMapper::userToUserManagementDTO).collect(Collectors.toList());
        return ResponseEntity.ok(userManagementDTOList);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserManagementDTO> getById(@PathVariable String id) {
        User user = userService.getById(id);
        UserManagementDTO userManagementDTO = userMapper.userToUserManagementDTO(user);
        return ResponseEntity.ok(userManagementDTO);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserManagementDTO> create(@Valid @RequestBody UserManagementDTO userManagementDTO) {
        User user = userMapper.userManagementDTOToUser(userManagementDTO);
        user = userService.createLocal(user);
        userManagementDTO = userMapper.userToUserManagementDTO(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userManagementDTO);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserManagementDTO> update(@Valid @RequestBody UserManagementDTO userManagementDTO) {
        User user = userMapper.userManagementDTOToUser(userManagementDTO);
        user = userService.update(user);
        userManagementDTO = userMapper.userToUserManagementDTO(user);
        return ResponseEntity.ok(userManagementDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

}
