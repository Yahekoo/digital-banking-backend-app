package com.banking.app.services;

import com.banking.app.entities.Account;
import com.banking.app.entities.CurrentAccount;
import com.banking.app.entities.SavingAccount;
import com.banking.app.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public void consulter() {
        System.out.println("*****************************");
        Account acc = accountRepository.findById("f12c4cb8-ab23-4fd4-a063-c3baeee77fae").orElse(null);
        if(acc != null) {
            System.out.println(acc.getCustomer().getName());
            System.out.println(acc.getBalance());
            System.out.println(acc.getClass().getSimpleName());
            if(acc instanceof SavingAccount) {
                System.out.println("Interest rate => "+((SavingAccount) acc).getInterestRate());
            }else if(acc instanceof CurrentAccount) {
                System.out.println("OverDraft => "+((CurrentAccount) acc).getOverDraft());
            }
            acc.getOperations().forEach( op -> {
                System.out.println(op.getType()+ "\t"+op.getAmount() + "\t" + op.getOperationDate());
            });
        }
    }
}
