package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "NEIF_DUMP_TEMP")
public class RefillDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false) 
    private Long id;

    @Column(name = "MESSAGE_REFERENCE", length = 50)
    private String messageReference;

    @Column(name = "IMSI", length = 50)
    private String imsi;

    @Column(name = "MSISDN", length = 50)
    private String msisdn;

    @Column(name = "NEIF_INFORMATION", length = 50)
    private String neifInformation;

    @Column(name = "ACCOUNT_PROFILE", length = 50)
    private String accountProfile;

    @Column(name = "TIME_STAMP", length = 50)
    private String timeStamp;

    @Column(name = "REFILL_COUNT", length = 50)
    private String refillCount;

    @Column(name = "MAIN_BONUS", length = 50)
    private String mainBonus;

    @Column(name = "BONUS_AMOUNT")
    private Double bonusAmount;

    @Column(name = "SCRATCH_CARD_NUMBER", length = 50)
    private String scratchCardNumber;

    @Column(name = "SCRATCH_CARD_PROFILE", length = 50)
    private String scratchCardProfile;

    @Column(name = "TAC", length = 20)
    private String tac;

    // --- GETTER & SETTER ---
    // (Vì code quá dài, bạn dùng chức năng Generate Getter/Setter của IDE để tạo ra nhé)
    public void setMessageReference(String messageReference) { this.messageReference = messageReference; }
    public void setImsi(String imsi) { this.imsi = imsi; }
    public void setMsisdn(String msisdn) { this.msisdn = msisdn; }
    public void setNeifInformation(String neifInformation) { this.neifInformation = neifInformation; }
    public void setAccountProfile(String accountProfile) { this.accountProfile = accountProfile; }
    public void setTimeStamp(String timeStamp) { this.timeStamp = timeStamp; }
    public void setRefillCount(String refillCount) { this.refillCount = refillCount; }
    public void setMainBonus(String mainBonus) { this.mainBonus = mainBonus; }
    public void setBonusAmount(Double bonusAmount) { this.bonusAmount = bonusAmount; }
    public void setScratchCardNumber(String scratchCardNumber) { this.scratchCardNumber = scratchCardNumber; }
    public void setScratchCardProfile(String scratchCardProfile) { this.scratchCardProfile = scratchCardProfile; }
    public void setTac(String tac) { this.tac = tac; }
}