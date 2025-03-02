package org.ably.circular.transaction;

import org.ably.circular.enterprise.Enterprise;
import org.ably.circular.enterprise.EnterpriseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring", uses = {TransactionMapper.class})
public interface TransactionMapper {

   
    TransactionResponse toResponse(Transaction transaction);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "createdAt", expression = "java(new java.util.Date())")
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "buyer", source = "buyerId")
    @Mapping(target = "seller", source = "sellerId")
    Transaction toEntity(TransactionRequest request);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "completedAt", ignore = true)
    @Mapping(target = "buyer", source = "buyerId")
    @Mapping(target = "seller", source = "sellerId")
    void updateEntityFromRequest(TransactionRequest request, @MappingTarget Transaction transaction);


    default Enterprise longToEnterprise(Long id) {
        if (id == null) {
            return null;
        }
        Enterprise enterprise = new Enterprise();
        enterprise.setId(id);
        return enterprise;
    }
}