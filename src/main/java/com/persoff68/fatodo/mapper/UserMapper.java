package com.persoff68.fatodo.mapper;

import com.persoff68.fatodo.model.Authority;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.constant.AuthorityType;
import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "authorities", target = "authorities", qualifiedByName = "authoritiesIntoStrings")
    UserPrincipalDTO userToUserPrincipalDTO(User user);

    @Mapping(source = "authorities", target = "authorities", qualifiedByName = "authoritiesIntoStrings")
    UserDTO userToUserDto(User user);

    @Mapping(source = "authorities", target = "authorities", qualifiedByName = "stringsIntoAuthorities")
    User userDtoToUser(UserDTO userDTO);

    @Named("authoritiesIntoStrings")
    static Set<String> authoritiesIntoStrings(Set<Authority> authoritySet) {
        return authoritySet.stream().map(Authority::getName).collect(Collectors.toSet());
    }

    @Named("stringsIntoAuthorities")
    static Set<Authority> stringsIntoAuthorities(Set<String> authoritySet) {
        return authoritySet.stream().filter(AuthorityType::contains).map(Authority::new).collect(Collectors.toSet());
    }

}
