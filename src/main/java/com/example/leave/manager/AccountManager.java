package com.example.leave.manager;

import com.example.leave.entity.Account;
import com.example.leave.facade.AccountFacade;
import com.example.leave.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Medion on 2016-09-13.
 */
@Component
public class AccountManager implements AccountManagerInterface {
    @Autowired
    AccountFacade accountFacade;

    @Override
    public void registerAccount(Account account) {
        accountFacade.registerAccount(account);
    }
}
