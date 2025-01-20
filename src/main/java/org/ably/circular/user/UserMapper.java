package org.ably.circular.user;


import org.ably.circular.auth.RegisterRequest;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {


User toEntity(RegisterRequest registerRequest);






}