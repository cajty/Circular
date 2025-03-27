package org.ably.circular.user;


import org.ably.circular.auth.RegisterRequest;
import org.ably.circular.role.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface UserMapper {

@Mapping(target = "createdAt", expression = "java(new java.util.Date())")
@Mapping(target ="firstName", source = "firstName")
@Mapping(target ="lastName", source = "lastName")
User toEntity(RegisterRequest registerRequest);

@Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToStringSet")
UserResponse toResponse(User user);

 @Named("rolesToStringSet")
    default Set<String> rolesToStringSet(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }




}