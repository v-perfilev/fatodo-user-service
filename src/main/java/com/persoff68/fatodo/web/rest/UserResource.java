package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.mapper.UserMapper;
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
import java.util.UUID;

@RestController
@RequestMapping(UserResource.ENDPOINT)
@RequiredArgsConstructor
public class UserResource {

    static final String ENDPOINT = "/api/users";

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<UserDTO>> getAll() {
        List<User> userList = userService.getAll();
        List<UserDTO> userDTOList = userList.stream()
                .map(userMapper::pojoToDTO)
                .toList();
        return ResponseEntity.ok(userDTOList);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getById(@PathVariable UUID id) {
        User user = userService.getById(id);
        UserDTO userDTO = userMapper.pojoToDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserDTO userDTO) {
        User user = userMapper.dtoToPojo(userDTO);
        user = userService.createLocal(user);
        userDTO = userMapper.pojoToDTO(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> update(@Valid @RequestBody UserDTO userDTO) {
        User user = userMapper.dtoToPojo(userDTO);
        user = userService.update(user);
        userDTO = userMapper.pojoToDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

}
