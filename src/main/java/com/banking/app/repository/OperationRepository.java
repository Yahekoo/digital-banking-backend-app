package com.banking.app.repository;

import com.banking.app.entities.Operations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operations,Long> {

    List<Operations> findByAccount_Id(String accountId);
    Page<Operations> findByAccount_Id(String accountId, Pageable pageable);
}
