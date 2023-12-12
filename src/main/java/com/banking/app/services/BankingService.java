package com.banking.app.services;

import com.banking.app.DTO.*;
import com.banking.app.entities.Account;
import com.banking.app.exceptions.AccountBalanceNotEnoughException;
import com.banking.app.exceptions.AccountNotFoundException;
import com.banking.app.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankingService {

    CustomerDTO saveCustomer(CustomerDTO customer);
    List<CustomerDTO> getCustomers();
    CustomerDTO getCustomer(Long id) throws CustomerNotFoundException;
    CurrentAccountDTO saveCurrentAcc(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingAccountDTO saveSavingAcc(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<AccountDTO> getAccounts();

    AccountDTO getAccountDTO(String id) throws AccountNotFoundException;

    Account getAccount(String id) throws AccountNotFoundException;
    void debit(String accId, double amount) throws AccountNotFoundException, AccountBalanceNotEnoughException;
    void credit(String accId, double amount) throws AccountNotFoundException;
    void transfer(String accSourceId, String accTargerId, double amount) throws AccountBalanceNotEnoughException, AccountNotFoundException;
    CustomerDTO updateCustomer(CustomerDTO customerDTO) throws CustomerNotFoundException;
    void deleteCustomer(Long id) throws CustomerNotFoundException;

    List<OperationsDTO> accOperations(String accId) throws AccountNotFoundException;
    AccountHistoryDTO accountHistory(String accId,int page, int size) throws AccountNotFoundException;


    List<CustomerDTO> getCustomerByName(String name) throws CustomerNotFoundException;
}
