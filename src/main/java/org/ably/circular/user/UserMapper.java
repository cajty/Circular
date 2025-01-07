package org.ably.circular.user;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {



    @Mapping(target = "accountIds", source = "accounts", qualifiedByName = "mapAccountIds")
    @Mapping(target = "invoiceIds", source = "invoices", qualifiedByName = "mapInvoiceIds")
    @Mapping(target = "loanIds", source = "loans", qualifiedByName = "mapLoanIds")
    UserDTO toDTO(User user);



    User toEntity(UserRequest userRequest);


    List<UserDTO> toDTOList(List<User> users);

    @Named("mapAccountIds")
    default List<UUID> mapAccountIds(List<Account> accounts) {
        if (accounts == null) return null;
        return accounts.stream()
                .map(Account::getId)
                .collect(Collectors.toList());
    }

    @Named("mapInvoiceIds")
    default List<Long> mapInvoiceIds(List<Invoice> invoices) {
        if (invoices == null) return null;
        return invoices.stream()
                .map(Invoice::getId)
                .collect(Collectors.toList());
    }

    @Named("mapLoanIds")
    default List<Long> mapLoanIds(List<Loan> loans) {
        if (loans == null) return null;
        return loans.stream()
                .map(Loan::getId)
                .collect(Collectors.toList());
    }





    User toRegisterRequest(RegisterRequest registerRequest);
}