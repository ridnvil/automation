package com.bank.database.automationdatabaseservices.repository;

import com.bank.database.automationdatabaseservices.model.DevicesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface DevicesRepository extends JpaRepository<DevicesModel, Long> {
    @Transactional
    @Modifying
    @Query(value = "update devices_model dm set dm.busy = :busy where dm.udid = :udid", nativeQuery = true)
    void updateBusy(@Param("busy") int busy, @Param("udid") String udid);

    @Transactional
    @Query(value = "select * from devices_model dm where dm.udid = :udid", nativeQuery = true)
    DevicesModel selectOne(@Param("udid") String udid);

    @Transactional
    @Query(value = "select * from devices_model where templogin = 0", nativeQuery = true)
    List<DevicesModel> selectReady();

    @Transactional
    @Query(value = "select * from devices_model", nativeQuery = true)
    List<DevicesModel> showAll();

    @Transactional
    @Modifying
    @Query(value = "update devices_model dm set dm.status = :status where dm.udid = :udid", nativeQuery = true)
    void updateStatus(@Param("status") String status, @Param("udid") String udid);

    @Transactional
    @Modifying
    @Query(value = "update devices_model dm set dm.credit = :credit, dm.active = :active where dm.udid = :udid", nativeQuery = true)
    void updateCredit(@Param("credit") double credit, @Param("active") String active, @Param("udid") String udid);

    @Transactional
    @Modifying
    @Query(value = "update devices_model dm set " +
            "dm.name=:name, dm.number=:number, " +
            "dm.mpin=:mpin, dm.username=:username, " +
            "dm.password=:password, dm.rekening=:rekening, " +
            "dm.provider=:provider, dm.credit=:credit, " +
            "dm.active=:active, dm.destination=:destination, " +
            "dm.bank=:bank, dm.update_at=:update_at " +
            "where dm.udid=:udid", nativeQuery = true)
    void update(
                @Param("udid") String udid,
                @Param("name") String name,
                @Param("number") String number,
                @Param("mpin") String mpin,
                @Param("username") String username,
                @Param("password") String password,
                @Param("rekening") String rekening,
                @Param("provider") String provider,
                @Param("credit") double credit,
                @Param("active") String active,
                @Param("destination") int destination,
                @Param("bank") String bank,
                @Param("update_at") Date update_at
            );

    @Transactional
    @Modifying
    @Query(value = "update devices_model set templogin = 1 where udid = :udid", nativeQuery = true)
    void blockDevices(@Param("udid") String udid);

    @Transactional
    @Modifying
    @Query(value = "update devices_model set templogin = 0 where udid = :udid", nativeQuery = true)
    void openblockDevices(@Param("udid") String udid);

    @Transactional
    @Modifying
    @Query(value = "update devices_model set templogin = 2 where udid = :udid", nativeQuery = true)
    void offDevices(@Param("udid") String udid);

    @Transactional
    @Modifying
    @Query(value = "select * from devices_model where templogin = 1", nativeQuery = true)
    List<DevicesModel> blockedDevices();
}
