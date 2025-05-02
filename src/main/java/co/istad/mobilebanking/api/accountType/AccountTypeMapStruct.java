package co.istad.mobilebanking.api.accountType;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountTypeMapStruct {

    AccountTypeDto toAccountTypeDto(AccountType model);

    List<AccountTypeDto> toListAccountTypeDto(List<AccountType> model);

    AccountType fromAccountTypeDto(AccountTypeDto accountTypeDto);

}
