package co.istad.mobilebanking.api.accountType;

import java.util.List;

public interface AccountTypeService {

    List<AccountTypeDto> findAllAccountTypes();

    AccountTypeDto findById(Integer id);

    AccountTypeDto createNew(AccountTypeDto accountTypeDto);

    AccountTypeDto updateById(Integer id, AccountTypeDto accountTypeDto);

}
