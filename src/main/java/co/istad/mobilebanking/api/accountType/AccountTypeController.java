package co.istad.mobilebanking.api.accountType;

import co.istad.mobilebanking.base.BaseRest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/account-types")
@RequiredArgsConstructor
public class AccountTypeController {

    private final AccountTypeService accountTypeService;

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_account:read')")
    public BaseRest<?> findAll() {
        var accountTypesDtoList = accountTypeService.findAllAccountTypes();
        return BaseRest.builder()
                .status(true)
                .code(HttpStatus.OK.value())
                .message("Account types have been found")
                .timestamp(LocalDateTime.now())
                .data(accountTypesDtoList)
                .build();

    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('SCOPE_account:read')")
    public BaseRest<?> findById(@PathVariable Integer id) {
        var accountType = accountTypeService.findById(id);
        return BaseRest.builder()
                .status(true)
                .code(HttpStatus.OK.value())
                .message("Account types has been found")
                .timestamp(LocalDateTime.now())
                .data(accountType)
                .build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_account:write')")
    public BaseRest<?> createNew(@Valid @RequestBody AccountTypeDto body) {
        AccountTypeDto accountTypeDto = accountTypeService.createNew(body);
        return BaseRest.builder()
                .status(true)
                .code(HttpStatus.OK.value())
                .message("Account types has been created")
                .timestamp(LocalDateTime.now())
                .data(accountTypeDto)
                .build();
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('SCOPE_account:update')")
    public BaseRest<?> updateById(@PathVariable Integer id, @Valid @RequestBody AccountTypeDto body) {
        AccountTypeDto accountTypeDto = accountTypeService.updateById(id, body);
        return BaseRest.builder()
                .status(true)
                .code(HttpStatus.OK.value())
                .message("Account types has been updated successfully")
                .timestamp(LocalDateTime.now())
                .data(accountTypeDto)
                .build();
    }
}
