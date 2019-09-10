package com.rabo.repository;

import com.rabo.model.CustomerStatement;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerStatementRepository extends JpaRepository<CustomerStatement, Long> {
}
