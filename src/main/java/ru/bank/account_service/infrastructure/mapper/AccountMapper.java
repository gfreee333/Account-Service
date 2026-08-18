package ru.bank.account_service.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bank.account_service.model.dto.request.RegistrationRequestDto;
import ru.bank.account_service.model.entity.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "accountNumber", ignore = true)
    @Mapping(target = "accountStatus", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    Account registrationToEntity(RegistrationRequestDto request);

}
