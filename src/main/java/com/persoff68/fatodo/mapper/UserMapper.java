package com.persoff68.fatodo.mapper;

import com.persoff68.fatodo.config.constant.AuthorityType;
import com.persoff68.fatodo.model.Authority;
import com.persoff68.fatodo.model.Info;
import com.persoff68.fatodo.model.Notifications;
import com.persoff68.fatodo.model.Settings;
import com.persoff68.fatodo.model.User;
import com.persoff68.fatodo.model.dto.LocalUserDTO;
import com.persoff68.fatodo.model.dto.OAuth2UserDTO;
import com.persoff68.fatodo.model.dto.UserDTO;
import com.persoff68.fatodo.model.dto.UserInfoDTO;
import com.persoff68.fatodo.model.dto.UserPrincipalDTO;
import com.persoff68.fatodo.model.vm.InfoVM;
import com.persoff68.fatodo.model.vm.NotificationsVM;
import com.persoff68.fatodo.model.vm.SettingsVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "authorities", target = "authorities", qualifiedByName = "authoritiesIntoStrings")
    UserPrincipalDTO pojoToPrincipalDTO(User user);

    @Mapping(source = "authorities", target = "authorities", qualifiedByName = "authoritiesIntoStrings")
    UserDTO pojoToDTO(User user);

    @Mapping(source = "info.firstname", target = "firstname")
    @Mapping(source = "info.lastname", target = "lastname")
    @Mapping(source = "info.gender", target = "gender")
    @Mapping(source = "info.imageFilename", target = "imageFilename")
    @Mapping(source = "deleted", target = "deleted")
    UserInfoDTO pojoToInfoDTO(User user);

    @Mapping(source = "authorities", target = "authorities", qualifiedByName = "stringsIntoAuthorities")
    User dtoToPojo(UserDTO userDTO);

    @Mapping(source = "language", target = "settings.language")
    @Mapping(source = "timezone", target = "settings.timezone")
    User localDTOToPojo(LocalUserDTO localUserDTO);

    @Mapping(source = "language", target = "settings.language")
    @Mapping(source = "timezone", target = "settings.timezone")
    User oAuth2DTOToPojo(OAuth2UserDTO oAuth2UserDTO);

    Info vmToInfo(InfoVM infoVM);

    Settings vmToSettings(SettingsVM settingsVM);

    Notifications vmToNotifications(NotificationsVM notificationsVM);

    @Named("authoritiesIntoStrings")
    default Set<String> authoritiesIntoStrings(Set<Authority> authoritySet) {
        return authoritySet != null
                ? authoritySet.stream()
                .map(Authority::getName)
                .collect(Collectors.toSet())
                : null;
    }

    @Named("stringsIntoAuthorities")
    default Set<Authority> stringsIntoAuthorities(Set<String> stringSet) {
        return stringSet != null
                ? stringSet.stream()
                .filter(AuthorityType::contains)
                .map(Authority::new)
                .collect(Collectors.toSet())
                : null;
    }

}
