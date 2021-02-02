package com.bank.database.automationdatabaseservices.repository;

import com.bank.database.automationdatabaseservices.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
