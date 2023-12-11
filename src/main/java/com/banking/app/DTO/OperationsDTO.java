package com.banking.app.DTO;

import com.banking.app.entities.Account;
import com.banking.app.enums.OperationType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
public class OperationsDTO {

    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType type;



}
