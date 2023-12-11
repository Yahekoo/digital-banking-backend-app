package com.banking.app.DTO;

import com.banking.app.entities.Customer;
import com.banking.app.enums.AccountStatus;
import lombok.Data;

import java.util.Date;

@Data
public class CurrentAccountDTO extends AccountDTO{


    private double overDraft;
    private String type = "CURRENT";
}
