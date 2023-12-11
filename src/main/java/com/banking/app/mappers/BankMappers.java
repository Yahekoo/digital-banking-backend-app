package com.banking.app.mappers;

import com.banking.app.DTO.*;
import com.banking.app.entities.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

//MapStruct
@Service
public class BankMappers {

    public CustomerDTO customerDTOMapper(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }

    public Customer customerMapper(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    public SavingAccountDTO savingAccDTOMapper(SavingAccount account) {
        SavingAccountDTO accountDTO = new SavingAccountDTO();
        BeanUtils.copyProperties(account,accountDTO);
        BeanUtils.copyProperties(account.getCustomer(),accountDTO.getCustomerDTO());
        return accountDTO;
    }

    public SavingAccount savingAccMapper(SavingAccountDTO accountDTO) {
        SavingAccount account= new SavingAccount();
        BeanUtils.copyProperties(accountDTO,account);
        return account;
    }

    public CurrentAccountDTO currentAccDTOMapper(CurrentAccount account) {
        CurrentAccountDTO accountDTO = new CurrentAccountDTO();
        BeanUtils.copyProperties(account,accountDTO);
        BeanUtils.copyProperties(account.getCustomer(),accountDTO.getCustomerDTO());
        return accountDTO;
    }

    public CurrentAccount currentAccMapper(CurrentAccountDTO accountDTO) {
        CurrentAccount account= new CurrentAccount();
        BeanUtils.copyProperties(accountDTO,account);
        return account;
    }

    public OperationsDTO operationsDTOMapper(Operations op) {
        OperationsDTO operationsDTO = new OperationsDTO();
        BeanUtils.copyProperties(op,operationsDTO);
        return operationsDTO;
    }

    public Operations operationsMapper(OperationsDTO op) {
        Operations operations = new Operations();
        BeanUtils.copyProperties(op,operations);
        return operations;
    }

}
