package com.banking.app.services;

import com.banking.app.DTO.*;
import com.banking.app.entities.*;
import com.banking.app.enums.AccountStatus;
import com.banking.app.enums.OperationType;
import com.banking.app.exceptions.AccountBalanceNotEnoughException;
import com.banking.app.exceptions.AccountNotFoundException;
import com.banking.app.exceptions.CustomerNotFoundException;
import com.banking.app.mappers.BankMappers;
import com.banking.app.repository.AccountRepository;
import com.banking.app.repository.CustomerRepository;
import com.banking.app.repository.OperationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j @AllArgsConstructor
@Service
@Transactional
public class BankingServiceImpl implements BankingService{

    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;
    private OperationRepository operationRepository;
    private BankMappers mapper;
    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer = mapper.customerMapper(customerDTO);
        Customer saved =  customerRepository.save(customer);
        return mapper.customerDTOMapper(saved);
    }

    @Override
    public CustomerDTO getCustomer(Long id) throws CustomerNotFoundException {
        Customer customer = this.customerRepository.findById(id)
                .orElseThrow( () -> new CustomerNotFoundException("Customer not found"));
        return mapper.customerDTOMapper(customer);
    }

    @Override
    public List<CustomerDTO> getCustomers() {
        return customerRepository.findAll().stream()
                .map( cust-> mapper.customerDTOMapper(cust))
                .toList();
    }

    @Override
    public CurrentAccountDTO saveCurrentAcc(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = this.customerRepository.findById(customerId).orElse(null);
        if(customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        CurrentAccount acc = new CurrentAccount() ;

        acc.setId(UUID.randomUUID().toString());
        acc.setStatus(AccountStatus.CREATED);
        acc.setCreatedAt(new Date());
        acc.setBalance(initialBalance);
        acc.setCustomer(customer);
        acc.setOverDraft(overDraft);
        CurrentAccount savedAcc = accountRepository.save(acc);
        return mapper.currentAccDTOMapper(savedAcc);
    }

    @Override
    public SavingAccountDTO saveSavingAcc(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = this.customerRepository.findById(customerId).orElse(null);
        if(customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        SavingAccount acc = new SavingAccount() ;

        acc.setId(UUID.randomUUID().toString());
        acc.setCreatedAt(new Date());
        acc.setStatus(AccountStatus.CREATED);
        acc.setBalance(initialBalance);
        acc.setCustomer(customer);
        acc.setInterestRate(interestRate);
        SavingAccount savedAcc = accountRepository.save(acc);
        return mapper.savingAccDTOMapper(savedAcc);
    }

    @Override
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream().map(this::accountDetecter).toList();
    }

    private AccountDTO accountDetecter(Account acc) {
        if (acc instanceof SavingAccount) {
            SavingAccount savingAcc = (SavingAccount) acc;
            return mapper.savingAccDTOMapper(savingAcc);
        } else {
            CurrentAccount currentAcc = (CurrentAccount) acc;
            return mapper.currentAccDTOMapper(currentAcc);
        }
    }

    @Override
    public AccountDTO getAccountDTO(String id) throws AccountNotFoundException {
        Account acc =  getAccount(id);
        if(acc instanceof CurrentAccount) {
            CurrentAccount currentAcc = (CurrentAccount)acc;
            return mapper.currentAccDTOMapper(currentAcc);
        } else {
            SavingAccount savingAcc = (SavingAccount) acc;
            return mapper.savingAccDTOMapper(savingAcc);
        }
    }

    @Override
    public Account getAccount(String id) throws AccountNotFoundException {
        return accountRepository.findById(id)
                .orElseThrow( () -> new AccountNotFoundException("Account not found"));
    }

    @Override
    public void debit(String accId, double amount) throws AccountNotFoundException, AccountBalanceNotEnoughException {
        Account acc = getAccount(accId);
        if(acc.getBalance() < amount) {
            throw new AccountBalanceNotEnoughException("Balance not sufficient");
        }
        Operations op = new Operations();
        op.setType(OperationType.DEBIT);
        op.setAccount(acc);
        op.setAmount(amount);
        op.setOperationDate(new Date());
        acc.setBalance(acc.getBalance() - amount);
        operationRepository.save(op);
        accountRepository.save(acc);
    }

    @Override
    public void credit(String accId, double amount) throws AccountNotFoundException {
        Account acc = getAccount(accId);
        Operations op = new Operations();
        op.setType(OperationType.CREDIT);
        op.setAccount(acc);
        op.setAmount(amount);
        op.setOperationDate(new Date());
        acc.setBalance(acc.getBalance() + amount);
        operationRepository.save(op);
        accountRepository.save(acc);

    }

    @Override
    public void transfer(String accSourceId, String accTargerId, double amount) throws AccountBalanceNotEnoughException, AccountNotFoundException {
         debit(accSourceId,amount);
        credit(accTargerId,amount);

    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) throws CustomerNotFoundException {
        if(getCustomer(customerDTO.getId()) == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        return saveCustomer(customerDTO);
    }

    @Override
    public void deleteCustomer(Long id) throws CustomerNotFoundException {
        CustomerDTO customerDTO = getCustomer(id);
        if( customerDTO == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        customerRepository.deleteById(id);
    }

    @Override
    public List<OperationsDTO> accOperations(String accId) throws AccountNotFoundException {
        return  operationRepository.findByAccount_Id(accId).stream().map(operation -> mapper.operationsDTOMapper(operation)).toList();
    }

    @Override
    public AccountHistoryDTO accountHistory(String accId,int page, int size) throws AccountNotFoundException {
       Account acc = getAccount(accId);
       AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
       Page<Operations> ops = operationRepository.findByAccount_Id(accId, PageRequest.of(page,size));
       List<OperationsDTO> opsDTO = ops.getContent().stream().map(op -> mapper.operationsDTOMapper(op)).toList();
       accountHistoryDTO.setOperationsDTO(opsDTO);
       accountHistoryDTO.setAccountId(acc.getId());
       accountHistoryDTO.setBalance(acc.getBalance());
       accountHistoryDTO.setPageSize(size);
       accountHistoryDTO.setCurrentPage(page);
       accountHistoryDTO.setTotalPages(ops.getTotalPages());
       return accountHistoryDTO;

    }


}
