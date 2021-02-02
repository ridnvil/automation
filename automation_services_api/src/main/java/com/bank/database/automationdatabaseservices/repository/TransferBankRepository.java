package com.bank.database.automationdatabaseservices.repository;

import com.bank.database.automationdatabaseservices.model.TransferBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface TransferBankRepository extends JpaRepository<TransferBank, Long> {

    @Transactional
    @Modifying
    @Query(value = "update transfer_bank tb set tb.status = :status, tb.createat = :createat where tb.uniqueid = :uniqueid", nativeQuery = true)
    void updateData(@Param("status") String status, @Param("createat") Date createat, @Param("uniqueid") String uniqueid);

    @Transactional
    @Modifying
    @Query(value = "delete from transfer_bank where uniqueid = :uniqueid", nativeQuery = true)
    void deleteKey(@Param("uniqueid") String uniqueid);

    @Transactional
    @Query(value = "select * from transfer_bank where uniqueid = :uniqueid", nativeQuery = true)
    TransferBank findByKey(@Param("uniqueid") String uniqueid);

    @Transactional
    @Modifying
    @Query(value = "select bank, uniqueid, udid, status, username, createat from transfer_bank", nativeQuery = true)
    List findAllBanktransfer();

    @Transactional
    @Modifying
    @Query(value = "SELECT  tb.uniqueid, dm.udid deviceid, tb.bank, dm.name, tb.rekening transferto, tb.nominal value, tb.status FROM transfer_bank tb INNER JOIN devices_model dm ON dm.udid = tb.udid", nativeQuery = true)
    List findBycustomQuery();

    @Transactional
    @Modifying
    @Query(value = "SELECT * FROM transfer_bank ORDER BY createat DESC", nativeQuery = true)
    List<TransferBank> findAllByOrderDate();

    @Transactional
    @Modifying
    @Query(value = "SELECT * FROM transfer_bank WHERE udid = :udid AND status = :status", nativeQuery = true)
    List<TransferBank> findByUdid(@Param("udid") String udid, @Param("status") String status);

    @Transactional
    @Modifying
    @Query(value = "SELECT SUM(transfer_bank.nominal) as total FROM transfer_bank WHERE udid = :udid AND status = :status", nativeQuery = true)
    List totalTransfer(@Param("udid") String udid, @Param("status") String status);
}
