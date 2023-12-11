package com.banking.app.DTO;

import com.banking.app.entities.Customer;
import com.banking.app.entities.Operations;
import com.banking.app.enums.AccountStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SavingAccountDTO extends AccountDTO{

    private double interestRate;
    private String type = "SAVING";
}
