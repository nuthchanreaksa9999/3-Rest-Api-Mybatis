package co.istad.mobilebanking.api.accountType;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountTypeServiceImpl implements AccountTypeService {

    private final AccountTypeMapper accountTypeMapper;
    private final AccountTypeMapStruct accountTypeMapStruct;

    @Override
    public List<AccountTypeDto> findAllAccountTypes() {
        List<AccountType> accountTypes = accountTypeMapper.select();

/*        List<AccountTypeDto> accountDtoList = accountTypes.stream()
                .map(accountType -> new AccountTypeDto(accountType.getName()))
                .toList(); */

        return accountTypeMapStruct.toListAccountTypeDto(accountTypes);
    }

    @Override
    public AccountTypeDto findById(Integer id) {
        AccountType accountType = accountTypeMapper.selectById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Account type with id = %s not found", id)));
        return accountTypeMapStruct.toAccountTypeDto(accountType);
    }

    @Override
    public AccountTypeDto createNew(AccountTypeDto accountTypeDto) {
        AccountType accountType = accountTypeMapStruct.fromAccountTypeDto(accountTypeDto);
        accountTypeMapper.insert(accountType);
        return accountTypeMapStruct.toAccountTypeDto(accountType);
    }

    @Override
    public AccountTypeDto updateById(Integer id, AccountTypeDto accountTypeDto) {
        AccountType accountType = accountTypeMapStruct.fromAccountTypeDto(accountTypeDto);
        accountType.setId(id);
        accountTypeMapper.update(accountType);
        return accountTypeMapStruct.toAccountTypeDto(accountType);
    }
}
