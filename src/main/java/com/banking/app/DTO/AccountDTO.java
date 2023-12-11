package com.banking.app.DTO;

import com.banking.app.entities.Customer;
import com.banking.app.entities.Operations;
import com.banking.app.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Data
public class AccountDTO {

    private String id;
    private double balance;
    private CustomerDTO customerDTO = new CustomerDTO();
    private Date createdAt;
    private AccountStatus status;
    private String type;


}

