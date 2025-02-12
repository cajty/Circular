package org.ably.circular.user;


import org.ably.circular.auth.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

@Mapping(target = "createdAt", expression = "java(new java.util.Date())")
User toEntity(RegisterRequest registerRequest);






}