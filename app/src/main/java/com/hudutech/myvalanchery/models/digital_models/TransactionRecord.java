package com.hudutech.myvalanchery.models.digital_models;

import java.util.Date;

public class TransactionRecord implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String docKey;
    private String userUid;
    private String accountNo;
    private int transactionType;
    private String purpose;
    private float amount;
    private String desc;
    private Date date;

    public TransactionRecord() {
    }

    public TransactionRecord(String docKey, String userUid, String accountNo, int transactionType, String purpose, float amount, String desc, Date date) {
        this.docKey = docKey;
        this.userUid = userUid;
        this.accountNo = accountNo;
        this.transactionType = transactionType;
        this.purpose = purpose;
        this.amount = amount;
        this.desc = desc;
        this.date = date;
    }

    public String getDocKey() {
        return docKey;
    }

    public void setDocKey(String docKey) {
        this.docKey = docKey;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "TransactionRecord{" +
                "docKey='" + docKey + '\'' +
                ", userUid='" + userUid + '\'' +
                ", accountNo='" + accountNo + '\'' +
                ", transactionType=" + transactionType +
                ", purpose='" + purpose + '\'' +
                ", amount=" + amount +
                ", desc='" + desc + '\'' +
                ", date=" + date +
                '}';
    }
}
