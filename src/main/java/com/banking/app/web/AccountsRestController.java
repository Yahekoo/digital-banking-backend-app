package com.banking.app.web;

import com.banking.app.DTO.*;
import com.banking.app.exceptions.AccountBalanceNotEnoughException;
import com.banking.app.exceptions.AccountNotFoundException;
import com.banking.app.exceptions.CustomerNotFoundException;
import com.banking.app.services.BankingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class AccountsRestController {

    private BankingService bankingService;

    public AccountsRestController(BankingService bankingService) {
        this.bankingService = bankingService;
    }

    @GetMapping("/accounts")
    public List<AccountDTO> accounts(){
        return this.bankingService.getAccounts();
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO account(@PathVariable String id) throws AccountNotFoundException {
        return this.bankingService.getAccountDTO(id);
    }

    @PostMapping("/saving_account")
    public AccountDTO saveSavingAccount(@RequestBody SavingAccountDTO account) throws CustomerNotFoundException {
        return this.bankingService.saveSavingAcc(account.getBalance(),account.getInterestRate(),account.getCustomerDTO().getId());
    }
    @PostMapping("/current_account")
    public AccountDTO saveCurrentAccount(@RequestBody CurrentAccountDTO account) throws CustomerNotFoundException {
        return this.bankingService.saveCurrentAcc(account.getBalance(),account.getOverDraft(),account.getCustomerDTO().getId());
    }

    @GetMapping("/accounts/{id}/operations")
    public List<OperationsDTO> accOperations(@PathVariable String id) throws AccountNotFoundException {
        return this.bankingService.accOperations(id);
    }

    @GetMapping("/accounts/{id}/acc_history")
    public AccountHistoryDTO accountHistory(@PathVariable String id
                                           ,@RequestParam(name="page",defaultValue = "0") int currentPage
                                           ,@RequestParam(name="size",defaultValue = "5") int size) throws AccountNotFoundException {
        return this.bankingService.accountHistory(id,currentPage,size);
    }

    @PostMapping("/accounts/{id}/debit")
    public AccountDTO debitAccount(@PathVariable String id, @RequestBody OperationsDTO op) throws AccountBalanceNotEnoughException, AccountNotFoundException {
        double amount = op.getAmount();
        if(amount > 0) {
            this.bankingService.debit(id, amount);
        }
        return this.bankingService.getAccountDTO(id);
    }

    @PostMapping("/accounts/{id}/credit")
    public AccountDTO creditAccount(@PathVariable String id, @RequestBody OperationsDTO op) throws AccountBalanceNotEnoughException, AccountNotFoundException {
        double amount = op.getAmount();
        if(amount > 0) {
            this.bankingService.credit(id, amount);
        }
        return this.bankingService.getAccountDTO(id);
    }

}
