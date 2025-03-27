package org.ably.circular.transaction.transactionItem;



import org.ably.circular.recyclableMaterial.Material;
import org.ably.circular.recyclableMaterial.MaterialMapper;
import org.ably.circular.transaction.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MaterialMapper.class})
public interface TransactionItemMapper {

    @Mapping(target = "transactionId", source = "transaction.id")
    TransactionItemResponse toResponse(TransactionItem transactionItem);

    List<TransactionItemResponse> toResponseList(List<TransactionItem> transactionItems);

    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "transaction", source = "transaction")
    @Mapping(target = "material.id", source = "request.materialId")
    @Mapping(target = "createdAt", expression = "java(new java.util.Date())")
    @Mapping(target = "deletedAt", ignore = true)
    TransactionItem toEntity(TransactionItemRequest request, Transaction transaction);


}