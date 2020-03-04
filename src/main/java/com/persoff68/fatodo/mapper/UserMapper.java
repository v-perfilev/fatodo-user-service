package com.persoff68.fatodo.mapper;

import com.persoff68.fatodo.model.Authority;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.constant.AuthorityType;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "authorities", target = "authorities", qualifiedByName = "authoritiesIntoStrings")
    UserPrincipalDTO toUserPrincipalDTO(User user);

    @Mapping(source = "authorities", target = "authorities", qualifiedByName = "authoritiesIntoStrings")
    UserDTO toDTO(User user);

    @Mapping(source = "authorities", target = "authorities", qualifiedByName = "stringsIntoAuthorities")
    User fromDTO(UserDTO userDTO);

    User fromLocalUserDTO(LocalUserDTO localUserDTO);

    User fromOAuth2UserDTO(OAuth2UserDTO oAuth2UserDTO);

    @Named("authoritiesIntoStrings")
    default Set<String> authoritiesIntoStrings(Set<Authority> authoritySet) {
        return authoritySet != null
                ? authoritySet.stream().map(Authority::getName).collect(Collectors.toSet())
                : null;
    }

    @Named("stringsIntoAuthorities")
    default Set<Authority> stringsIntoAuthorities(Set<String> authoritySet) {
        return authoritySet != null
                ? authoritySet.stream().filter(AuthorityType::contains).map(Authority::new).collect(Collectors.toSet())
                : null;
    }

}
